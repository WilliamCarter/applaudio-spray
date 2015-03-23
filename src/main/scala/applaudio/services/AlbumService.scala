package applaudio.services

import applaudio.models.Album

import scala.concurrent.Future

trait AlbumService {
  def byArtist(artist: String): Future[List[Album]]
}
