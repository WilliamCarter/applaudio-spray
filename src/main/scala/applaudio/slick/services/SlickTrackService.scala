package applaudio.slick.services

import applaudio.models.Track
import applaudio.services.TrackService
import applaudio.slick.tables.{Tracks, TrackTable}

import scala.slick.driver.MySQLDriver.simple._

class SlickTrackService extends TrackService {

  val tracks = TableQuery[TrackTable]

  override def byArtist(artist: String): List[Track] = Database.forConfig("db") withSession { implicit session: Session =>
    Tracks.byArtist(artist).list.map(Tracks.fromRow(_))
  }

  override def byAlbum(album: String): List[Track] = Database.forConfig("db") withSession { implicit session: Session =>
    Tracks.byAlbum(album).list.map(Tracks.fromRow(_))
  }

}
