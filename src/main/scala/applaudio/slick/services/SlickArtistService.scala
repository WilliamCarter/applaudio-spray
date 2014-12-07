package applaudio.slick.services

import applaudio.models.Artist
import applaudio.services.ArtistService
import applaudio.slick.tables.TrackTable

import scala.slick.driver.MySQLDriver.simple._

class SlickArtistService extends ArtistService {

  val tracks = TableQuery[TrackTable]

  override def all: List[Artist] = Database.forConfig("db") withSession { implicit session: Session =>
    tracks.map(_.artist).filter(_.isDefined).list.distinct.map {
      track => Artist(track.get)
    }
  }

}
