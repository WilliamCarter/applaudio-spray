package applaudio.slick.services

import applaudio.models.Artist
import applaudio.services.ArtistService
import applaudio.slick.tables.SlickTrackTable

import scala.concurrent.{ExecutionContext, Future}

class SlickArtistService extends SlickService with ArtistService with SlickTrackTable {

  import driver.simple._

  override def all: Future[List[Artist]] = withSession { implicit session: Session =>
    Tracks.map(_.artist).filter(_.isDefined).list.distinct.map {
      track => Artist(track.get)
    }
  }

}
