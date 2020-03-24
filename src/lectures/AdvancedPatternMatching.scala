package lectures

object AdvancedPatternMatching extends App {
  val numbers: List[Int] = List(1)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ =>
  }

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }
  val bob = new Person(name="BoB", age=25);
  val greeting = bob match {
    case Person(n,a) => s"Hi my name is $n and I am $a years old."
  }
  println(greeting)
  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }
  println(legalStatus)

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0;
  }
  object singleDigit {
    def unapply(arg: Int): Boolean = arg > - 10 && arg < 10
  }
  val mathProperty = 8 match {
    case singleDigit => s"Single Digit Number"
    case even => s"An even Number"
    case _ => s"No property"
  }
  println(mathProperty)
  case class Or[A,B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }
  println(humanDescription)
  val vararg = numbers match {
    case List (1,_*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1,2,_*) => "starting with 1 and 2"
    case _ => "something else"
  }
  println(decomposed)
  
  // custom return types for unapply
  // isEmpty: Boolean, get: Something
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }
  object PersonWrapper {
    def unapply(arg: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get: String = arg.name
    }
  }
  println(bob match {
    case PersonWrapper(n) => s"This persons name is $n"
    case _ => "An alien"
  })
}
