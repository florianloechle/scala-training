package lectures

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Random, Success}

object FuturesAndPromises extends App {

  trait Thing[T] {
    def map[B](f: T => B): Thing[B]

    def toString: String
  }

  class MyThing[T](private val value: T) extends Thing[T] {
    override def map[B](f: T => B): Thing[B] =
      new MyThing[B](f(value))

    override def toString: String = "" + value
  }

  object MyThing {
    def apply[T](v: => T): MyThing[T] =
      new MyThing[T](v)
  }

  //  val x = MyThing {
  //    var x = 0
  //    x + 1
  //  }
  //  println(x.map(x => x +1).toString)

  def calculateMeaningOfLive: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture: Unit = Future {
    calculateMeaningOfLive
  }.onComplete {
    case Success(meaningOfLive) =>
      println("The meaning of live is " + meaningOfLive)
    case Failure(e) =>
      println(s"I have failed with $e")
  }
  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit = {
      println(s"${this.name} is poking ${anotherProfile.name}!")
    }
  }

  object SocialNetwork {
    val names = Map(
      "fb.id.1-zuk" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuk" -> "fb.id.2-bill"
    )

    val random = new Random()

    //API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(500))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(500))
      val bfID = friends(profile.id)
      Profile(bfID, names(bfID))
    }
  }

  val mark = SocialNetwork.fetchProfile("fb.id.1-zuk")
    .onComplete {
      case Success(markProfile) => SocialNetwork.fetchBestFriend(markProfile)
        .onComplete {
          case Success(billProfile) => markProfile.poke(billProfile)
          case Failure(e) => e.printStackTrace()
        }
      case Failure(e) => e.printStackTrace()
    }

  // functional composition of futures
  val intFuture = Future {  1 }
  val addFuture = intFuture.map(x => x + 1)
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuk")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  // fallbacks

  SocialNetwork.fetchProfile("blala").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
  }
  SocialNetwork.fetchProfile("unknown").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  Thread.sleep(2000)

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    final val name = "Rock the jvm"
    def fetchUser(name: String):Future[User] = Future {
      Thread.sleep(1000)
      User(name)
    }
    def createTransaction(user: User, merchantName: String, amount: Double):Future[Transaction] =
      Future {
        Thread.sleep(1000)
        Transaction(sender=user.name, receiver=merchantName,amount=amount,"SUCCESS")
      }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user form the DB
      // create a transaction
      // WAIT FOR THE TRANSACTION TO FINISH
      val transactionStatusFuture = for  {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status
      Await.result(transactionStatusFuture, 2.seconds)
    }

  }
  println(BankingApp.purchase("Daniel","iPhone","Apple", 899.99))


}
