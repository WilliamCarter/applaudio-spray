package applaudio.routing

import applaudio.services.MetadataService
import applaudio.utilities.FileUpload
import spray.routing._
import spray.httpx.SprayJsonSupport._
import spray.http.StatusCodes._

trait MetadataApi extends HttpService with FileUpload {
  
  def metadataService: MetadataService

  val metadataRoutes: Route = {
    path("metadata") {
      post {
        withMultipartFormData { formData =>
          complete {
            formData.file match {
              case Some(file) => metadataService.metadataForFile(file.data)
              case None => BadRequest
            }
          }
        }
      }
    }
  }

}
