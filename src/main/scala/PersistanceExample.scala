import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import scala.collection.JavaConverters._
import spray.json.DefaultJsonProtocol


//Data model
case class Person (id: String, name: String, age: Int)

//Spray-json for marshalling unmarshalling
object ServiceJsonProtoocol extends DefaultJsonProtocol {
  implicit val persformat = jsonFormat3(Person.apply)
}
//Object to create a restful api to persist and extract person data
object PersistanceExample {
  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("system1")
    implicit val actorMaterializer = ActorMaterializer()
    val buf = scala.collection.mutable.ListBuffer.empty[Person]
    import ServiceJsonProtoocol.persformat

    val route =
      path("person") {
        post {
          entity(as[Person]) {
            pers => complete {
              buf += pers
              s"got persone with name ${pers.name}"
            }
          }
        } ~
          get {
            complete {
              ToResponseMarshallable(buf.toList)
            }
          }
      }

    Http().bindAndHandle(route, "localhost", 9000)

    println("server started at 9000")
  }

}

