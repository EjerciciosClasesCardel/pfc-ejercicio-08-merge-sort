# Ejercicio 8 — Ordenamiento por mezcla

Fundamentos de Programación Funcional y Concurrente
Escuela de Ingeniería de Sistemas y Computación, Universidad del Valle
Carlos Andrés Delgado Saavedra

Dividir el problema en dos mitades independientes, resolver cada una y
combinar. Es el primer algoritmo del curso con esa forma, y la misma
estructura reaparece al final cuando las dos mitades se resuelvan en
paralelo.

## La idea

Ordenar una lista de un elemento es no hacer nada. Ordenar una más larga es
partirla en dos, ordenar cada mitad y mezclar los dos resultados. La mezcla
es la parte que hace el trabajo: recorre las dos listas a la vez comparando
sus cabezas y tomando la menor.

```
[5, 3, 8, 1]
      /        \
  [5, 3]      [8, 1]
   /  \        /  \
 [5]  [3]    [8]  [1]
   \  /        \  /
  [3, 5]      [1, 8]
      \        /
     [1, 3, 5, 8]
```

El costo es el número de elementos por su logaritmo: hay tantos niveles como
divisiones a la mitad, y cada nivel recorre todos los elementos. Mejor que el
cuadrático del ordenamiento por inserción de la sesión anterior.

## Lo que hay que resolver

Todo va en `app/src/main/scala/taller/Ejercicio.scala`. **No use `sorted`,
`sortWith` ni `sortBy`**: el ejercicio es escribir el algoritmo.

### `partir`

```scala
def partir(l: List[Int]): (List[Int], List[Int])
```

Reparte los elementos en dos mitades. Con un número impar, la primera se
queda con el elemento de más. Lo que importa es que ninguna de las dos quede
vacía cuando la lista tiene dos o más elementos: si eso pasa, la recursión no
termina.

### `mezclar`

```scala
def mezclar(l1: List[Int], l2: List[Int]): List[Int]
```

Recibe dos listas **ya ordenadas** y devuelve una sola ordenada con todos sus
elementos.

| Llamada | Resultado |
|---|---|
| `mezclar(List(1,3,5), List(2,4,6))` | `List(1,2,3,4,5,6)` |
| `mezclar(List(), List(1,2))` | `List(1,2)` |
| `mezclar(List(1,2,2), List(2,3))` | `List(1,2,2,2,3)` |

Fíjese en el último: los repetidos no se descartan.

### `mergeSort`

```scala
def mergeSort(l: List[Int]): List[Int]
```

Ordena de menor a mayor combinando las dos anteriores. Los casos base son la
lista vacía y la de un elemento.

| Llamada | Resultado |
|---|---|
| `mergeSort(List(5,3,8,1,9,2,7))` | `List(1,2,3,5,7,8,9)` |
| `mergeSort(List(3,1,3,2,1))` | `List(1,1,2,3,3)` |
| `mergeSort(List())` | `List()` |

### `mergeSortCon`

```scala
def mergeSortCon(l: List[Int], menor: (Int, Int) => Boolean): List[Int]
```

El mismo algoritmo, pero recibiendo el criterio de orden como parámetro.
`menor(a, b)` responde si `a` va antes que `b`.

```scala
mergeSortCon(List(5,3,8,1), (a, b) => a < b)   // List(1, 3, 5, 8)
mergeSortCon(List(5,3,8,1), (a, b) => a > b)   // List(8, 5, 3, 1)
```

Es el mismo salto de la sesión de funciones de alto orden: lo que cambia
entre ordenar ascendente y descendente sale como argumento en lugar de
escribirse dos veces.

## Cómo está organizado el proyecto

```
app/src/main/scala/taller/
    App.scala          programa de arranque
    Ejercicio.scala    aquí van las cuatro funciones

app/src/test/scala/taller/
    AppSuite.scala        comprueba que el entorno quedó bien
    EjercicioTest.scala   los casos de arriba
```

Su código va en `main`. Las pruebas viven aparte y no se tocan.

Hay una prueba con una lista de 500 elementos. Sirve para ver que el
algoritmo escala: una solución cuadrática también la pasa, pero una que no
parta bien la lista se queda sin pila.

## Cómo se ejecuta

```bash
./gradlew test    # corre las pruebas
```

Las pruebas arrancan en rojo y el trabajo es ponerlas en verde. El informe
completo queda en `app/build/reports/tests/test/index.html`.

## Cómo se trabaja

1. Haga fork de este repositorio.
2. En su fork, abra la pestaña **Actions** y habilítelas. GitHub las deja
   desactivadas en las copias hasta que el dueño lo confirme.
3. Clone, resuelva, haga commit y suba a `main`.
4. Verifique en **Actions** que la última ejecución quedó en verde.

## Restricciones

Este curso trabaja sin estado mutable: nada de `var`, `while`, `return` ni
variables que cambien. El resultado correcto por el camino equivocado no
cuenta como resultado correcto.
