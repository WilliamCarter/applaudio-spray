package applaudio.services

import applaudio.models.Album

import scala.concurrent.{Future, ExecutionContext}

trait AlbumService {

  def byArtist(artist: String)(implicit ec: ExecutionContext): Future[List[Album]]

}
