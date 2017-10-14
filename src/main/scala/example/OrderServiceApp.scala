package example

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future

object OrderServiceApp extends App with RequestTimeout {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val processOrders = system.actorOf(
    Props(new ProcessOrders), "process-orders"
  )

  val x = configuredRequestTimeout(config)

  val api = new OrderServiceApi(system, configuredRequestTimeout(config), processOrders).routes
  implicit val materializer = ActorMaterializer()
  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(api, host, port)
  val log = Logging(system.eventStream, "order-service")
  bindingFuture.map { serverBinding =>
    log.info(s"Bound to ${serverBinding.localAddress}")
  }.onFailure {
    case ex:Exception =>
      log.error(ex, s"Failed to bind to $host:$port!")
      system.terminate()
  }

}
