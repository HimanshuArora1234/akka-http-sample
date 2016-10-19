import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
/**
 * Object to create hello world rest ful api.
 * Run this object and goto http://localhost:8080/ to test this API.
 */
object HelloWorld {
  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("system")
    implicit val actorMaterializer = ActorMaterializer()


    val route =
      pathSingleSlash {
        get {
          complete {
            "Hello world"
          }
        }
      }
    Http().bindAndHandle(route,"localhost",8080)

    println("server started at 8080")
  }
}
