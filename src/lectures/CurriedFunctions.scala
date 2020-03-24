package lectures

object CurriedFunctions extends App {

  //curried function
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)
  //println(superAdder(5)(4))

  def curriedAdder(x: Int)(y: Int): Int = x + y
  //println(curriedAdder(4) _)

  // Partial Function applications
  val add5 = curriedAdder(5) _

  // EXERCISES
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def curriedAddMethod(x: Int)(y:Int):Int = x + y
  // add 7: Int => Int = y => 7 + y
  // as many different implementations of add 7 using the above;
  val add7 = (x: Int) => simpleAddFunction(7,x)
  val add7curried = curriedAddMethod(7) _
  val add7_2 = curriedAddMethod(7)( _: Int)

  println(add7(5))
  println(s"CurriedMethod: %s".format(add7curried(5)))
  println(s"CurriedMethod1.5: %s".format(add7_2(5)))
  println(s"CurriedMethod2: %s".format(curriedAddMethod(7)(5)))
  println(s"simpleAddCurried: %s".format(simpleAddFunction.curried(7)(5)))

  def concatenator(a: String, b: String, c: String): String = a + b + c
  val insertName = concatenator("Hello, I'm ",_: String, ", how are you?")
  println(insertName("Florian"))

  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1

  def method: Int = 42
  def parentMethod(): Int = 43

  println(s"Calling by name %s".format(byName(method)))
  println(s"Calling by function %s".format(byFunction(() => 1)))
}
