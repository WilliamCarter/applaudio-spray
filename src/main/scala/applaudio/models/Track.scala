package applaudio.models

import spray.json.DefaultJsonProtocol

case class Track (
  id: Option[Long],
  title: String,
  artist: Option[String],
  album: Option[String],
  albumTrack: Option[Int],
  length: Option[Int],
  year: Option[Int],
  encoding: String
)

object Track extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat8(Track.apply)
}
