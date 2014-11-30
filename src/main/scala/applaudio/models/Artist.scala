package applaudio.models

import spray.json._

case class Artist(name: String)

object Artist extends DefaultJsonProtocol {
  implicit val artistFormat = jsonFormat1(Artist.apply)
}
