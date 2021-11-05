package lectures.session4.part_1_functional_state

object Helpers {
  // Very simple state monad
  sealed trait ST[S, A] { self =>
    protected def run(s: S): (A, S) // Protected because it means that the state can be mutated

    def map[B](f: A => B): ST[S, B] = new ST[S, B] {
      override protected def run(s: S): (B, S) = {
        val (a, s1) = self.run(s)
        (f(a), s1)
      }
    }

    def flatMap[B](f: A => ST[S, B]): ST[S, B] = new ST[S, B] {
      override protected def run(s: S): (B, S) = {
        val (a, s1) = self.run(s)
        f(a).run(s1)
      }
    }
  }

  object ST {
    def apply[S, A](a: => A): ST[S, A] = {
      lazy val memo = a
      new ST[S, A] {
        override def run(s: S): (A, S) = (memo, s)
      }
    }

    def runST[A](runnableST: RunnableST[A]): A = {
      val (run, _) = runnableST.apply[Unit].run(())
      run
    }
  }

  // Fancy wrapper around a protected var
  sealed trait STRef[S, A] { // Type S is NOT the type of the cell and it's not used, but WE need it for ST[S, ?]
    protected var cell: A // Mutability is present AND controlled
    def read: ST[S, A] = ST(cell)
    def write(a: A): ST[S, Unit] = new ST[S, Unit] {
      override protected def run(s: S): (Unit, S) = {
        cell = a
        ((), s)
      }
    }
  }

  object STRef {
    // This is not optimal because it returns a mutable reference
    def apply[S, A](a: A): ST[S, STRef[S, A]] = ST(new STRef[S, A] {
      override protected var cell: A = a
    })
  }

  // When the computation is encapsulated in this operation, it's safe to run
  trait RunnableST[A] {
    def apply[S]: ST[S, A]
  }
}
