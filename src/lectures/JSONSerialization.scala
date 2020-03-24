package lectures

import java.util.Date

object JSONSerialization extends App {

  /*
  Users, posts, feeds
   */
  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  sealed trait Boxable {
    def boxify: String
  }

  final case class JSONObject(value: Map[String, Boxable]) extends Boxable {
    
  }
}
