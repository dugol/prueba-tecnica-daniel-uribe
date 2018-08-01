package co.com.corrientazoADomicilio.modelling.dominio

import scala.io.Source
import scala.util.Try

sealed trait ArchivoAlgebra {
  def leerArchivo(url: String): Try[List[List[Movimiento]]]

  def convertirARuta(rutaAConvertir: Try[List[List[Movimiento]]]): Try[Ruta]
}

sealed trait Archivo extends ArchivoAlgebra {
  def leerArchivo(url: String): Try[List[List[Movimiento]]] = {
    val source = Source.fromFile(url)

    val lines = source.getLines()
    Try(lines.filter(_.nonEmpty).map(x => x.toList).toList.map {
      x =>
        x.map {
          y =>
            y match {
              case 'A' => A()
              case 'I' => I()
              case 'D' => D()
              case _ => throw new Exception("El archivo contiene datos invalidos")
            }
        }

    })
  }

  def convertirARuta(rutaAConvertir: Try[List[List[Movimiento]]]): Try[Ruta] = {

    Try(Ruta(rutaAConvertir.map(x => x.map(y=>Pedido(y))).get))
  }
}

sealed trait servicioConvertirArchivoARutaAlgebra{
  def convertirArchivoARuta(path:String):Try[Ruta]
}

sealed trait servicioCovertirArchivoARuta extends  servicioConvertirArchivoARutaAlgebra{
  override def convertirArchivoARuta(path: String): Try[Ruta] = {
    Archivo.convertirARuta(Archivo.leerArchivo(path))

  }
}
object servicioCovertirArchivoARuta extends servicioCovertirArchivoARuta

private object Archivo extends Archivo







