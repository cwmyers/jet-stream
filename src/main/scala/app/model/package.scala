package app

import java.time.OffsetDateTime

package object model {

  case class Position(lat: Double, lon: Double, altitude: Long, velocity: Long)

  case class Plane(position: Position, aircraft: Aircraft, flight: Flight)

  case class Aircraft(aircraft: String, registration: String, modeSCode: String)

  case class Flight(flightNumber: String, altFlightNumber: String, origin: String, destination: String)

  case class Communication(modeSCode: String, squawk: String)

  case class Event(entityId: String, systemName: String, timestamp: OffsetDateTime, body: Plane)

}
