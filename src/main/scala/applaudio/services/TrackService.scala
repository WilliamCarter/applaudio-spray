package applaudio.services

import applaudio.models.Track

import scala.concurrent.{Future, ExecutionContext}


trait TrackService {

  def byArtist(album: String)(implicit ec: ExecutionContext): Future[List[Track]]
  def byAlbum(album: String)(implicit ec: ExecutionContext): Future[List[Track]]

}
