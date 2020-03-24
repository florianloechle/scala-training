package lectures

object PimpMyLibraryWithImplicits extends App {
  // 2.isPrime

  implicit class RichInt(value: Int) {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
    def times(f: Int => Unit): Unit =
      for { i <- 1 to value } f(i)
  }
  println(new RichInt(42))
  println(42.isEven)
  // type enrichment, pimping

  import scala.concurrent.duration._
  3.seconds

  implicit class EnrichedString(value: String) {
    def asInt: Int = Integer.valueOf(value)
  }
  println("5".asInt)
  println(5.times(index => println("Scala Rocks!")))
}
