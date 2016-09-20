package app.infrastructure

import java.util.concurrent.{ExecutorService, Executors, ThreadFactory}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}
import scala.util.{Failure, Success}
import scalaz.concurrent.{Strategy, Task}
import scalaz._
import Scalaz._

package object concurrency {

  def daemonThreadFactory(threadNameFormat: String) = {
    new ThreadFactory {
      val defaultThreadFactory = Executors.defaultThreadFactory()
      def newThread(r: Runnable) = {
        val t = defaultThreadFactory.newThread(r)
        t.setDaemon(true)
        t.setName(threadNameFormat.replace("%d", t.getId.toString))
        t
      }
    }
  }

  def createExecutorService(threads: Int, threadNameFormat: String): ExecutorService = {
    Executors.newFixedThreadPool(threads, daemonThreadFactory(threadNameFormat))
  }

  def createStrategy(threads: Int, threadNameFormat: String): Strategy = {
    Strategy.Executor(createExecutorService(threads, threadNameFormat))
  }

  def createExecutionContext(threads: Int, threadNameFormat: String): ExecutionContextExecutorService = {
    ExecutionContext.fromExecutorService(createExecutorService(threads, threadNameFormat))
  }

  def toTask[T](future: => Future[T])(implicit ec: ExecutionContext): Task[T] =
    Task.async(
      register =>
        future.onComplete {
          case Success(v)  => register(v.right)
          case Failure(ex) => register(ex.left)
      }
    )

}
