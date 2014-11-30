package applaudio.models

import org.specs2.mutable.Specification
import org.specs2.matcher._
import spray.json._

class TrackSpec extends Specification with JsonMatchers {

  val jamesAndTheColdGun = Track(
    title = "James and the Cold Gun",
    artist = "Kate Bush",
    album = "The Kick Inside",
    length = (3 * 60) + 37,
    year = 1978,
    trackIndex = 7,
    `type` = "mp3",
    quality = "128kb/s",
    size = 4378921,
    filename = "adfa.mp3"
  )

  val jamesAndTheColdGunJson =
    """
      |{
      |    "title": "James and the Cold Gun",
      |    "artist": "Kate Bush",
      |    "album": "The Kick Inside",
      |    "length": 217,
      |    "year": 1978,
      |    "trackIndex": 7,
      |    "type": "mp3",
      |    "quality": "128kb/s",
      |    "size": 4378921,
      |    "filename": "adfa.mp3"
      |}
    """.stripMargin.parseJson

  "The Artist Object" can {
     "serialise to JSON" in {
       jamesAndTheColdGun.toJson must be equalTo (jamesAndTheColdGunJson)
     }
   }

 }
