package co.com.corrientazoADomicilio.modelling.dominio.services

import java.util.concurrent.Executors

import co.com.corrientazoADomicilio.modelling.dominio.entities._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


sealed trait ServicioDronAlg {
  def moverDron(dron: Dron, movimiento: Movimiento): Try[Dron]

  protected def moverAdelante(dron: Dron): Try[Dron]

  protected def girar90Derecha(dron: Dron): Try[Dron]

  protected def girar90Izquierda(dron: Dron): Try[Dron]

  //Validar que la coordenada y no sea mayor que 9 y venga un movimiento adelante
  protected def validarMovimientoNorte(dron: Dron): Try[Dron]

  //Validar que la coordenada y no sea menor que -9 y el siguiente movimiento sea adelante
  protected def validarMovimientoSur(dron: Dron): Try[Dron]

  //Validar que la coordenada x no sea menor que -9 y el seguiente movimiento sea adelante
  protected def validarMovimientoOeste(dron: Dron): Try[Dron]

  //Validar que la coordenada x no sea mayor que 9 y el siguiente movimiento sea adelante.
  protected def validarMovimientoEste(dron: Dron): Try[Dron]
}

sealed trait ServicioDron extends ServicioDronAlg {
  override def moverDron(dron: Dron, movimiento: Movimiento): Try[Dron] = {
    movimiento match {
      case A() => moverAdelante(dron)
      case I() => girar90Izquierda(dron)
      case D() => girar90Derecha(dron)
    }
  }

  protected override def moverAdelante(dron: Dron): Try[Dron] = {
    dron.posicion.orientacion match {
      case N() => validarMovimientoNorte(dron)
      case S() => validarMovimientoSur(dron)
      case O() => validarMovimientoOeste(dron)
      case E() => validarMovimientoEste(dron)
    }
  }

  protected override def girar90Izquierda(dron: Dron): Try[Dron] = {
    dron.posicion.orientacion match {
      case N() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), O()), dron.capacidad))
      case S() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), E()), dron.capacidad))
      case E() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), N()), dron.capacidad))
      case O() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), S()), dron.capacidad))
    }
  }

  protected override def girar90Derecha(dron: Dron): Try[Dron] = {
    dron.posicion.orientacion match {
      case N() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), E()), dron.capacidad))
      case S() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), O()), dron.capacidad))
      case E() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), S()), dron.capacidad))
      case O() => Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), N()), dron.capacidad))
    }
  }

  protected override  def validarMovimientoNorte(dron: Dron): Try[Dron] = {
    if (dron.posicion.coordenada.y > 9) Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
    else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y + 1), N()), dron.capacidad))
  }

  protected override def validarMovimientoSur(dron: Dron): Try[Dron] = {
    if (dron.posicion.coordenada.y < (-9)) Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
    else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y - 1), S()), dron.capacidad))
  }

  protected override def validarMovimientoEste(dron: Dron): Try[Dron] = {
    if (dron.posicion.coordenada.x > 9) Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
    else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x + 1, dron.posicion.coordenada.y), E()), dron.capacidad))
  }

  protected override def validarMovimientoOeste(dron: Dron): Try[Dron] = {
    if (dron.posicion.coordenada.x < (-9)) Try(throw new Exception("Nueva posicion excede cuadras permitidas"))
    else Try(Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x - 1, dron.posicion.coordenada.y), O()), dron.capacidad))
  }
}


object ServicioDron extends ServicioDron

sealed trait ServicioCorrientazoADomicilioAlg {
  def realizarDomicilios(rutas: List[Try[Ruta]])

  protected def realizarEntrega(dron: Dron, pedido: Pedido): Try[Dron]

  protected def realizarRuta(dron: Dron, ruta: Ruta)

}

sealed trait ServicioCorrientazoADomicilio extends ServicioCorrientazoADomicilioAlg {
  implicit val ecParaRutas = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))


  override def realizarDomicilios(rutas: List[Try[Ruta]]) = {
    val rutasSucces = rutas.filter(x => x.isSuccess)
    val idDron: List[Int] = Range(1, rutasSucces.size + 1).toList
    val rutasConIdDron: List[(Try[Ruta], Int)] = rutasSucces.zip(idDron)
    rutasConIdDron
      .map(x => x._1.fold(y => {
        Future(List(Dron(1, Posicion(Coordenada(0, 0), N()), 10)))
      }, z => {
        realizarRuta(Dron(x._2, Posicion(Coordenada(0, 0), N()), 10), z)
      }))
  }

  protected override def realizarEntrega(dron: Dron, pedido: Pedido): Try[Dron] = {

    Try(pedido.movimientos.foldLeft(dron) { (acu: Dron, item: Movimiento) => Dron(dron.id, ServicioDron.moverDron(acu, item).get.posicion, dron.capacidad) })

  }

  protected override def realizarRuta(dron: Dron, ruta: Ruta) = {

    Future(ServicioArchivo.entregarReporte(ruta.pedidos.scanLeft(dron)((a, b) => realizarEntrega(a, b).get).tail, dron.id))
  }
}

object ServicioCorrientazoADomicilio extends ServicioCorrientazoADomicilio
