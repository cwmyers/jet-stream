package app.extract

import java.io.File

import dispatch.Defaults._
import dispatch._
import fs2.{Strategy, Stream, Task}
import org.apache.commons.io.FileUtils

//curl 'https://data-live.flightradar24.com/zones/fcgi/feed.js?bounds=-37.42,-37.99,144.56,145.10&faa=1&mlat=1&flarm=1&adsb=1&gnd=1&air=1&vehicles=1&estimated=1&maxage=7200&gliders=1&stats=1' -H 'origin: https://www.flightradar24.com' -H 'accept-encoding: gzip, deflate, sdch, br' -H 'accept-language: en-US,en;q=0.8' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36' -H 'accept: application/json, text/javascript, */*; q=0.01' -H 'referer: https://www.flightradar24.com/-37.7,144.8/12' -H 'authority: data-live.flightradar24.com' --compressed

object Extract {
  def createExtractorFromFlightRadar24(implicit strategy: Strategy): Stream[Task, String] = {
    val req = url(
      "https://data-live.flightradar24.com/zones/fcgi/feed.js?bounds=-37.42,-37.99,144.56,145.10&faa=1&mlat=1&flarm=1&adsb=1&gnd=1&air=1&vehicles=1&estimated=1&maxage=7200&gliders=1&stats=1"
    )
    Stream.eval(Task.fromFuture(Http(req)).map(_.getResponseBody))
  }

  def createExtractorFromDump1090(file: String): Stream[Task, String] = {
    Stream.eval(Task.delay {
      FileUtils.readFileToString(new File(file))
    })
  }
}
