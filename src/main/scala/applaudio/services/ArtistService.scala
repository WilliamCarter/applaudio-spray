package applaudio.services

import applaudio.models.Artist

import scala.concurrent.{ExecutionContext, Future}

trait ArtistService {

  def all(implicit ec: ExecutionContext): Future[List[Artist]]

}
