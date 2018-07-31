package co.com.corrientazoADomicilio.modelling.dominio

trait Movimiento{
  def moverDron(posicion: Posicion):Posicion
}
trait Orientacion{
  def rotarDron(movimiento: Movimiento):Orientacion
}
case class Coordenada(x:Int,y:Int)
case class Posicion(coordenada: Coordenada,orientacion: Orientacion)
case class Pedido(movimientos:List[Movimiento])
case class Ruta(pedidos:List[Pedido])
case class PlanRuta(rutas:List[Ruta])