package app.load

import dispatch.{Http, Req, url}
import fs2.{Strategy, Task}

import scala.concurrent.ExecutionContext

class Load(strategy: Strategy, address: String, ec: ExecutionContext) {

  def send(event: String): Task[Unit] = {
    val request: Req = url(s"$address/event") << event
    Task.fromFuture {
      Http(request)(ec)
    }(strategy, ec).map(_ => ())
  }

}
