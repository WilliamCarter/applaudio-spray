package applaudio.slick.services

import applaudio.models.Track
import applaudio.services.TrackService
import applaudio.slick.tables.SlickTrackTable

import scala.concurrent.Future

class SlickTrackService extends SlickService with TrackService with SlickTrackTable {

  import driver.simple._

  override def byArtist(artist: String) = withSession { implicit session: Session =>
    Tracks.byArtist(artist).list.map(fromRow _)
  }

  override def byAlbum(artist: String, album: String) = withSession { implicit session: Session =>
    Tracks.byAlbum(artist, album).list.map(fromRow _)
  }

  override def add(track: Track): Future[Option[Long]] = withSession { implicit session: Session =>
    //Tracks += track
    None
  }
}
