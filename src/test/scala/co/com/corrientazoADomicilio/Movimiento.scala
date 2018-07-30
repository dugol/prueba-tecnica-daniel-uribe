package co.com.corrientazoADomicilio

import co.com.corrientazoADomicilio.modelling.dominio
import co.com.corrientazoADomicilio.modelling.dominio._
import org.scalatest.FunSuite

class Movimiento extends FunSuite {

  test("Pedido"){
    val posicionInicial=Posicion(Coordenada(0,0),N())
    val movimientos:List[dominio.Movimiento with Product with Serializable]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val res=Pedido(movimientos).entregarPedido(posicionInicial)
    assert(res===Posicion(Coordenada(-2,4),N()))
  }


  test("Lista de pedidos"){
    //val posicionInicial=Posicion(Coordenada(0,0),N())
    val movimientos1:List[dominio.Movimiento with Product with Serializable]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val movimientos2:List[dominio.Movimiento with Product with Serializable]=List(D(),D(),A(),I(),A(),D())
    val movimientos3:List[dominio.Movimiento with Product with Serializable]=List(A(),A(),I(),A(),D(),A(),D())
    val pedido1:Pedido=Pedido(movimientos1)
    val pedido2:Pedido= Pedido(movimientos2)
    val pedido3:Pedido=Pedido(movimientos3)
    val pedidos:List[Pedido]=List(pedido1,pedido2,pedido3)

    val output:List[Posicion]=Ruta(pedidos).entregarRuta()

    assert(output===List(Posicion(Coordenada(-2,4),N()),Posicion(Coordenada(-1,3),S()),Posicion(Coordenada(0,0),O())))

  }

  test("Conjunto de Rutas"){

    val movimientos1:List[dominio.Movimiento with Product with Serializable]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val movimientos2:List[dominio.Movimiento with Product with Serializable]=List(D(),D(),A(),I(),A(),D())
    val movimientos3:List[dominio.Movimiento with Product with Serializable]=List(A(),A(),I(),A(),D(),A(),D())
    val movimientos4:List[dominio.Movimiento with Product with Serializable]=List(A(),A(),A(),A(),I(),A(),A(),D())
    val movimientos5:List[dominio.Movimiento with Product with Serializable]=List(D(),D(),A(),I(),A(),D())
    val movimientos6:List[dominio.Movimiento with Product with Serializable]=List(A(),A(),I(),A(),D(),A(),D())
    val pedido1:Pedido=Pedido(movimientos1)
    val pedido2:Pedido= Pedido(movimientos2)
    val pedido3:Pedido=Pedido(movimientos3)
    val pedido4:Pedido=Pedido(movimientos4)
    val pedido5:Pedido= Pedido(movimientos5)
    val pedido6:Pedido=Pedido(movimientos6)
    val pedidos1:List[Pedido]=List(pedido1,pedido2,pedido3)
    val pedidos2:List[Pedido]=List(pedido4,pedido5,pedido6)
    val ruta1: Ruta =Ruta(pedidos1)
    val ruta2: Ruta =Ruta(pedidos2)
    val rutas: List[Ruta] =List(ruta1,ruta2)


    val output:List[List[Posicion]]=PlanRutas(rutas).asignarRutas()

    assert(output==List(List(Posicion(Coordenada(-2,4),N()),Posicion(Coordenada(-1,3),S()),Posicion(Coordenada(0,0),O())),List(Posicion(Coordenada(-2,4),N()),Posicion(Coordenada(-1,3),S()),Posicion(Coordenada(0,0),O()))))

  }

}
