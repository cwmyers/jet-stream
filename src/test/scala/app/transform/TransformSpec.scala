package app.transform

import java.time.OffsetDateTime.parse

import app.model._
import org.specs2.mutable.Specification
import Codecs._
import io.circe.generic.auto._
import io.circe.syntax._

class TransformSpec extends Specification {

  val testPayload =
    """
      |{"full_count":11316,"version":4
      |,"b166941":["7C6DBB",-37.5882,144.6672,342,7125,273,"1353","F-YMEN2","B738","VH-VYL",1474583552,"MEL","BNE","QF608",0,2432,"QFA608",0]
      |,"b164829":["7C6B0C",-37.7202,144.7008,242,5650,277,"3256","F-YMEN2","A320","VH-VFI",1474583552,"SYD","AVV","JQ603",0,-1280,"JST603",0]
      |,"b151e8e":["7C531C",-37.4995,144.8063,171,3475,201,"5353","T-YMAV1","A333","VH-QPA",1474583552,"HKG","MEL","QF30",0,-1152,"QFA30",0]
      |,"stats":{"total":{"ads-b":6626,"mlat":2584,"faa":1658,"flarm":5,"estimated":327},"visible":{"ads-b":14,"mlat":0,"faa":0,"flarm":0,"estimated":0}}}
    """.stripMargin

  "TransformSpec" should {
    "createEvents" in {
      Transform.createEventsFromFlightRadar(testPayload) must containTheSameElementsAs(
        Vector(
          Event("VH-VYL", "planes", parse("2016-09-23T08:32:32+10:00"),
            Plane(Position(-37.5882, 144.6672, 7125, 273, 342.0, parse("2016-09-23T08:32:32+10:00")),
              Aircraft("B738", "VH-VYL", "7C6DBB"), Flight("QF608", "QFA608", "MEL", "BNE"),
              Communication("7C6DBB", "1353")).asJson),
          Event("VH-VFI", "planes", parse("2016-09-23T08:32:32+10:00"),
            Plane(Position(-37.7202, 144.7008, 5650, 277, 242.0, parse("2016-09-23T08:32:32+10:00")),
              Aircraft("A320", "VH-VFI", "7C6B0C"),
              Flight("JQ603", "JST603", "SYD", "AVV"),
              Communication("7C6B0C", "3256")).asJson),
          Event("VH-QPA", "planes", parse("2016-09-23T08:32:32+10:00"),
            Plane(Position(-37.4995, 144.8063, 3475, 201, 171.0, parse("2016-09-23T08:32:32+10:00")),
              Aircraft("A333", "VH-QPA", "7C531C"),
              Flight("QF30", "QFA30", "HKG", "MEL"),
              Communication("7C531C", "5353")).asJson)
        ))

    }

  }
}
