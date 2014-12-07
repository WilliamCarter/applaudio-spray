package applaudio.slick.services

import applaudio.models.Artist
import applaudio.services.ArtistService
import applaudio.slick.tables.SlickTrackTable

class SlickArtistService extends SlickService with ArtistService {

  import driver.simple._

  val trackTable = new SlickTrackTable(driver)

  override def all: List[Artist] = withSession { implicit session: Session =>
    trackTable.Tracks.map(_.artist).filter(_.isDefined).list.distinct.map {
      track => Artist(track.get)
    }
  }

}
