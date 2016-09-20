package app

import app.extract.Extract
import app.infrastructure.concurrency
import app.load.Load
import app.transform.Transform
import io.circe.generic.auto._
import io.circe.syntax._
import app.model.Codecs._

import scala.concurrent.duration._
import scalaz.concurrent.{Strategy, Task}
import scalaz.stream.Process
import scalaz.stream.process1.unchunk
import scalaz.stream.time.awakeEvery

object Main {

  def main(args: Array[String]): Unit = {

    val ec          = concurrency.createExecutionContext(10, "jet-stream-%d")
    val strategy    = Strategy.Executor(ec)
    val extractor   = Extract.createExtractor()
    val loader      = new Load(ec).sendToEvents
    val transformer = Transform.createEvents _

    val events: Process[Task, String] =
      awakeEvery(1.minute)(strategy, Strategy.DefaultTimeoutScheduler)
        .flatMap(_ => extractor)
        .map(transformer)
        .pipe(unchunk)
        .map(_.asJson.noSpaces)
        .observe(loader)

    events.run.run

  }

}
