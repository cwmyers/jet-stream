package app.transform

import java.time.OffsetDateTime

import app.model.Event
import io.circe.{Json, parser}
import org.specs2.mutable.Specification

class Dump1090Spec extends Specification {

  "Dump1090Spec" should {
    "createEventsFromDump1090" in {
      val input =
        """
          |{
          |  "now": 1476441346,
          |  "messages": 4492,
          |  "aircraft": [
          |    {
          |      "hex": "7c6b39",
          |      "squawk": "1320",
          |      "lat": -36.451194,
          |      "lon": 145.887451,
          |      "nucp": 7,
          |      "seen_pos": 21.7,
          |      "altitude": 29450,
          |      "vert_rate": 1216,
          |      "track": 39,
          |      "speed": 484,
          |      "mlat": [],
          |      "tisb": [],
          |      "messages": 21,
          |      "seen": 21,
          |      "rssi": -33.1
          |    },
          |    {
          |      "hex": "7c6d7b",
          |      "squawk": "1203",
          |      "flight": "JST445  ",
          |      "lat": -36.281231,
          |      "lon": 145.191397,
          |      "nucp": 7,
          |      "seen_pos": 21.1,
          |      "altitude": 33975,
          |      "vert_rate": -128,
          |      "track": 220,
          |      "speed": 415,
          |      "category": "A0",
          |      "mlat": [],
          |      "tisb": [],
          |      "messages": 119,
          |      "seen": 19.1,
          |      "rssi": -32.7
          |    }
          |   ]
          | }
        """.stripMargin

      Dump1090.createEventsFromDump1090(input) === Vector(Event("7c6b39", "dump1090",
        OffsetDateTime.parse("2016-10-14T10:35:46Z"),
        parser.parse(
          """{
        "hex" : "7c6b39",
        "squawk" : "1320",
        "lat" : -36.451194,
        "lon" : 145.887451,
        "nucp" : 7,
        "seen_pos" : 21.7,
        "altitude" : 29450,
        "vert_rate" : 1216,
        "track" : 39,
        "speed" : 484,
        "mlat" : [
        ],
        "tisb" : [
        ],
        "messages" : 21,
        "seen" : 21,
        "rssi" : -33.1
      }""").getOrElse(Json.Null)), Event("7c6d7b", "dump1090", OffsetDateTime.parse("2016-10-14T10:35:46Z"), parser.parse(
        """{
        "hex" : "7c6d7b",
        "squawk" : "1203",
        "flight" : "JST445  ",
        "lat" : -36.281231,
        "lon" : 145.191397,
        "nucp" : 7,
        "seen_pos" : 21.1,
        "altitude" : 33975,
        "vert_rate" : -128,
        "track" : 220,
        "speed" : 415,
        "category" : "A0",
        "mlat" : [
        ],
        "tisb" : [
        ],
        "messages" : 119,
        "seen" : 19.1,
        "rssi" : -32.7
      }""").getOrElse(Json.Null)))
    }

  }
}
