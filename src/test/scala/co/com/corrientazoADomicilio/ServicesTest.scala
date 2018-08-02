package co.com.corrientazoADomicilio

import java.util.concurrent.Executors

import co.com.corrientazoADomicilio.modelling.dominio.entities._
import co.com.corrientazoADomicilio.modelling.dominio.services.{ServicioArchivo, ServicioCorrientazoADomicilio, ServicioDron}
import org.scalatest.FunSuite

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try

class ServicesTest extends FunSuite {



  test("Mover Dron hacia adelante mirando para el norte"){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),N()),10),A())
    assert(res===Try(Dron(1,Posicion(Coordenada(9,1),N()),10)))
  }
  test("Mover Dron adelante mirando para el sur "){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),S()),10),A())
    assert(res===Try(Dron(1,Posicion(Coordenada(9,-1),S()),10)))
  }
  test("Mover Dron adelante mirando para el Oeste"){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),O()),10),A())
    assert(res===Try(Dron(1,Posicion(Coordenada(8,0),O()),10)))
  }
  test("Mover Dron adelante mirando para el Este"){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),E()),10),A())
    assert(res===Try(Dron(1,Posicion(Coordenada(10,0),E()),10)))
  }
  test("Girar Dron 90° a la izquierda mirando para el norte"){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),N()),10),I())
    assert(res===Try(Dron(1,Posicion(Coordenada(9,0),O()),10)))
  }
  test("Girar Dron 90° a la izquierda mirando para el sur"){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),S()),10),I())
    assert(res===Try(Dron(1,Posicion(Coordenada(9,0),E()),10)))
  }
  test("Girar Dron 90° a la izquierda mirando para el Este"){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),E()),10),I())
    assert(res===Try(Dron(1,Posicion(Coordenada(9,0),N()),10)))
  }
  test("Girar Dron 90° a la izquierda mirando para el Oeste"){
    val res:Try[Dron]=ServicioDron.moverDron(Dron(1,Posicion(Coordenada(9,0),O()),10),I())
    assert(res===Try(Dron(1,Posicion(Coordenada(9,0),S()),10)))
  }
  test("Convertir Archivo a ruta"){
    val res: Try[Ruta]=ServicioArchivo.convertirArchivoARuta("/home/s4n/Downloads/prueba-tecnica-daniel-uribe/in.txt")
    assert(res===Try(Ruta(List(Pedido(List(A(), A(), A(), A(), A(), A(), A(), A(), A(), A(), A())), Pedido(List(A(), A(), A(), A(), I(), A(), A(), D())), Pedido(List(D(), D(), A(), I(), A(), D())), Pedido(List(A(), A(), I(), A(), D(), A(), D())), Pedido(List(A(), A(), A(), D(), A(), I(), A())), Pedido(List(D(), A(), A(), I(), A(), I(), A(), A())), Pedido(List(A(), A(), I(), D(), I(), A(), D(), D(), A())), Pedido(List(A(), A(), A(), D(), I(), D(), D(), A())), Pedido(List(D(), A(), A(), D(), A(), I(), I(), A())), Pedido(List(I(), I(), I(), I(), D(), A(), D(), D(), A(), A())), Pedido(List(A(), A(), D(), A(), A(), I(), A(), A()))))))
  }

  test("Convertir Archivo a ruta fallido"){
    val res: Try[Ruta]=ServicioArchivo.convertirArchivoARuta("/home/s4n/Downloads/prueba-tecnica-daniel-uribe/in2.txt")
    assert(res.isSuccess===false)
  }

  test("Servicio corrientazo a domicilio"){
    implicit val ecParaRutas = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(20))
    val in=Range(1,21).toList
    val rutas:List[Try[Ruta]]=in.map(x=>ServicioArchivo.convertirArchivoARuta(s"/home/s4n/Documents/in${x}.txt"))
    ///val ruta1:Try[Ruta]=ServicioArchivo.convertirArchivoARuta("/home/s4n/Documents/in.txt")
    //val ruta2:Try[Ruta]=ServicioArchivo.convertirArchivoARuta("/home/s4n/Documents/in2.txt")
    ///val rutas:List[Try[Ruta]]=List(ruta1,ruta2)
    ServicioCorrientazoADomicilio.realizarDomicilios(rutas)
    assert(true)
  }

}
