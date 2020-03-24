package lectures

import java.util.concurrent.Executors

object Concurrent extends App {

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })
  aThread.start() // this creates a jvm thread
  aThread.join() // blocks the until the thread finished running

  val threadHello = new Thread(() =>(1 to 5).foreach(
    _ => println("Hello")
  ))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(
    _ => println("Goodbye")
  ))
  threadHello.start()
  threadGoodbye.start()

  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("Something in a thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("Done after one second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("Almost done")
    Thread.sleep(1000)
    println("Done after two seconds")
  })


  def runInParallel = {
    var x = 0;
    val t1 = new Thread(() => {
      x = 1
    })
    val t2 = new Thread(() => {
      x = 2
    })
    t1.start()
    t2.start()
    println(x)
  }

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }
  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.amount -= price
    println("I've bought " + thing)
    println("My account is now " + account)
  }
//  for (_ <- 1 to 1000) {
//    val account = new BankAccount(50000)
//    val t1 = new Thread(() => buySafe(account, "shoes", 3000))
//    val t2 = new Thread(() => buySafe(account, "iPhone", 4000))
//    if (account.amount != 43000) println("AHA: " + account.amount)
//  }

//   option #1: use synchronized
  def buySafe(account: BankAccount, thing: String, price: Int) =  {
    account.synchronized {
      account.amount -= price
      println("I've bought " + thing)
      println("My account is now " + account)
    }
  }

}
