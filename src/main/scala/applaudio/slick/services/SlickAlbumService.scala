package applaudio.slick.services

import applaudio.models.Album
import applaudio.services.AlbumService
import applaudio.slick.tables.{TrackTable, Tracks}

import scala.slick.driver.MySQLDriver.simple._

class SlickAlbumService extends AlbumService {

  val tracks = TableQuery[TrackTable]

  override def byArtist(artist: String): List[Album] = Database.forConfig("db") withSession { implicit session: Session =>
    Tracks.byArtist(artist).map(_.album).filter(_.isDefined).list.distinct.map {
      album => Album(album.get)
    }
  }

}
