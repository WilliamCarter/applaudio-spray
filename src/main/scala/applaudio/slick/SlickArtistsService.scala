package applaudio.slick

import applaudio.models.{Track, Artist}
import applaudio.services.ArtistsService
import applaudio.slick.tables.TrackTable

import scala.slick.driver.MySQLDriver.simple._

class SlickArtistsService extends ArtistsService {

  val tracks = TableQuery[TrackTable]

  override def all: List[Artist] = Database.forURL(url="jdbc:mysql://localhost/Applaudio", driver = "com.mysql.jdbc.Driver" , user="root", password="") withSession { implicit session: Session =>
    tracks.map(_.artist).filter(_.isDefined).list.distinct.map {
      track => Artist(track.get)
    }
  }

}
