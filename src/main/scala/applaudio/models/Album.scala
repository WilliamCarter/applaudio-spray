package applaudio.models

import spray.json.DefaultJsonProtocol

case class Album(
  name: String
)

object Album extends DefaultJsonProtocol {
  implicit val albumFormat = jsonFormat1(Album.apply)
}
