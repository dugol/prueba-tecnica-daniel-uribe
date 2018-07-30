import co.com.corrientazoADomicilio.modelling.dominio._

import scala.io.Source

val source=Source.fromFile("/home/s4n/Documents/in.txt")
val lines=source.getLines()
val res=lines.filter(_.nonEmpty).map(x=>x.toList).toList.map{
  x=>x.map{
    y=>y match {
      case 'A'=> A()
      case 'I' => I()
      case 'D'=> D()
    }
  }

}
Ruta(res.map(x=>Pedido(x)))