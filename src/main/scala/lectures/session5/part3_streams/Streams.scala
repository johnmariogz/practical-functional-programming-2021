package lectures.session5.part3_streams

import akka.Done
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{
  Broadcast,
  FileIO,
  Flow,
  Framing,
  GraphDSL,
  RunnableGraph,
  Sink,
  Source
}
import akka.util.ByteString
import exercises.shared.Helpers

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Streams extends App {
  private val SourceFile = "src/main/resources/fahrenheit.txt"
  private val FinalFile  = "src/main/resources/celsius.txt"
  private val MaxFrame   = 4096

  val system = ActorSystem("streams-example")
  import system.dispatcher // Use the thread pool from the actors to execute everything
  implicit val materializer: Materializer = Materializer(system)

  sealed trait ConversionResult
  case class ConversionSuccessful(fahrenheit: Double, celsius: Double) extends ConversionResult
  case class FailedConversion(input: String)                           extends ConversionResult

  def sleepMillis(sleepTimeMs: Int): Unit = {
    val start = System.nanoTime()
    while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < sleepTimeMs) {}
  }

  def makeNumberConversion(input: String): Future[ConversionResult] = Future {
    input.toDoubleOption match {
      case Some(fahrenheit) =>
        sleepMillis(100) // Expensive transformation
        val celsius = fahrenheitToCelsius(fahrenheit)
        ConversionSuccessful(fahrenheit, celsius)
      case None =>
        sleepMillis(200) // Some report must be done here
        FailedConversion(input)
    }
  }

  def fahrenheitToCelsius(f: Double): Double =
    (f - 32.0) * (5.0 / 9.0)

  val storeFuture: Future[Done] = Helpers.measureTimeF("store") {
    buildTemperatureProcessingStream(
      SourceFile,
      FinalFile,
      8
    ) // TODO - What if we change the parallelism?
  }

  def buildTemperatureProcessingStream(inputFile: String, outputFile: String, parallelism: Int) = {
    def combineResults[A, B](f1: Future[A], f2: Future[B]): Future[Done] =
      for {
        _ <- Future.sequence(List(f1, f2))
      } yield Done

    val printSink: Sink[ConversionResult, Future[Done]] =
      Sink.foreach[ConversionResult](result => println(s"The result is: $result"))

    val storeToFileSink: Sink[ByteString, Future[IOResult]] =
      FileIO.toPath(Paths.get(outputFile))

    val graph = RunnableGraph.fromGraph(
      GraphDSL.createGraph(printSink, storeToFileSink)(combineResults(_, _)) {
        implicit builder => (printSink, storeToFileSink) =>
          import GraphDSL.Implicits._

          // Step 1: Read the file
          val sourceReadFile: Source[ByteString, Future[IOResult]] =
            FileIO.fromPath(Paths.get(inputFile))

          // Step 2(.1): Split each line by \n
          val splitLine: FlowShape[ByteString, ByteString] =
            builder.add(Framing.delimiter(ByteString("\n"), MaxFrame))

          // Step 2(.2): Make each line usable by converting it from bytes to a string
          val convertToString: FlowShape[ByteString, String] =
            builder.add(Flow[ByteString].map(_.utf8String))

          // Step 3: Convert each line fahrenheit => celsius (if possible)
          val convertFahrenheitToCelsius: FlowShape[String, ConversionResult] = builder.add(
            Flow[String].mapAsync(parallelism)(makeNumberConversion)
          )

          // Broadcast results to print & store
          val broadcast: UniformFanOutShape[ConversionResult, ConversionResult] =
            builder.add(Broadcast[ConversionResult](2))

          // Step 4(.1) // Separate conversions from failures (and keep the degrees only)
          val separateConversions: FlowShape[ConversionResult, Double] = builder.add(
            Flow[ConversionResult]
              .collect({ case ConversionSuccessful(_, celsius) => celsius })
          )

          // Step 4(.2.) // Convert the degrees to a usable format that can be stored
          val convertDegreesToByteString: FlowShape[Double, ByteString] = builder.add(
            Flow[Double].map(degrees => ByteString(s"$degrees\n"))
          )

          // Step 5. Store only valid conversion results [with storeToFileSink]

          // Step 6. Print all results [with printSink]

          // format: off
          //[- step 1 -] -> [-------- step 2 ----------] -> [-------- step 3 --------] -> [ split ] -> [------------------- step 4 --------------------] -> [--- step 5 ---]
          sourceReadFile ~> splitLine ~> convertToString ~> convertFahrenheitToCelsius ~> broadcast ~> separateConversions ~> convertDegreesToByteString ~> storeToFileSink
                                                                                          broadcast ~> printSink
          //                                                                              [ split ] -> [-step 6-]
          // format: on
          ClosedShape
      }
    )

    graph.run()
  }

  Await.ready(storeFuture, 20.seconds)
  Await.ready(system.terminate(), 1.second)
}
