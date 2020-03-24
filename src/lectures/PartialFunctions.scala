package lectures

object PartialFunctions extends App {
  val aFunction = (x: Int) => x + 1

  val chatBotAnswer: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Noo do not leave meee"
    case "you are great" => "Thanks you to"
    case _ => "I don't know."
  }

  scala.io.Source.stdin.getLines().map(chatBotAnswer).foreach(println)
}
