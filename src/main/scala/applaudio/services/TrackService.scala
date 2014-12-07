package applaudio.services

import applaudio.models.Track


trait TrackService {

  def byArtist(album: String): List[Track]
  def byAlbum(album: String): List[Track]

}
