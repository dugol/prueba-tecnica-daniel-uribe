package co.com.corrientazoADomicilio.modelling.dominio

import scala.io.Source

sealed trait servicioArchivoAlgebra {
  def leerArchivo(url: String): List[List[Movimiento]]

  def convertirARuta(rutaAConvertir: List[List[Movimiento]]): Ruta
}

sealed trait servicioArchivo extends servicioArchivoAlgebra {
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

object servicioArchivo extends servicioArchivo







