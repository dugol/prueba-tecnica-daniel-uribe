package co.com.corrientazoADomicilio.modelling.dominio

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.Try

sealed trait ServicioArchivoAlg {
  def leerArchivo(url: String): Try[List[List[Movimiento]]]

  def convertirARuta(rutaAConvertir: Try[List[List[Movimiento]]]): Try[Ruta]

  def convertirArchivoARuta(path:String):Try[Ruta]

  def entregarReporte(drones:List[Dron],id:Int)
}

sealed trait ServicioArchivo extends ServicioArchivoAlg {
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

  def convertirArchivoARuta(path: String): Try[Ruta] = {
    convertirARuta(leerArchivo(path))

  }

  def entregarReporte(posiciones:List[Dron],id:Int)={
    val pw=new PrintWriter(new File(s"out${id}.txt"))
    println(posiciones)
    pw.write("==Reporte de entregas==")
    posiciones.map(y=>pw.write(s"\n(${y.posicion.coordenada.x},${y.posicion.coordenada.y}) Orientacion ${y.posicion.orientacion.toString}"))
    pw.close()
  }

}



object ServicioArchivo extends ServicioArchivo







