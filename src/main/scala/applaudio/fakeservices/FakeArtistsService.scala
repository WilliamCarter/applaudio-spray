package applaudio.fakeservices

import applaudio.models.Artist
import applaudio.services.ArtistService

class FakeArtistsService extends ArtistService {

  def all = List(Artist("Bob Dylan"), Artist("The Black Keys"))

}
