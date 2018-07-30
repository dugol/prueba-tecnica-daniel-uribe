package co.com.corrientazoADomicilio.modelling.dominio

import scala.io.Source

case class File() {

  val source=Source.fromFile("/home/s4n/Documents/in.txt")
  def leerArchivo(): List[List[Product with Serializable with co.com.corrientazoADomicilio.modelling.dominio.Movimiento]]={

    val lines=source.getLines()
    lines.filter(_.nonEmpty).map(x=>x.toList).toList.map{
      x=>x.map{
        y=>y match {
          case 'A'=> A()
          case 'I' => I()
          case 'D'=> D()
        }
      }

    }
  }

  def convertirARuta(rutaAConvertir: List[List[Product with Serializable with co.com.corrientazoADomicilio.modelling.dominio.Movimiento]]): Ruta ={
    Ruta(rutaAConvertir.map(x=>Pedido(x)))
  }

  def crearOut(reporte:List[List[Posicion]], nombre:String): Unit ={

  }
}
