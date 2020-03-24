package lectures

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  class SimpleContainer[T](private var value: T = 0) {
    def map[B](f: T => B): SimpleContainer[B] =
      new SimpleContainer[B](f(value))

    def get: T = value

    def set(newValue: T): Unit =
      value = newValue

    def isEmpty: Boolean = value == 0
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer]: waiting")
      while (container.isEmpty) {
        println("[consumer] actively waiting..")
      }
      println("[consumer] I have consumed " + container.get)
    })
    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value: Int = 42;
      println(s"[producer] I produced $value")
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  //  naiveProdCons()
  def smartProdCons(): Unit = {
    val container = new SimpleContainer[Int]
    val consumer = new Thread(() => {
      println("[consumer] waiting..")
      container.synchronized {
        container.wait()
      }
      println("[consumer] I have consumed " + container.get)
    })
    val producer = new Thread(() => {
      println("[producer] computing..")
      Thread.sleep(2000)
      container.synchronized {
        val value = 42;
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }

  //  smartProdCons()

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3
    val consumer = new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          val x = buffer.dequeue()
          println("[consumer] consumed " + x)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(100))
      }
    })
    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }
          println("[producer] producing.. " + i)
          buffer.enqueue(i)
          buffer.notify()
          i += 1
          Thread.sleep(random.nextInt(500))
        }
      }
    })
    consumer.start()
    producer.start()
  }
  prodConsLargeBuffer()
}
