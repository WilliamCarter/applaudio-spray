package applaudio.fakeservices

import applaudio.models.Artist
import applaudio.services.ArtistsService

class FakeArtistsService extends ArtistsService {

  def all = List(Artist("Bob Dylan"), Artist("The Black Keys"))

}
