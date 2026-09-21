package taller

class Ejercicio {

  // Tal como está, todo devuelve la lista vacía, la tupla de listas vacías
  // o cero, y las pruebas quedan en rojo.

  // Punto 1. Tuplas: armarlas y desarmarlas.

  // Forma un par con los elementos que ocupan la misma posición en las dos
  // listas; los que sobran de la más larga se descartan.
  def emparejar(l1: List[Int], l2: List[Int]): List[(Int, Int)] = {
    List() // Completar
  }

  // De una lista de pares saca dos listas: la de los primeros y la de los
  // segundos, en el mismo orden.
  def desemparejar(l: List[(Int, Int)]): (List[Int], List[Int]) = {
    (List(), List()) // Completar
  }

  // Punto 2. Tuplas en reconocimiento de patrones: dos listas a la vez.

  // Compara las dos listas como se ordenan las palabras en un diccionario:
  // -1 si l1 va antes, 1 si va después, 0 si son iguales. La que se acaba
  // primero va antes.
  def comparar(l1: List[Int], l2: List[Int]): Int = {
    0 // Completar
  }

  // El tramo inicial que las dos listas tienen igual, hasta la primera
  // diferencia.
  def prefijoComun(l1: List[Int], l2: List[Int]): List[Int] = {
    List() // Completar
  }

  // Punto 3. Ordenamiento por mezcla, primero sobre enteros y luego
  // sobre cualquier tipo con el criterio como parámetro.

  // Parte la lista en dos mitades. Con un número impar de elementos, la
  // primera se queda con el elemento de más.
  def partir[T](l: List[T]): (List[T], List[T]) = {
    (List(), List()) // Completar
  }

  // Mezcla dos listas de enteros ya ordenadas en una sola ordenada.
  def mezclar(l1: List[Int], l2: List[Int]): List[Int] = {
    List() // Completar
  }

  // Ordena de menor a mayor partiendo, ordenando cada mitad y mezclando.
  def mergeSort(l: List[Int]): List[Int] = {
    List() // Completar
  }

  // Mezcla dos listas ya ordenadas según menor, para cualquier tipo.
  // menor(a, b) responde si a debe ir antes que b.
  def mezclarCon[T](l1: List[T], l2: List[T])(menor: (T, T) => Boolean): List[T] = {
    List() // Completar
  }

  // Ordena una lista de cualquier tipo con el criterio que se le entregue.
  def mergeSortGen[T](l: List[T])(menor: (T, T) => Boolean): List[T] = {
    List() // Completar
  }

  // Ordena enteros con el criterio que se le entregue.
  def mergeSortCon(l: List[Int], menor: (Int, Int) => Boolean): List[Int] = {
    List() // Completar
  }

  // Punto 4. Funciones de alto orden sobre listas, escritas a mano.

  // La lista con f aplicada a cada elemento, en el mismo orden.
  def mapear[A, B](l: List[A])(f: A => B): List[B] = {
    List() // Completar
  }

  // Los elementos que cumplen p, en el mismo orden.
  def filtrar[A](l: List[A])(p: A => Boolean): List[A] = {
    List() // Completar
  }

  // Combina los elementos de izquierda a derecha partiendo de z:
  // op(op(op(z, x1), x2), x3) para List(x1, x2, x3).
  def plegarIzq[A, B](l: List[A])(z: B)(op: (B, A) => B): B = {
    z // Completar
  }

  // Ordena los elementos según la clave que se calcula de cada uno;
  // menor compara claves. Devuelve los elementos, no las claves.
  def ordenarPor[T, K](l: List[T])(clave: T => K)(menor: (K, K) => Boolean): List[T] = {
    List() // Completar
  }
}
