package app.infrastructure

import java.util.concurrent.{ExecutorService, Executors}

import fs2.{Strategy, Stream, Task}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

package object concurrency {

  def createExecutorService(threads: Int, threadNameFormat: String): ExecutorService = {
    Executors.newFixedThreadPool(threads, Strategy.daemonThreadFactory(threadNameFormat))
  }

  def createExecutionContext(threads: Int, threadNameFormat: String): ExecutionContextExecutorService = {
    ExecutionContext.fromExecutorService(createExecutorService(threads, threadNameFormat))
  }

  def lift[A, B](f: A => Task[B]): A => Stream[Task, B] = a => Stream.eval(f(a))

}
