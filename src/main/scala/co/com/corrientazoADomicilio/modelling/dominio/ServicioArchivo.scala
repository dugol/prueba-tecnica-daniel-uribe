package co.com.corrientazoADomicilio.modelling.dominio

import scala.io.Source

case class ServicioArchivo() {

  //val source=Source.fromFile("/home/s4n/Documents/in.txt")
  def leerArchivo(url:String): List[List[Product with Serializable with co.com.corrientazoADomicilio.modelling.dominio.Movimiento]]={
    val source=Source.fromFile(url)

    val lines=source.getLines()
    lines.filter(_.nonEmpty).map(x=>x.toList).toList.map{
      x=>x.map{
        y=>y match {
          case 'A'=> A()
          case 'I' => I()
          case 'D'=> D()
          case _ => throw new Exception(s"Caracter invalido para creacion de instruccion: $y")
        }
      }

    }
  }

  def convertirARuta(rutaAConvertir: List[List[Product with Serializable with co.com.corrientazoADomicilio.modelling.dominio.Movimiento]]): Ruta ={
    Ruta(rutaAConvertir.map(x=>Pedido(x)))
  }

}
