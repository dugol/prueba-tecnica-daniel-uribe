package co.com.corrientazoADomicilio.modelling.dominio

import java.io.{File, PrintWriter}
import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

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
  def realizarEntrega(dron: Dron, pedido: Pedido): Try[Dron]
}

sealed trait interpretacionEntrega extends algebraEntrega {
  override def realizarEntrega(dron: Dron, pedido: Pedido): Try[Dron] = {

    Try(pedido.movimientos.foldLeft(dron){(acu:Dron,item:Movimiento)=>Dron(dron.id,movimientoDron.moverDron(acu,item).get.posicion,dron.capacidad)})
    /*if (pedido.movimientos.size > 0) {
      realizarEntrega(Dron(dron.id, movimientoDron.moverDron(dron, pedido.movimientos.head).posicion, dron.capacidad), Pedido(pedido.movimientos.tail))
    } else {
      dron
    }*/

  }
}

private object interpretacionEntrega extends interpretacionEntrega

sealed trait algebraRutaDeEntrega {
  def realizarRuta(dron: Dron, ruta: Ruta)
  def entregarReporte(drones:List[Dron],id:Int)

}

sealed trait interpretacionRutaEntrega extends algebraRutaDeEntrega {
  implicit val ecParaRutas = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))

  def entregarReporte(posiciones:List[Dron],id:Int)={
    val pw=new PrintWriter(new File(s"out${id}.txt"))
    println(posiciones)
    pw.write("==Reporte de entregas==")
    posiciones.map(y=>pw.write(s"\n(${y.posicion.coordenada.x},${y.posicion.coordenada.y}) Orientacion ${y.posicion.orientacion.toString}"))
    pw.close()
  }

  override def realizarRuta(dron: Dron, ruta: Ruta) = {

    Future(entregarReporte(ruta.pedidos.scanLeft(dron)((a,b)=>interpretacionEntrega.realizarEntrega(a,b).get).tail,dron.id))
  }
}


private object interpretacionRutaEntrega extends interpretacionRutaEntrega

sealed trait algebraServicioCorrientazoADomicilio {
  def realizarDomicilios(rutas: List[Try[Ruta]])

}

sealed trait servicioCorrientazoADomicilio extends algebraServicioCorrientazoADomicilio{
  implicit val ecParaRutas = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))


  override def realizarDomicilios(rutas: List[Try[Ruta]]) = {
    val rutasSucces=rutas.filter(x=>x.isSuccess)
    val idDron:List[Int]=Range(1,rutasSucces.size+1).toList
    val rutasConIdDron: List[(Try[Ruta], Int)] =rutasSucces.zip(idDron)
    rutasConIdDron
      .map(x=>x._1.fold(y=>{Future(List(Dron(1,Posicion(Coordenada(0,0),N()),10)))},z=>{
        println("Entre")
        interpretacionRutaEntrega.realizarRuta(Dron(x._2,Posicion(Coordenada(0,0),N()),10),z)}))
  }
}

object servicioCorrientazoADomicilio extends servicioCorrientazoADomicilio
