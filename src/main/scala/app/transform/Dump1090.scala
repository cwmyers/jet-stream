package app.transform

import java.time.{Instant, OffsetDateTime, ZoneId}

import app.model.Event
import cats.implicits._
import io.circe.Decoder.Result
import io.circe.Json
import io.circe.generic.auto._
import io.circe.parser._

object Dump1090 {

  case class Payload(aircraft: Vector[Json], now: Long)

  case class Aircraft(hex: String)

  def createEventsFromDump1090(payload: String): Vector[Event] = {

    val result = for {
      jsonPayload <- decode[Payload](payload)
      events <- jsonPayload.aircraft.traverseU(jsonToEvent(_, jsonPayload.now))
    } yield events

    result.getOrElse(Vector.empty)
  }

  def jsonToEvent(json: Json, now: Long): Result[Event] = {
    json
      .as[Aircraft]
      .map(a => Event(a.hex, "dump1090", OffsetDateTime.ofInstant(Instant.ofEpochSecond(now), ZoneId.of("Z")), json))
  }
}
