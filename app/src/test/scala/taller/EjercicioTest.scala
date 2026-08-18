package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EjercicioTest extends AnyFunSuite {
  val obj = new Ejercicio()

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
}
