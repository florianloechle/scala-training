package lectures

import scala.util.Try

object Monads extends App {

  // our own try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
    def map[B](f: A => B): Attempt[B]
  }
  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Failure(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] = {
      try {
        f(value)
      } catch {
        case e: Throwable => Failure(e)
      }
    }

    def map[B](f: A => B): Attempt[B] =
      new Success[B](f(value))
  }
  case class Failure(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
    def map[B](f: Nothing => B): Attempt[B] = this
  }

  Try[Int] {
    42
  }
    .map((v: Int) => println("Got a value"))
    .toEither
    .getOrElse()


  val none = None
  none.map((v: Int) => v + 1)
 println(none)






}
