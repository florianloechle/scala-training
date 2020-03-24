package playground

sealed trait Stack[A] {
  def pop(): Unit
  def push(element: A): Unit
  def peek: A
  def foreach(f: A => Unit)
}

class MyStack[A](values: List[A]) extends Stack[A] {
  private var elements: List[A] = values
  def pop(): Unit = {
    val currentTop = peek
    elements = elements.tail
    currentTop
  }

  def peek: A = elements.head
  def push(element: A): Unit = { elements = element :: elements }
  def foreach(f: A => Unit): Unit = {
       elements.foreach(f)
  }
  def map[B](f: A => B): MyStack[B] = {
    new MyStack[B](elements.map(f))
  }
}

object Stack {
  def apply[A](values: A*):MyStack[A] = {
    new MyStack(values.toList)
  }
}

object MyApp extends App {
  val s = Stack("hallo, erstes Element","2","4")
  println(s.peek)
  val x = s.map(v => v + "!!\n")
  x.foreach(e => print(e))
}
