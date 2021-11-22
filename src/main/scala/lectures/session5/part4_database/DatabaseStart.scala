package lectures.session5.part4_database

import cats.effect.IO
import doobie.util.transactor.Transactor
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.flywaydb.core.Flyway

private[part4_database] trait DatabaseStart {
  def startDatabase(): IO[EmbeddedPostgres] =
    IO.delay(EmbeddedPostgres.start())

  def buildTransactor(postgres: EmbeddedPostgres): IO[Transactor[IO]] =
    IO.delay(
      Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        postgres.getJdbcUrl("postgres", "postgres"),
        "postgres",
        "postgres"
      )
    )

  def applyMigrations(postgres: EmbeddedPostgres): IO[Unit] = {
    for {
      flyway <- IO.pure(
        Flyway
          .configure()
          .dataSource(postgres.getDatabase("postgres", "postgres"))
          .load()
      )
      _ <- IO.delay(flyway.migrate())
    } yield ()
  }
}
