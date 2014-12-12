package applaudio.slick.services

import applaudio.models.Album
import applaudio.services.AlbumService
import applaudio.slick.tables.SlickTrackTable

import scala.concurrent.{Future, ExecutionContext}

class SlickAlbumService extends SlickService with AlbumService with SlickTrackTable {

  import driver.simple._

  override def byArtist(artist: String)(implicit ec: ExecutionContext) = withSession { implicit session: Session =>
    Future {
      Tracks.byArtist(artist).map(_.album).filter(_.isDefined).list.distinct.map {
        album => Album(album.get)
      }
    }
  }

}
