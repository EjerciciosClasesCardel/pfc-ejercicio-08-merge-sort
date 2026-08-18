package taller

class Ejercicio {

  // Tal como está todo devuelve la lista vacía y las pruebas quedan en rojo.

  /** Parte la lista en dos mitades. Con un número impar de elementos, la
    * primera mitad se queda con el elemento de más.
    */
  def partir(l: List[Int]): (List[Int], List[Int]) = {
    (List(), List()) // Completar
  }

  /** Mezcla dos listas ya ordenadas en una sola ordenada. */
  def mezclar(l1: List[Int], l2: List[Int]): List[Int] = {
    List() // Completar
  }

  /** Ordena de menor a mayor partiendo, ordenando cada mitad y mezclando. */
  def mergeSort(l: List[Int]): List[Int] = {
    List() // Completar
  }

  /** Ordena con el criterio que se le entregue.
    *
    * `menor(a, b)` responde si a debe ir antes que b, de modo que el mismo
    * algoritmo sirva para ordenar de forma ascendente o descendente.
    */
  def mergeSortCon(l: List[Int], menor: (Int, Int) => Boolean): List[Int] = {
    List() // Completar
  }
}
