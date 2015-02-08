package applaudio.services

import applaudio.models.Track

import scala.concurrent.Future


trait TrackService {

  def byArtist(artist: String): Future[List[Track]]
  def byAlbum(artist: String, album: String): Future[List[Track]]

  def add(track: Track): Future[Long]
  def delete(id: Long)

}
