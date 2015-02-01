package applaudio.persistence.services

import applaudio.models.Track
import applaudio.persistence.tables.SlickTrackTable
import applaudio.services.TrackService

import scala.concurrent.Future

class SlickTrackService extends SlickService with TrackService with SlickTrackTable {

  import driver.simple._

  override def byArtist(artist: String) = withSession { implicit session: Session =>
    Tracks.byArtist(artist).list.map(fromRow _)
  }

  override def byAlbum(artist: String, album: String) = withSession { implicit session: Session =>
    Tracks.byAlbum(artist, album).list.map(fromRow _)
  }

  override def add(track: Track): Future[Long] = withSession { implicit session: Session =>
    (Tracks returning Tracks.map(_.id)) += Track.unapply(track).get
  }

}
