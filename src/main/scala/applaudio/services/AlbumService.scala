package applaudio.services

import applaudio.models.Album

trait AlbumService {

  def byArtist(artist: String): List[Album]

}
