package applaudio.models

import spray.json.DefaultJsonProtocol

case class Track (
  id: Option[Long],
  title: String,
  artist: String,
  album: String,
  albumTrack: Int,
  length: Int,
  year: Int,
  encoding: String
)

object Track extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat8(Track.apply)
}
