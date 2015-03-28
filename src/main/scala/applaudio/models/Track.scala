package applaudio.models

import spray.json._

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

  def apply(title: String, artist: Option[String], album: Option[String], albumTrack: Option[Int], length: Option[Int],
      year: Option[Int], encoding: String): Track =
    apply(None, title, artist, album, albumTrack, length, year, encoding)

  implicit object TrackJsonFormat extends RootJsonFormat[Track] {

    def write(track: Track) = JsObject (
      ("id", track.id.map(JsNumber(_)).getOrElse(JsNull)),
      ("title", JsString(track.title)),
      ("artist", jsStringOrNull(track.artist)),
      ("album", jsStringOrNull(track.album)),
      ("albumTrack", jsNumberOrNull(track.albumTrack)),
      ("length", jsNumberOrNull(track.length)),
      ("year", jsNumberOrNull(track.year)),
      ("encoding", JsString(track.encoding)),
      ("url", jsStringOrNull(track.id.map { id => s"/library/$id.${track.encoding}" })),
      ("downloadUrl", track.id.map { id => JsString(s"/library/downloads/$id.${track.encoding}") }.getOrElse(JsNull))
    )

    def read(value: JsValue) = {
      val id = fromField[Option[Long]](value, "id")
      val title = fromField[String](value, "title")
      val artist = fromField[Option[String]](value, "artist")
      val album = fromField[Option[String]](value, "album")
      val albumTrack = fromField[Option[Int]](value, "albumTrack")
      val length = fromField[Option[Int]](value, "length")
      val year = fromField[Option[Int]](value, "year")
      val encoding = fromField[String](value, "encoding")
      Track(id, title, artist, album, albumTrack, length, year, encoding)
    }

    private def jsStringOrNull(maybeString: Option[String]):JsValue = maybeString.map(JsString.apply).getOrElse(JsNull)
    private def jsNumberOrNull(maybeNumber: Option[Int]):JsValue = maybeNumber.map(JsNumber.apply).getOrElse(JsNull)
  }

}
