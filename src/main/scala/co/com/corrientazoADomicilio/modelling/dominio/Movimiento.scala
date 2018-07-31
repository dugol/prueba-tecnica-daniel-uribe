package co.com.corrientazoADomicilio.modelling.dominio

/*//Posibles Movimientos del Dron
sealed trait Movimiento{
  def movimiento(posicion: Posicion):Posicion
}
//Movimiento Adelante
case class A() extends Movimiento{
  def movimiento(posicion: Posicion):Posicion={
    posicion.orientacion match{
      case N()=>  Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y+1),posicion.orientacion.rotacion(A()))
      case S() => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y-1),posicion.orientacion.rotacion(A()))
      case O() => Posicion(Coordenada(posicion.coordenada.x-1,posicion.coordenada.y),posicion.orientacion.rotacion(A()))
      case E() => Posicion(Coordenada(posicion.coordenada.x+1,posicion.coordenada.y),posicion.orientacion.rotacion(A()))
    }
  }
}
//Giro de 90° a la izquierda
case class I() extends Movimiento{
  def movimiento(posicion: Posicion):Posicion={
    posicion.orientacion match{
      case N()=> Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(I()))
      case S() => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(I()))
      case O() => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(I()))
      case E() => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(I()))
    }
  }
}
//Giro de 90° a la derecha
case class D() extends Movimiento{
  def movimiento(posicion: Posicion):Posicion={
    posicion.orientacion match{
      case N()=> Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(D()))
      case S() => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(D()))
      case O() => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(D()))
      case E() => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotacion(D()))
    }
  }
}


//Posibles orientaciones del dron
sealed trait Orientacion{
  def rotacion(movimiento: Movimiento):Orientacion
}
//Mirando hacia el Norte
case class N() extends Orientacion{
  def rotacion(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A() => N()
      case I() => O()
      case D() => E()
    }
  }

}
//Mirando hacia el Sur
case class S() extends Orientacion{
  def rotacion(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A() => S()
      case I() => E()
      case D() => O()
    }
  }
}
//Mirando hacia el Este
case class E() extends Orientacion{
  def rotacion(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A() => E()
      case I() => N()
      case D() => S()
    }
  }
}
//Mirando hacia el Oeste
case class O() extends Orientacion{
  def rotacion(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A() => O()
      case I() => S()
      case D() => N()
    }
  }
}

//Coordenada dentro de la cuadricula
//Restringir los enteros para que solo acepte numero del 1 al 10(Pendiente)
case class Coordenada(x:Int, y:Int)

case class Posicion(coordenada: Coordenada, orientacion: Orientacion)


case class Pedido(movimientos: List[Movimiento]) {
  def entregarPedido(posicionActual: Posicion): Posicion = {
    var posicion = posicionActual
    movimientos.foreach(x => posicion = x.movimiento(posicion))
    posicion


  }
}

case class Ruta(pedidos:List[Pedido]) {

    def entregarRuta(): List[Posicion] = {

      var posicionPedido: Posicion = Posicion(Coordenada(0, 0), N())
      val posicionesPedidos: List[Posicion] = pedidos.map {
        x =>
          posicionPedido = Pedido(x.movimientos).entregarPedido(posicionPedido)
          posicionPedido
      }

      posicionesPedidos
    }

}


case class PlanRutas(rutas:List[Ruta]){

  def asignarRutas():List[List[Posicion]]={

    rutas.map{
      x=>x.entregarRuta()
    }
  }
}
*/





