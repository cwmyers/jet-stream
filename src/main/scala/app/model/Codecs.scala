package app.model

import java.time.OffsetDateTime

import io.circe.{Encoder, Json}

object Codecs {
  implicit def DateEncoder: Encoder[OffsetDateTime] =
    Encoder.instance(d => Json.fromString(d.toString))
}
