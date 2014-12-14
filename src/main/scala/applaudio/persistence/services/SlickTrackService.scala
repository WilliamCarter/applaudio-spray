package applaudio.persistence.services

import applaudio.models.Track
import applaudio.services.TrackService
import applaudio.persistence.tables.SlickTrackTable
import scala.concurrent.Future
import scalaz.{\/-, -\/, \/}

class SlickTrackService extends SlickService with TrackService with SlickTrackTable {

  import driver.simple._

  override def byArtist(artist: String) = withSession { implicit session: Session =>
    Tracks.byArtist(artist).list.map(fromRow _)
  }

  override def byAlbum(artist: String, album: String) = withSession { implicit session: Session =>
    Tracks.byAlbum(artist, album).list.map(fromRow _)
  }

  override def add(track: Track): Future[String \/ Long] = withSession { implicit session: Session =>
    (Tracks returning Tracks.map(_.id)) += Track.unapply(track).get match {
      case Some(id) => \/-(id)
      case None => -\/("Inserted track not given an ID. What!?")
    }
  }
}
