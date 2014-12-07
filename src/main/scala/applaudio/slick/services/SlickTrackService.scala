package applaudio.slick.services

import applaudio.models.Track
import applaudio.services.TrackService
import applaudio.slick.tables.SlickTrackTable

class SlickTrackService extends SlickService with TrackService {

  import driver.simple._

  val trackTable = new SlickTrackTable(driver)

  override def byArtist(artist: String): List[Track] = withSession { implicit session: Session =>
    trackTable.Tracks.byArtist(artist).list.map(trackTable.Tracks.fromRow(_))
  }

  override def byAlbum(album: String): List[Track] = withSession { implicit session: Session =>
    trackTable.Tracks.byAlbum(album).list.map(trackTable.Tracks.fromRow(_))
  }

}
