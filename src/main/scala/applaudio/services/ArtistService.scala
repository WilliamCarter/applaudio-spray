package applaudio.services

import applaudio.models.Artist

trait ArtistService {

  def all: List[Artist]

}
