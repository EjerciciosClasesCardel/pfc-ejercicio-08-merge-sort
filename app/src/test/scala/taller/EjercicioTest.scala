package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EjercicioTest extends AnyFunSuite {
  val obj = new Ejercicio()

  // Punto 1: emparejar y desemparejar

  test("Emparejar dos listas del mismo tamaño forma un par por posición") {
    assert(obj.emparejar(List(1, 2, 3), List(10, 20, 30)) == List((1, 10), (2, 20), (3, 30)))
    assert(obj.emparejar(List(1, 2), List(3, 4)) == List((1, 3), (2, 4)))
  }

  test("Emparejar se detiene donde se acaba la lista más corta") {
    assert(obj.emparejar(List(1, 2, 3), List(10)) == List((1, 10)))
    assert(obj.emparejar(List(1), List(10, 20, 30)) == List((1, 10)))
    assert(obj.emparejar(List(), List(1, 2)) == List())
    assert(obj.emparejar(List(1, 2), List()) == List())
  }

  test("Desemparejar separa los pares en dos listas del mismo orden") {
    assert(obj.desemparejar(List((1, 10), (2, 20), (3, 30))) == (List(1, 2, 3), List(10, 20, 30)))
    assert(obj.desemparejar(List((5, -5))) == (List(5), List(-5)))
    assert(obj.desemparejar(List()) == (List(), List()))
  }

  test("Desemparejar deshace a emparejar y muestra lo que se recortó") {
    val a = List(4, 8, 15)
    val b = List(16, 23, 42)
    assert(obj.desemparejar(obj.emparejar(a, b)) == (a, b))
    assert(obj.desemparejar(obj.emparejar(List(1, 2, 3), List(4, 5))) == (List(1, 2), List(4, 5)))
  }

  // Punto 2: comparar y prefijoComun

  test("Comparar da cero solo cuando las dos listas son iguales") {
    assert(obj.comparar(List(1, 2, 3), List(1, 2, 3)) == 0)
    assert(obj.comparar(List(), List()) == 0)
    assert(obj.comparar(List(1, 2, 3), List(1, 2, 4)) != 0)
  }

  test("Comparar decide en el primer elemento distinto") {
    assert(obj.comparar(List(1, 2, 3), List(1, 3, 0)) == -1)
    assert(obj.comparar(List(2), List(1, 9, 9)) == 1)
    assert(obj.comparar(List(7, 1), List(7, 0)) == 1)
  }

  test("Comparar pone la lista que se acaba primero antes que la otra") {
    assert(obj.comparar(List(1, 2), List(1, 2, 3)) == -1)
    assert(obj.comparar(List(1, 2, 3), List(1, 2)) == 1)
    assert(obj.comparar(List(), List(0)) == -1)
  }

  test("El prefijo común se corta en la primera diferencia") {
    assert(obj.prefijoComun(List(1, 2, 3, 4), List(1, 2, 5, 4)) == List(1, 2))
    assert(obj.prefijoComun(List(1, 2), List(1, 2, 3)) == List(1, 2))
    assert(obj.prefijoComun(List(1), List(2)) == List())
    assert(obj.prefijoComun(List(), List(1)) == List())
  }

  // Punto 3: partir, mezclar, mergeSort, mezclarCon, mergeSortGen y mergeSortCon

  test("Partir reparte los elementos entre las dos mitades") {
    val (a, b) = obj.partir(List(1, 2, 3, 4))
    assert(a.length == 2 && b.length == 2)
    assert((a ++ b).sorted == List(1, 2, 3, 4))
  }

  test("Partir con un número impar deja el elemento de más en la primera") {
    val (a, b) = obj.partir(List(1, 2, 3, 4, 5))
    assert(a.length == 3 && b.length == 2)
  }

  test("Partir listas de cero y un elemento") {
    assert(obj.partir(List()) == (List(), List()))
    val (a, b) = obj.partir(List(7))
    assert(a.length + b.length == 1)
  }

  test("Mezclar dos listas ordenadas") {
    assert(obj.mezclar(List(1, 3, 5), List(2, 4, 6)) == List(1, 2, 3, 4, 5, 6))
    assert(obj.mezclar(List(1, 2), List(3, 4)) == List(1, 2, 3, 4))
    assert(obj.mezclar(List(3, 4), List(1, 2)) == List(1, 2, 3, 4))
  }

  test("Mezclar con una lista vacía devuelve la otra") {
    assert(obj.mezclar(List(), List(1, 2)) == List(1, 2))
    assert(obj.mezclar(List(1, 2), List()) == List(1, 2))
    assert(obj.mezclar(List(), List()) == List())
  }

  test("Mezclar conserva los elementos repetidos") {
    assert(obj.mezclar(List(1, 2, 2), List(2, 3)) == List(1, 2, 2, 2, 3))
  }

  test("Ordenar casos pequeños") {
    assert(obj.mergeSort(List()) == List())
    assert(obj.mergeSort(List(7)) == List(7))
    assert(obj.mergeSort(List(2, 1)) == List(1, 2))
  }

  test("Ordenar una lista desordenada") {
    assert(obj.mergeSort(List(5, 3, 8, 1, 9, 2, 7)) == List(1, 2, 3, 5, 7, 8, 9))
  }

  test("Ordenar una lista que ya viene ordenada y una al revés") {
    assert(obj.mergeSort(List(1, 2, 3, 4)) == List(1, 2, 3, 4))
    assert(obj.mergeSort(List(4, 3, 2, 1)) == List(1, 2, 3, 4))
  }

  test("Ordenar conserva los repetidos") {
    assert(obj.mergeSort(List(3, 1, 3, 2, 1)) == List(1, 1, 2, 3, 3))
  }

  test("Ordenar una lista grande") {
    val entrada = List.tabulate(500)(i => (i * 37) % 500)
    assert(obj.mergeSort(entrada) == entrada.sorted)
  }

  test("Ordenar con un criterio propio") {
    val l = List(5, 3, 8, 1)
    assert(obj.mergeSortCon(l, (a, b) => a < b) == List(1, 3, 5, 8))
    assert(obj.mergeSortCon(l, (a, b) => a > b) == List(8, 5, 3, 1))
  }

  test("MezclarCon recibe el criterio y sirve para cualquier tipo") {
    assert(obj.mezclarCon(List("avena", "cereza"), List("banana"))((a, b) => a < b) == List("avena", "banana", "cereza"))
    assert(obj.mezclarCon(List(5, 3), List(4, 1))((a, b) => a > b) == List(5, 4, 3, 1))
    assert(obj.mezclarCon(List[Int](), List(1, 2))((a, b) => a < b) == List(1, 2))
  }

  test("MergeSortGen ordena cadenas y decimales con el criterio que se le entregue") {
    assert(obj.mergeSortGen(List("banana", "avena", "cereza"))((a, b) => a < b) == List("avena", "banana", "cereza"))
    assert(obj.mergeSortGen(List(2.5, -1.0, 3.25, 0.0))((a, b) => a < b) == List(-1.0, 0.0, 2.5, 3.25))
    assert(obj.mergeSortGen(List[String]())((a, b) => a < b) == List())
    assert(obj.mergeSortGen(List(7))((a, b) => a < b) == List(7))
  }

  test("MergeSortGen ordena pares por su segunda componente") {
    val personas = List(("ana", 3), ("luis", 1), ("eva", 2))
    assert(obj.mergeSortGen(personas)((a, b) => a._2 < b._2) == List(("luis", 1), ("eva", 2), ("ana", 3)))
  }

  test("MergeSortGen ordena listas de enteros con comparar como criterio") {
    val listas = List(List(2, 1), List(1, 9, 9), List(1, 2, 3), List[Int]())
    val esperado = List(List[Int](), List(1, 2, 3), List(1, 9, 9), List(2, 1))
    assert(obj.mergeSortGen(listas)((a, b) => obj.comparar(a, b) < 0) == esperado)
  }

  test("MergeSortGen con una lista grande y orden descendente") {
    val entrada = List.tabulate(500)(i => (i * 37) % 500)
    assert(obj.mergeSortGen(entrada)((a, b) => a > b) == entrada.sorted.reverse)
  }

  // Punto 4: mapear, filtrar, plegarIzq y ordenarPor

  test("Mapear transforma cada elemento y conserva el orden") {
    assert(obj.mapear(List(1, 2, 3, 4))(x => x * x) == List(1, 4, 9, 16))
    assert(obj.mapear(List(3, 1, 2))(x => x * 10) == List(30, 10, 20))
    assert(obj.mapear(List[Int]())(x => x + 1) == List())
  }

  test("Mapear puede cambiar el tipo de la lista") {
    assert(obj.mapear(List("hola", "mundo", "scala"))(s => s.length) == List(4, 5, 5))
    assert(obj.mapear(List(1, 2, 3, 4))(x => x % 2 == 0) == List(false, true, false, true))
  }

  test("Filtrar deja solo los elementos que cumplen el predicado") {
    assert(obj.filtrar(List(2, 4, 7, 3, 8, 1))(x => x % 2 == 0) == List(2, 4, 8))
    assert(obj.filtrar(List(-3.0, 1.0, -2.0, 4.0, 0.0))(x => x > 0) == List(1.0, 4.0))
    assert(obj.filtrar(List(1, 3, 5))(x => x % 2 == 0) == List())
    assert(obj.filtrar(List[Int]())(x => x > 0) == List())
  }

  test("PlegarIzq reproduce la suma y el producto, también con la lista vacía") {
    assert(obj.plegarIzq(List(1, 2, 3, 4))(0)((acc, x) => acc + x) == 10)
    assert(obj.plegarIzq(List(1, 2, 3, 4))(1)((acc, x) => acc * x) == 24)
    assert(obj.plegarIzq(List[Int]())(0)((acc, x) => acc + x) == 0)
    assert(obj.plegarIzq(List[Int]())(1)((acc, x) => acc * x) == 1)
  }

  test("PlegarIzq asocia por la izquierda") {
    assert(obj.plegarIzq(List(1, 2, 3))(0)((acc, x) => acc - x) == -6)
    assert(obj.plegarIzq(List(1, 2, 3))(List[Int]())((acc, x) => x :: acc) == List(3, 2, 1))
  }

  test("PlegarIzq puede devolver un tipo distinto al de los elementos") {
    assert(obj.plegarIzq(List("a", "b", "c"))("")((acc, s) => acc + s) == "abc")
    assert(obj.plegarIzq(List(1, 2, 3))("")((acc, x) => acc + x) == "123")
  }

  test("OrdenarPor ordena por la clave y devuelve los elementos originales") {
    assert(obj.ordenarPor(List("banana", "kiwi", "uva", "cerezas"))(s => s.length)((a, b) => a < b) == List("uva", "kiwi", "banana", "cerezas"))
    assert(obj.ordenarPor(List(-3, 1, -2, 4))(x => x * x)((a, b) => a < b) == List(1, -2, -3, 4))
  }

  test("OrdenarPor con orden descendente y con la lista vacía") {
    val personas = List(("ana", 3), ("luis", 1), ("eva", 2))
    assert(obj.ordenarPor(personas)(p => p._2)((a, b) => a > b) == List(("ana", 3), ("eva", 2), ("luis", 1)))
    assert(obj.ordenarPor(List[String]())(s => s.length)((a, b) => a < b) == List())
  }
}
