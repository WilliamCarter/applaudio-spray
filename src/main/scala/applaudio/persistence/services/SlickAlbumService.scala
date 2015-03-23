package applaudio.persistence.services

import applaudio.models.Album
import applaudio.persistence.tables.SlickTrackTable
import applaudio.services.AlbumService

class SlickAlbumService extends SlickService with AlbumService with SlickTrackTable {

  import driver.simple._

  override def byArtist(artist: String) = withSession { implicit session: Session =>
    Tracks.byArtist(artist).map(_.album).filter(_.isDefined).list.distinct.map {
      album => Album(album.get, artist)
    }
  }

}
