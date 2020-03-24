package lectures

object Implicits extends App {
  val pair = "Test" -> "test"

  case class Person(name: String, age: Int) {
    def greet = s"Hello my name is $name"
  }
  implicit def fromStringToPerson(str: String):Person = Person(str, 42)
  println("Peter".greet)

  val persons: List[Person] = List(
    Person("Zebra", 32),
    Person("Alex", 70),
    Person("Mike", 23)
  )

  // LOCAL IMPLICITS > OBJECT / CLASS IMPLICITS > GLOBAL IMPLICITS
  //implicit def orderPersonsAlphabetic: Ordering[Person] =
    //Ordering.fromLessThan(_.name > _.name)
  object AlphabeticOrdering {
    implicit val alphabeticNameOrdering: Ordering[Person] =
      Ordering.fromLessThan(_.name < _.name)
  }
  import AlphabeticOrdering._

  println(persons.sorted)

  /*
  Exercise
  - totalPrice = most used (50%)
  - by unit count = 25%
  - by unit price = 25%
   */

  case class Purchase(nUnits: Int, unitPrice: Double)
  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((pa, pb) =>
        pa.nUnits * pa.unitPrice > pb.nUnits * pb.unitPrice)
  }
  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] =
      Ordering.fromLessThan(_.nUnits > _.nUnits)
  }
  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan(_.unitPrice > _.unitPrice)
  }


  val purchases: List[Purchase] = List(
    Purchase(10, 9.99),
    Purchase(100,2.99),
    Purchase(2,699.99)
  )
  println(purchases.sorted)
}
