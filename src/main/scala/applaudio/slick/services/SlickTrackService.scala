package applaudio.slick.services

import applaudio.services.TrackService
import applaudio.slick.tables.SlickTrackTable

import scala.concurrent.{ExecutionContext, Future}

class SlickTrackService extends SlickService with TrackService with SlickTrackTable {

  import driver.simple._

  override def byArtist(artist: String)(implicit ec: ExecutionContext) = withSession { implicit session: Session =>
    Future {
      Tracks.byArtist(artist).list.map(Tracks.fromRow(_))
    }
  }

  override def byAlbum(album: String)(implicit ec: ExecutionContext) = withSession { implicit session: Session =>
    Future{
      Tracks.byAlbum(album).list.map(Tracks.fromRow(_))
    }
  }

}
