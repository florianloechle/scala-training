package lectures

import scala.util.Try

object DarkSugars extends App {
  // syntax sugar #1: methods with single param
  def singleArgumentMethod(arg: Int): String = {
    s"$arg little ducks"
  }
  val description = singleArgumentMethod {
    // some code
    10
  }
  val aTryInstance = Try {
    throw new RuntimeException()
  }

  List(1,2,3) map { x => x + 1 }

  // syntax sugar #2: signle abstract method
  trait Action {
    def act(x: Int): Int
  }
  val aInstance = new Action {
    override def act(x: Int): Int = 10
  }
  val aFunkyInstance: Action = (x: Int) => x + 1 //magic
  //example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hello Scala")
  })
  val aSweeterThread = new Thread(() => println("Sweet Scala"))

  abstract class AType {
    def implemented: Int = 32
    def f(a: Int): Unit
  }
  val anAbstractInstance: AType = (a: Int) => println("Hello Abstract")

  //syntax sugar #3: the :: and #:: methods
  val prependedList = 2 :: List(3,4)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // syntax sugar #4: multi word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }
  val lilly = new TeenGirl(name="Lilly")
  lilly `and then said` "Scala is so sweet"

  // syntax sugar #5: infix types
  class Composite[A, B] {

  }

  //val composite: Int Composite String = new Composite[1]
  class -->[A,B]
  //val towards: Int --> String =

  // syntax sugar #6: update() is very special, much like apply
  val anArray = Array(1,2,3)
  anArray(2) = 7 // rewritten to anArray.update(2,7)
  //used in mutable collection types

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0
    def member:Int = internalMember // getter
    def member_=(value: Int):Unit = internalMember = value // setter
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_=(42);


}
