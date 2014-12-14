package applaudio.services

import applaudio.models.Track

import scala.concurrent.Future
import scalaz.\/


trait TrackService {

  def byArtist(artist: String): Future[List[Track]]
  def byAlbum(artist: String, album: String): Future[List[Track]]

  def add(track: Track): Future[\/[String, Long]]

}
