package applaudio.services

import applaudio.models.Track

import scala.concurrent.{Future, ExecutionContext}


trait TrackService {

  def byArtist(artist: String)(implicit ec: ExecutionContext): Future[List[Track]]
  def byAlbum(artist: String, album: String)(implicit ec: ExecutionContext): Future[List[Track]]

}
