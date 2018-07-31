package co.com.corrientazoADomicilio.modelling.dominio

//Servicios de Movimiento
sealed trait A extends Movimiento{
  def moverDron(posicion: Posicion):Posicion={
    posicion.orientacion match{
      case N=>  Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y+1),posicion.orientacion.rotarDron(A))
      case S => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y-1),posicion.orientacion.rotarDron(A))
      case O => Posicion(Coordenada(posicion.coordenada.x-1,posicion.coordenada.y),posicion.orientacion.rotarDron(A))
      case E => Posicion(Coordenada(posicion.coordenada.x+1,posicion.coordenada.y),posicion.orientacion.rotarDron(A))
    }
  }
}
object A extends A

sealed trait I extends Movimiento{
  def moverDron(posicion: Posicion):Posicion={
    posicion.orientacion match{
      case N=> Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(I()))
      case S => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(I()))
      case O => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(I()))
      case E => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(I()))
    }
  }
}
object I extends I

sealed trait D extends Movimiento{
  def moverDron(posicion: Posicion):Posicion={
    posicion.orientacion match{
      case N=> Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(D))
      case S => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(D))
      case O => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(D))
      case E => Posicion(Coordenada(posicion.coordenada.x,posicion.coordenada.y),posicion.orientacion.rotarDron(D))
    }
  }
}
object D extends D
//----------------------------------------------------------------------------
//Servicios de Orientacion

sealed trait N extends Orientacion{
  def rotarDron(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A => N
      case I => O
      case D => E
    }
  }

}
object N extends N

sealed trait S extends Orientacion{
  def rotarDron(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A => S
      case I => E
      case D => O
    }
  }
}
object S extends S

sealed trait E extends Orientacion{
  def rotarDron(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A => E
      case I => N
      case D => S
    }
  }
}
object E extends E

sealed trait O extends Orientacion{
  def rotarDron(movimiento: Movimiento):Orientacion={
    movimiento match {
      case A => O
      case I => S
      case D => N
    }
  }
}
object O extends O

sealed trait algebraEntrega{
  def realizarEntrega(pedido: Pedido, posicionActual: Posicion):Posicion
}

sealed trait interpretacionEntrega extends algebraEntrega{
  override def realizarEntrega(pedido: Pedido, posicionActual: Posicion): Posicion = {

  }
}