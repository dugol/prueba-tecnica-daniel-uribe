package co.com.corrientazoADomicilio.modelling.dominio.entities

sealed trait Movimiento

case class A() extends Movimiento

case class D() extends Movimiento

case class I() extends Movimiento

case class Dron(id: Int, posicion: Posicion,capacidad:Int)

sealed trait Orientacion

case class S() extends Orientacion{
  override def toString: String = {
    "Sur"
  }
}

case class N() extends Orientacion{
  override def toString: String = {
    "Norte"
  }
}

case class E() extends Orientacion{
  override def toString: String = {
    "Este"
  }
}

case class O() extends Orientacion{
  override def toString: String = {
    "Oeste"
  }
}


case class Coordenada(x: Int, y: Int)

case class Posicion(coordenada: Coordenada, orientacion: Orientacion)

case class Pedido(movimientos: List[Movimiento])

case class Ruta(pedidos: List[Pedido])

//case class PlanRuta(rutas: List[Ruta])