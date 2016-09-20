package app.load

import app.infrastructure.concurrency
import dispatch.{Http, Req, url}

import scala.concurrent.ExecutionContext
import scalaz.concurrent.Task
import scalaz.stream.{Sink, sink}

class Load(ec: ExecutionContext) {

  def sendToEvents: Sink[Task, String] = {
    sink.lift(send)
  }

  def send(event: String): Task[Unit] = {
    val request: Req = url("http://192.168.1.198:9090/event") << event
    concurrency.toTask(Http(request)(ec))(ec).map(_ => ())
  }
}
