package applaudio.models

import org.specs2.matcher._
import org.specs2.mutable.Specification
import spray.json._

class TrackSpec extends Specification with JsonMatchers {

  "The Artist Object" can {
    "serialise to JSON" in {
      jamesAndTheColdGun(id = Some(38L)).toJson must be equalTo jamesAndTheColdGunJson(id = "38")
    }
    "serialise from JSON with null id" in {
      val track = Track.TrackJsonFormat.read(jamesAndTheColdGunJson(id = "null"))
      track must be equalTo jamesAndTheColdGun(id = None)
    }
   }


  def jamesAndTheColdGun(id: Option[Long]) = Track(
    id = id,
    title = "James and the Cold Gun",
    artist = Option("Kate Bush"),
    album = Option("The Kick Inside"),
    albumTrack = Option(7),
    length = Option((3 * 60) + 37),
    year = Option(1978),
    encoding = "mp3"
  )

  def jamesAndTheColdGunJson(id: String) =
    s"""
      |{
      |    "id": $id,
      |    "title": "James and the Cold Gun",
      |    "artist": "Kate Bush",
      |    "album": "The Kick Inside",
      |    "albumTrack": 7,
      |    "length": 217,
      |    "year": 1978,
      |    "encoding": "mp3",
      |    "location": "/library/$id.mp3",
      |    "downloadUrl": "/library/downloads/$id.mp3"
      |}
    """.stripMargin.parseJson

}
