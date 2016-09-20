package app.transform

import java.time.{Instant, OffsetDateTime, ZoneId}

import app.model._
import cats.data.Xor
import cats.implicits._
import io.circe.optics.JsonPath._
import io.circe.parser._
import io.circe.{Json, ParsingFailure}

object Transform {
  def createEvents(payload: String): Vector[Event] = {
    val maybePlanes: Xor[ParsingFailure, Vector[Event]] = for {
      payload <- parse(payload)
      a <- payload.asObject.toRightXor(ParsingFailure("", new RuntimeException("blah")))
      b = a.toMap.filterKeys(k => !Vector("stats", "full_count", "version").contains(k)).toVector.map(_._2)
      planes = b.traverse(createPlane).orEmpty
    } yield planes

    maybePlanes.getOrElse(Vector.empty)
  }

  def createPlane(json: Json): Option[Event] = {
    for {
      modeSCode <- root.index(0).string.getOption(json)
      lat <- root.index(1).double.getOption(json)
      lon <- root.index(2).double.getOption(json)
      track <- root.index(3).double.getOption(json)
      altitude <- root.index(4).long.getOption(json)
      velocity <- root.index(5).long.getOption(json)
      squawk <- root.index(6).string.getOption(json)
      radar <- root.index(7).string.getOption(json)
      aircraft <- root.index(8).string.getOption(json)
      registration <- root.index(9).string.getOption(json)
      timestamp <- root.index(10).long.getOption(json)
      origin <- root.index(11).string.getOption(json)
      destination <- root.index(12).string.getOption(json)
      flightNumber <- root.index(13).string.getOption(json)
      altFlightNumber <- root.index(16).string.getOption(json)
    } yield
      Event(
        registration,
        "planes",
        OffsetDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()),
        Plane(
          Position(lat, lon, altitude, velocity),
          Aircraft(aircraft, registration, modeSCode),
          Flight(flightNumber, altFlightNumber, origin, destination)
        )
      )

  }
}
