package app

import app.infrastructure.concurrency.lift
import app.model.Codecs._
import app.model.Event
import fs2.Task._
import fs2.{Scheduler, Strategy, Stream, Task, time}
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.duration._

object JetStream {

  def create(extractor: Stream[Task, String],
             transformer: String => Vector[Event],
             loader: String => Task[Unit])(implicit strategy: Strategy, scheduler: Scheduler): Stream[Task, Unit] = {
    time
      .awakeEvery[Task](30.seconds)
      .flatMap(_ => extractor)
      .map(transformer)
      .flatMap(Stream.emits)
      .map(_.asJson.noSpaces)
      .flatMap(lift(loader))
  }

}
