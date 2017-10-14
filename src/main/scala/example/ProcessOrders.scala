package example

import akka.actor.Actor

import scala.collection.mutable.HashMap

class ProcessOrders extends Actor {
  val orderList: HashMap[Long, TrackingOrder] = new HashMap[Long, TrackingOrder]
  var lastOrderId = 0L

  override def receive = {
    case order: Order =>
      lastOrderId += 1
      val newOrder = new TrackingOrder(lastOrderId, "received", order)
      orderList += lastOrderId -> newOrder
      sender ! newOrder
    case order: OrderId =>
      orderList.get(order.id) match {
        case Some(intOrder) => sender ! intOrder.copy(status = "processing")
        case None => sender ! NoSuchOrder(order.id)
      }
    case "reset" =>
      lastOrderId = 0
      orderList.clear()
  }
}

case class Order(customerId: String, productId: String, number: Int)

case class TrackingOrder(id: Long, status: String, order: Order)

case class OrderId(id: Long)

case class NoSuchOrder(id: Long)
