package example

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.PathMatchers.IntNumber
import akka.util.Timeout
import akka.pattern.ask
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext
import scala.xml.NodeSeq

trait OrderService {
  val processOrders: ActorRef

  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout

  val routes = getOrder ~ postOrder

  def getOrder = get {
    pathPrefix("orders" / IntNumber) { id =>
      onSuccess(processOrders.ask(OrderId(id))) {
        case result: TrackingOrder =>
          complete(
            <statusResponse>
              <id>{result.id}</id>
              <status>{result.status}</status>
            </statusResponse>
          )
        case result: NoSuchOrder =>
          complete(StatusCodes.NotFound)
      }
    }
  }

  def postOrder = post {
    path("orders") {
      entity(as[NodeSeq]) { xml =>
        val order = toOrder(xml)
        onSuccess(processOrders.ask(order)) {
          case result: TrackingOrder =>
            complete(
              <confirm>
                <id>{result.id}</id>
                <status>{result.status}</status>
              </confirm>
            )
          case result =>
            complete(StatusCodes.BadRequest)
        }
      }
    }
  }

  def toOrder(xml: NodeSeq): Order = {
    val order = xml \\ "order"
    val customer = (order \ "customerId").text
    val productId = (order \ "productId").text
    val number = (order \ "number").text.toInt
    new Order(customer, productId, number)
  }

}

class OrderServiceApi(system: ActorSystem, timeout: Timeout, val processOrders: ActorRef) extends OrderService {
  override implicit val executionContext: ExecutionContext = system.dispatcher
  override implicit val requestTimeout: Timeout = timeout
}