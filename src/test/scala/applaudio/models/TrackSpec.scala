package applaudio.models

import org.specs2.mutable.Specification
import org.specs2.matcher._
import spray.json._

class TrackSpec extends Specification with JsonMatchers {

  val jamesAndTheColdGun = Track(
    id = Option(38L),
    title = "James and the Cold Gun",
    artist = Option("Kate Bush"),
    album = Option("The Kick Inside"),
    albumTrack = Option(7),
    length = Option((3 * 60) + 37),
    year = Option(1978),
    encoding = "mp3"
  )

  val jamesAndTheColdGunJson =
    """
      |{
      |    "id": 38,
      |    "title": "James and the Cold Gun",
      |    "artist": "Kate Bush",
      |    "album": "The Kick Inside",
      |    "albumTrack": 7,
      |    "length": 217,
      |    "year": 1978,
      |    "encoding": "mp3"
      |}
    """.stripMargin.parseJson

  "The Artist Object" can {
     "serialise to JSON" in {
       jamesAndTheColdGun.toJson must be equalTo (jamesAndTheColdGunJson)
     }
   }

 }
