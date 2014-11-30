package applaudio.models

import spray.json.DefaultJsonProtocol

case class Track (
  title: String,
  artist: String,
  album: String,
  length: Int,
  year: Int,
  trackIndex: Int,
  `type`: String,
  quality: String,
  size: Int,
  filename: String
)

object Track extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat10(Track.apply)
}
