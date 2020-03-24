package lectures

import javax.naming.PartialResultException

object TypeClasses extends App {
  trait HTMLWriteable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String)
  val flo = User("Flo", 24, "flo@flo.de")
  val david = User("David", 22, "david@flo.de")
  //println(flo.toHTML)

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }
  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(value: User): String =
      s"<div>${value.name} is ${value.age} old.</div>"
  }
  //println(UserSerializer.serialize(flo))
  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(value: User): String =
      s"<a>${value.email}</a>"
  }
  //println(PartialUserSerializer.serialize(flo))

  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String = ???
  }

  trait Equal[T] {
    def apply(va: T, vb: T):Boolean = va == vb
  }
  implicit object NameEquality extends Equal[User] {
    override def apply(va: User, vb: User): Boolean =
      va.name == vb.name
  }
  //println(NameEquality(flo, flo))

  object HTMLSerializer {
    def apply[T](implicit serializer: HTMLSerializer[T]):HTMLSerializer[T] = serializer
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]):String =
      serializer.serialize(value)
  }
  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }
  //println(HTMLSerializer.serialize(43))
  //println(HTMLSerializer.serialize(flo))
  val userSerializer = HTMLSerializer[User].serialize(flo)
  object Equal {
    def apply[T](va: T, vb: T)(implicit Equality: Equal[T]): Boolean =
      Equality(va,vb)
  }
  //println(Equal(flo,flo))
  implicit class Equals[T](value: T) {
    def ===(anotherValue: T)(implicit equality: Equal[T]):Boolean =
      equality.apply(value, anotherValue)
    def !==(anotherValue: T)(implicit equality: Equal[T]):Boolean =
      !equality.apply(value, anotherValue)

  }
  println(s"Equal: ${flo === flo}")
  println(s"Equal: ${flo !== david}")

  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)
  }

  println(flo.toHTML)
  // Cool, extend this to more types
  //println(2.toHTML)
  println(flo.toHTML(PartialUserSerializer))

}
