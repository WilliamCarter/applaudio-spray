package applaudio.fakeservices

import applaudio.models.Artist
import applaudio.services.ArtistService

import scala.concurrent.{Future, ExecutionContext}

class FakeArtistsService extends ArtistService {

  def all(implicit ec: ExecutionContext) = Future {
    List(Artist("Bob Dylan"), Artist("The Black Keys"))
  }

}
