package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  def apply(e: A):Boolean =
    contains(e)

  def contains(e: A): Boolean
  def +(e: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}

class EmptySet[A] extends MySet[A] {
  def contains(e: A): Boolean = false
  def +(e: A): MySet[A] = new NonEmptySet[A](e, tail=this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(e: A): Boolean =
    e == head || tail.contains(e)

  def +(e: A): MySet[A] =
    if (this.contains(e)) this
    else new NonEmptySet[A](e, this)

  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] =
    (tail map f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] =
    (tail flatMap f) ++ f(head)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail  = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head);
    tail foreach f
  }
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    println(values)
    @tailrec
    def buildSet(seq: Seq[A], acc: MySet[A]):MySet[A] = {
      if (seq.isEmpty) acc
      else buildSet(seq.tail, acc + seq.head)
    }
    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1,2,3,4);
  s + 5 ++ MySet(-2,-5) flatMap   (x => MySet(x,x * 10))
}

