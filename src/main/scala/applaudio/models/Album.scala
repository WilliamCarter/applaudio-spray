package applaudio.models

import spray.json.DefaultJsonProtocol

case class Album(
  name: String,
  artist: String
)

object Album extends DefaultJsonProtocol {
  implicit val albumFormat = jsonFormat2(Album.apply)
}
