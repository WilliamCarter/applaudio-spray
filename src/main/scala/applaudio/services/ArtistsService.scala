package applaudio.services

import applaudio.models.Artist

trait ArtistsService {

  def all: List[Artist]

}
