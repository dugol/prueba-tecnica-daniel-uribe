package co.com.corrientazoADomicilio

import java.util.concurrent.Executors

import scala.concurrent.duration._
import co.com.corrientazoADomicilio.modelling.dominio._
import org.scalatest.FunSuite

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try

class Mov extends FunSuite {

  /*test("Pedido"){
    val posicionInicial=Posicion(Coordenada(0,0),N())
    val movimientos:List[Movimiento]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val res=interpretacionEntrega.realizarEntrega(Dron(1,posicionInicial,10),Pedido(movimientos))
    assert(res===Dron(1,Posicion(Coordenada(-2,4),N()),10))
  }*/


  test("Lista de pedidos"){
    val posicionInicial=Posicion(Coordenada(0,0),N())
    implicit val ecParaRutas = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
   /* val movimientos1:List[Movimiento]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val movimientos2:List[Movimiento]=List(D(),D(),A(),I(),A(),D())
    val movimientos3:List[Movimiento]=List(A(),A(),I(),A(),D(),A(),D())
    val pedido1:Pedido=Pedido(movimientos1)
    val pedido2:Pedido= Pedido(movimientos2)
    val pedido3:Pedido=Pedido(movimientos3)
    val pedidos:List[Pedido]=List(pedido1,pedido2,pedido3)*/

    val ruta:Try[Ruta]=servicioCovertirArchivoARuta.convertirArchivoARuta("/home/s4n/Documents/in.txt")
    val resDefinitiva:List[Dron]=ruta.fold[List[Dron]](x=>{List(Dron(1,Posicion(Coordenada(0,0),N()),10))},s=>{
      val dron:Dron=Dron(1,posicionInicial,10)
      val output:Future[List[Dron]]=interpretacionRutaEntrega.realizarRuta(dron,s)
      val res=Await.result(output,10 seconds)
      res
      })



    //assert(resDefinitiva===List(Dron(1,Posicion(Coordenada(-2,4),N()),10), Dron(1,Posicion(Coordenada(-1,3),S()),10), Dron(1,Posicion(Coordenada(0,0),O()),10), Dron(1,Posicion(Coordenada(-4,1),O()),10), Dron(1,Posicion(Coordenada(-5,1),S()),10), Dron(1,Posicion(Coordenada(-5,-1),O()),10), Dron(1,Posicion(Coordenada(-7,-1),E()),10), Dron(1,Posicion(Coordenada(-7,-3),E()),10), Dron(1,Posicion(Coordenada(-7,-2),N()),10), Dron(1,Posicion(Coordenada(-5,2),N()),10)))
    assert(resDefinitiva===List(Dron(1,Posicion(Coordenada(0,0),N()),10)))

  }
/*
  test("Conjunto de Rutas"){
    //val archivo=File().leerArchivo()
    //val ruta1:Ruta=File().convertirARuta(archivo)
    //val ruta2:Ruta =ruta1
    val posicionInicial=Posicion(Coordenada(0,0),N())
    val movimientos1:List[Movimiento]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val movimientos2:List[Movimiento]=List(D(),D(),A(),I(),A(),D())
    val movimientos3:List[Movimiento]=List(A(),A(),I(),A(),D(),A(),D())
    val movimientos4:List[Movimiento]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val movimientos5:List[Movimiento]=List(D(),D(),A(),I(),A(),D())
    val movimientos6:List[Movimiento]=List(A(),A(),I(),A(),D(),A(),D())
    val pedido1:Pedido=Pedido(movimientos1)
    val pedido2:Pedido= Pedido(movimientos2)
    val pedido3:Pedido=Pedido(movimientos3)
    val pedido4:Pedido=Pedido(movimientos4)
    val pedido5:Pedido= Pedido(movimientos5)
    val pedido6:Pedido=Pedido(movimientos6)
    val pedidos1:List[Pedido]=List(pedido1,pedido2,pedido3)
    val pedidos2:List[Pedido]=List(pedido4,pedido5,pedido6)
    val ruta1: Ruta =Ruta(pedidos1)
    //val ruta2: Ruta =Ruta(pedidos2)

    //val rutas: List[Ruta] =List(ruta1,ruta2)


    val output:List[Dron]=interpretacionRutaEntrega.realizarRuta(Dron(1,posicionInicial),ruta1)

    assert(output===List(Dron(1,Posicion(Coordenada(-2,4),N())), Dron(1,Posicion(Coordenada(-1,3),S())), Dron(1,Posicion(Coordenada(0,0),O()))))

  }
/*

  test("Ejemplo"){
    sealed trait Movimiento
    case class A() extends Movimiento
    case class D() extends Movimiento

    sealed trait Orientacion
    case class N() extends Orientacion
    case class S() extends Orientacion
    case class W() extends Orientacion
    case class E() extends Orientacion

    case class Dron(x:Int, y:Int)

    trait ServicioDronAlg{
      def moverDron(d:Dron, m:Movimiento): Dron
    }

    trait ServicioDron extends ServicioDronAlg{
      override def moverDron(d: Dron, m: Movimiento): Dron = {
        m match {
          case A() => Dron(d.x+1, d.y)
          case D() => Dron(d.x, d.y+1)
        }
      }
    }

    object ServicioDron extends ServicioDron

    val d:Dron = ServicioDron.moverDron(Dron(1,1), A())

    assert(Dron(2,1) == d  )

  }
  */
*/
}
