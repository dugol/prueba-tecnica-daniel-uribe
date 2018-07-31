package co.com.corrientazoADomicilio.modelling.dominio

//Servicios de Movimiento

sealed trait servicioDronAlgebra {
  def moverDron(dron: Dron, movimiento: Movimiento): Dron
}

sealed trait servicioDron extends servicioDronAlgebra {
  override def moverDron(dron: Dron, movimiento: Movimiento): Dron = {
    movimiento match {
      case A() => dron.posicion.orientacion match {
        case N() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y + 1), N()))
        case S() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y - 1), S()))
        case O() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x - 1, dron.posicion.coordenada.y), O()))
        case E() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x + 1, dron.posicion.coordenada.y), E()))
      }
      case I() => dron.posicion.orientacion match {
        case N() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), O()))
        case S() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), E()))
        case E() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), N()))
        case O() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), S()))
      }
      case D() => dron.posicion.orientacion match {
        case N() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), E()))
        case S() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), O()))
        case E() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), S()))
        case O() => Dron(dron.id, Posicion(Coordenada(dron.posicion.coordenada.x, dron.posicion.coordenada.y), N()))
      }
    }
  }
}

object servicioDron extends servicioDron


sealed trait algebraEntrega {
  def realizarEntrega(dron: Dron, pedido: Pedido): Dron
}

sealed trait interpretacionEntrega extends algebraEntrega {
  override def realizarEntrega(dron: Dron, pedido: Pedido): Dron = {
    if (pedido.movimientos.size > 0) {
      println(dron)
      realizarEntrega(Dron(dron.id, servicioDron.moverDron(dron, pedido.movimientos.head).posicion), Pedido(pedido.movimientos.tail))
    } else {
      dron
    }

  }
}

object interpretacionEntrega extends interpretacionEntrega

sealed trait algebraRutaDeEntrega {
  def realizarRuta(dron: Dron, ruta: Ruta): List[Dron]
}

sealed trait interpretacionRutaEntrega extends algebraRutaDeEntrega {
  override def realizarRuta(dron: Dron, ruta: Ruta): List[Dron] = {
    ruta.pedidos.scanLeft(dron)((a, b) => interpretacionEntrega.realizarEntrega(a, b)).tail
  }
}


object interpretacionRutaEntrega extends interpretacionRutaEntrega
