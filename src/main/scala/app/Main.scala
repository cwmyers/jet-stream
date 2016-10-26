package app

import app.extract.Extract
import app.infrastructure.concurrency
import app.load.Load
import app.model.Event
import app.transform.{Dump1090, Transform}
import fs2.{Scheduler, Strategy, Stream, Task}

object Main {

  def main(args: Array[String]): Unit = {

    val eventStreamUrl     = sys.env.getOrElse("EVENT_STREAM_URL", "http://192.168.1.198:9090")
    val source             = sys.env.getOrElse("DATA_SOURCE", "dump1090")
    val interval           = sys.env.getOrElse("POST_INTERVAL", "30").toInt
    val ec                 = concurrency.createExecutionContext(10, "jetstream")
    val ecLoader           = concurrency.createExecutionContext(10, "eventstream")
    implicit val strategy  = Strategy.fromExecutionContext(ec)
    implicit val scheduler = Scheduler.fromFixedDaemonPool(1)
    val extractor          = getExtractor(source, strategy)
    val loader             = new Load(strategy, eventStreamUrl, ecLoader).send _
    val transformer        = getTransformer(source)

    JetStream.create(interval, extractor, transformer, loader)(strategy, scheduler).run.unsafeRun()

  }

  def getExtractor(source: String, strategy: Strategy): Stream[Task, String] = source match {
    case "dump1090" =>
      val file = sys.env.getOrElse("DUMP1090_FILE", "aircraft.json")
      Extract.createExtractorFromDump1090(file)
    case "fr24" => Extract.createExtractorFromFlightRadar24(strategy)
    case _      => throw new RuntimeException(s"Unknown source: $source")
  }

  def getTransformer(source: String): String => Vector[Event] = source match {
    case "dump1090" => Dump1090.createEventsFromDump1090
    case "fr24"     => Transform.createEventsFromFlightRadar
  }

}
