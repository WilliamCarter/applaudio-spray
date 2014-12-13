package applaudio.services

import applaudio.models.Artist

import scala.concurrent.{ExecutionContext, Future}

trait ArtistService {

  def all: Future[List[Artist]]

}
