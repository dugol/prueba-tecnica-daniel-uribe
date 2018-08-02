package co.com.corrientazoADomicilio.modelling.dominio

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Try}

//Servicios de Movimiento

sealed trait ServicioDronAlg {
  def moverDron(dron: Dron, movimiento: Movimiento): Try[Dron]
}

sealed trait ServicioDron extends ServicioDronAlg {
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


object ServicioDron extends ServicioDron

sealed trait ServicioCorrientazoADomicilioAlg {
  def realizarDomicilios(rutas: List[Try[Ruta]])
  protected def realizarEntrega(dron: Dron, pedido: Pedido): Try[Dron]
  protected def realizarRuta(dron: Dron, ruta: Ruta)

}

sealed trait ServicioCorrientazoADomicilio extends ServicioCorrientazoADomicilioAlg{
  implicit val ecParaRutas = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))


  override def realizarDomicilios(rutas: List[Try[Ruta]]) = {
    val rutasSucces=rutas.filter(x=>x.isSuccess)
    val idDron:List[Int]=Range(1,rutasSucces.size+1).toList
    val rutasConIdDron: List[(Try[Ruta], Int)] =rutasSucces.zip(idDron)
    rutasConIdDron
      .map(x=>x._1.fold(y=>{Future(List(Dron(1,Posicion(Coordenada(0,0),N()),10)))},z=>{
        println("Entre")
        realizarRuta(Dron(x._2,Posicion(Coordenada(0,0),N()),10),z)}))
  }

  protected override def realizarEntrega(dron: Dron, pedido: Pedido): Try[Dron] = {

    Try(pedido.movimientos.foldLeft(dron){(acu:Dron,item:Movimiento)=>Dron(dron.id,ServicioDron.moverDron(acu,item).get.posicion,dron.capacidad)})

  }

  protected override def realizarRuta(dron: Dron, ruta: Ruta) = {

    Future(ServicioArchivo.entregarReporte(ruta.pedidos.scanLeft(dron)((a,b)=>realizarEntrega(a,b).get).tail,dron.id))
  }
}

object ServicioCorrientazoADomicilio extends ServicioCorrientazoADomicilio
