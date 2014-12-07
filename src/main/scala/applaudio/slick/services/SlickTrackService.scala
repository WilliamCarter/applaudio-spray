package applaudio.slick.services

import applaudio.models.Track
import applaudio.services.TrackService
import applaudio.slick.tables.SlickTrackTable

class SlickTrackService extends SlickService with TrackService with SlickTrackTable {

  import driver.simple._

  override def byArtist(artist: String): List[Track] = withSession { implicit session: Session =>
    Tracks.byArtist(artist).list.map(Tracks.fromRow(_))
  }

  override def byAlbum(album: String): List[Track] = withSession { implicit session: Session =>
    Tracks.byAlbum(album).list.map(Tracks.fromRow(_))
  }

}
