package co.com.corrientazoADomicilio.modelling.dominio

import scala.io.Source

sealed trait ArchivoAlgebra {
  def leerArchivo(url: String): List[List[Movimiento]]

  def convertirARuta(rutaAConvertir: List[List[Movimiento]]): Ruta
}

sealed trait Archivo extends ArchivoAlgebra {
  def leerArchivo(url: String): List[List[Movimiento]] = {
    val source = Source.fromFile(url)

    val lines = source.getLines()
    lines.filter(_.nonEmpty).map(x => x.toList).toList.map {
      x =>
        x.map {
          y =>
            y match {
              case 'A' => A()
              case 'I' => I()
              case 'D' => D()
            }
        }

    }
  }

  def convertirARuta(rutaAConvertir: List[List[Movimiento]]): Ruta = {
    Ruta(rutaAConvertir.map(x => Pedido(x)))
  }
}

sealed trait servicioConvertirArchivoARutaAlgebra{
  def convertirArchivoARuta(path:String):Ruta
}

sealed trait servicioCovertirArchivoARuta extends  servicioConvertirArchivoARutaAlgebra{
  override def convertirArchivoARuta(path: String): Ruta = {
    Archivo.convertirARuta(Archivo.leerArchivo(path))

  }
}
object servicioCovertirArchivoARuta extends servicioCovertirArchivoARuta

private object Archivo extends Archivo







