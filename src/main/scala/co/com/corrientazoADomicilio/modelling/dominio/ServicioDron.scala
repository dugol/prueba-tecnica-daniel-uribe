package co.com.corrientazoADomicilio.modelling.dominio

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

//Servicios de Movimiento

sealed trait movimientoDronAlgebra {
  def moverDron(dron: Dron, movimiento: Movimiento): Try[Dron]
}

sealed trait movimientoDron extends movimientoDronAlgebra {
  override def moverDron(dron: Dron, movimiento: Movimiento): Try[Dron] = {
    movimiento match {
      case A() => dron.posicion.orientacion match {
        case N() => {
          if (dron.posicion.coordenada.y > 9) {
            Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
          }
          else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y + 1), N()), dron.capacidad))
        }
        case S() => {
          if (dron.posicion.coordenada.y < (-9)) {
            Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
          }
          else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y - 1), S()), dron.capacidad))
        }
        case O() => {
          if (dron.posicion.coordenada.x < (-9)) {
            Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
          }
          else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x - 1, dron.posicion.coordenada.y), O()), dron.capacidad))
        }
        case E() => {
          if (dron.posicion.coordenada.x > 9) {
            Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
          }
          else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x + 1, dron.posicion.coordenada.y), E()), dron.capacidad))
        }
      }
      case I() => dron.posicion.orientacion match {
        case N() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), O()), dron.capacidad))
        case S() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), E()), dron.capacidad))
        case E() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), N()), dron.capacidad))
        case O() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), S()), dron.capacidad))
      }
      case D() => dron.posicion.orientacion match {
        case N() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), E()), dron.capacidad))
        case S() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), O()), dron.capacidad))
        case E() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), S()), dron.capacidad))
        case O() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), N()), dron.capacidad))
      }
    }
  }
}

private object movimientoDron extends movimientoDron


sealed trait algebraEntrega {
  def realizarEntrega(dron: Dron, pedido: Pedido): Dron
}

sealed trait interpretacionEntrega extends algebraEntrega {
  override def realizarEntrega(dron: Dron, pedido: Pedido): Dron = {
    if (pedido.movimientos.size > 0) {
      realizarEntrega(Dron(dron.id, movimientoDron.moverDron(dron, pedido.movimientos.head).posicion,dron.capacidad), Pedido(pedido.movimientos.tail))
    } else {
      dron
    }

  }
}

private object interpretacionEntrega extends interpretacionEntrega

sealed trait algebraRutaDeEntrega {
  def realizarRuta(dron: Dron, ruta: Ruta): Future[List[Dron]]
}

sealed trait interpretacionRutaEntrega extends algebraRutaDeEntrega {
  implicit val ecParaRutas = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
  override def realizarRuta(dron: Dron, ruta: Ruta): Future[List[Dron]] = {

    Future(ruta.pedidos.scanLeft(dron)((a, b) => interpretacionEntrega.realizarEntrega(a, b)).tail)
  }
}


object interpretacionRutaEntrega extends interpretacionRutaEntrega

sealed trait algebraCorrientazoADomicilio{
  def realizarDomicilios(rutas:List[Ruta]):List[Future[List[Dron]]]
}
