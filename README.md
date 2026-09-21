# Ejercicio 8 — Tuplas, ordenamiento por mezcla y alto orden

Fundamentos de Programación Funcional y Concurrente
Escuela de Ingeniería de Sistemas y Computación, Universidad del Valle
Carlos Andrés Delgado Saavedra

Cuatro puntos que siguen el orden de la sesión: tuplas para devolver dos
cosas a la vez, tuplas dentro del `match` para mirar dos listas al mismo
tiempo, el ordenamiento por mezcla sobre enteros y después sobre cualquier
tipo con el criterio como parámetro, y las funciones de alto orden sobre
listas escritas a mano, para saber qué hay detrás de `map`, `filter` y
`foldLeft` antes de usarlas.

## Tuplas y dos listas a la vez

Una tupla agrupa valores de tipos distintos en una estructura de tamaño fijo.
Se arma con paréntesis y se desarma con un patrón:

```scala
val pareja = ("numero", 42)
val (texto, valor) = pareja     // texto: String, valor: Int
```

Eso permite que una función devuelva dos resultados de una vez y que quien la
llama los reciba con nombre. También permite analizar dos listas en un solo
`match`, sin anidar uno dentro de otro: se agrupan en una tupla y cada caso
describe una combinación de formas.

```scala
(l1, l2) match {
  case (Nil, Nil)         => ...   // las dos se acabaron
  case (Nil, _)           => ...   // se acabó la primera
  case (_, Nil)           => ...   // se acabó la segunda
  case (x :: xs, y :: ys) => ...   // las dos tienen cabeza
}
```

## Dividir y mezclar

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
cuadrático del ordenamiento por inserción. La misma estructura de dividir y
combinar reaparece cuando las dos mitades se resuelvan en paralelo.

El algoritmo no depende de que los elementos sean enteros; lo único que
necesita es saber cuál de dos va antes. Como `<` no está definido para un
tipo `T` cualquiera, esa comparación viaja como parámetro,
`menor: (T, T) => Boolean`, y la misma función ordena enteros, cadenas,
decimales o pares.

## Lo que hay que resolver

Todo va en `app/src/main/scala/taller/Ejercicio.scala`. **No use `sorted`,
`sortWith`, `sortBy`, `zip`, `unzip`, `map`, `filter`, `foldLeft`,
`foldRight`, `reduceLeft` ni `reduceRight`**: el ejercicio es escribirlos.
`splitAt`, `take`, `drop` y `length` sí se pueden usar.

### Punto 1: `emparejar` y `desemparejar`

```scala
def emparejar(l1: List[Int], l2: List[Int]): List[(Int, Int)]
def desemparejar(l: List[(Int, Int)]): (List[Int], List[Int])
```

`emparejar` forma un par con los elementos que ocupan la misma posición en
las dos listas. Cuando una se acaba antes, los que sobran de la otra se
descartan. `desemparejar` hace el camino de vuelta: de una lista de pares
saca la lista de los primeros y la de los segundos, en el mismo orden. La
llamada recursiva devuelve una tupla, y hay que abrirla para poner cada
componente al frente de la lista que le corresponde.

| Llamada | Resultado |
|---|---|
| `emparejar(List(1,2,3), List(10,20,30))` | `List((1,10), (2,20), (3,30))` |
| `emparejar(List(1,2), List(3,4))` | `List((1,3), (2,4))` |
| `emparejar(List(1,2,3), List(10))` | `List((1,10))` |
| `emparejar(List(1), List(10,20,30))` | `List((1,10))` |
| `emparejar(List(), List(1,2))` | `List()` |
| `emparejar(List(1,2), List())` | `List()` |
| `desemparejar(List((1,10), (2,20), (3,30)))` | `(List(1,2,3), List(10,20,30))` |
| `desemparejar(List((5,-5)))` | `(List(5), List(-5))` |
| `desemparejar(List())` | `(List(), List())` |
| `desemparejar(emparejar(List(4,8,15), List(16,23,42)))` | `(List(4,8,15), List(16,23,42))` |
| `desemparejar(emparejar(List(1,2,3), List(4,5)))` | `(List(1,2), List(4,5))` |

Los dos últimos muestran que `desemparejar` deshace a `emparejar` cuando las
listas tienen el mismo tamaño, y que cuando no, lo que se recortó no vuelve.

### Punto 2: `comparar` y `prefijoComun`

```scala
def comparar(l1: List[Int], l2: List[Int]): Int
def prefijoComun(l1: List[Int], l2: List[Int]): List[Int]
```

`comparar` ordena dos listas como un diccionario ordena las palabras: se
recorren a la vez y decide el primer elemento en que difieren. Devuelve `-1`
si `l1` va antes, `1` si va después y `0` si son iguales. Cuando una lista es
el comienzo de la otra, la que se acaba primero va antes. Las cuatro
combinaciones del `match` sobre la tupla aparecen todas, y el orden en que se
escriben importa.

| Llamada | Resultado |
|---|---|
| `comparar(List(1,2,3), List(1,2,3))` | `0` |
| `comparar(List(), List())` | `0` |
| `comparar(List(1,2,3), List(1,2,4))` | distinto de `0` |
| `comparar(List(1,2,3), List(1,3,0))` | `-1` |
| `comparar(List(2), List(1,9,9))` | `1` |
| `comparar(List(7,1), List(7,0))` | `1` |
| `comparar(List(1,2), List(1,2,3))` | `-1` |
| `comparar(List(1,2,3), List(1,2))` | `1` |
| `comparar(List(), List(0))` | `-1` |

Dos filas separan la comparación correcta de otras que se parecen:
`List(2)` va después de `List(1,9,9)` aunque es más corta y suma menos, y
`List(1,2,3)` va antes de `List(1,3,0)` aunque suma más. Ni el tamaño ni la
suma deciden; decide el primer elemento distinto.

`prefijoComun` devuelve el tramo inicial que las dos listas tienen igual,
hasta la primera diferencia. Se resuelve con un solo caso que compara las dos
cabezas y otro que recoge todo lo demás.

| Llamada | Resultado |
|---|---|
| `prefijoComun(List(1,2,3,4), List(1,2,5,4))` | `List(1,2)` |
| `prefijoComun(List(1,2), List(1,2,3))` | `List(1,2)` |
| `prefijoComun(List(1), List(2))` | `List()` |
| `prefijoComun(List(), List(1))` | `List()` |

En la primera fila el `4` coincide en las dos listas, pero viene después de
una diferencia y no hace parte del prefijo.

### Punto 3: el ordenamiento por mezcla, sobre enteros y sobre cualquier tipo

```scala
def partir[T](l: List[T]): (List[T], List[T])
def mezclar(l1: List[Int], l2: List[Int]): List[Int]
def mergeSort(l: List[Int]): List[Int]
```

`partir` reparte los elementos en dos mitades y las devuelve como tupla. Con
un número impar, la primera se queda con el elemento de más. Partir no
compara nada, así que sirve para listas de cualquier tipo. Lo que importa es
que ninguna de las dos mitades quede vacía cuando la lista tiene dos o más
elementos: si eso pasa, la recursión no termina.

| Llamada | Resultado |
|---|---|
| `partir(List(1,2,3,4))` | dos mitades de 2 elementos con los mismos cuatro números |
| `partir(List(1,2,3,4,5))` | una mitad de 3 elementos y otra de 2 |
| `partir(List())` | `(List(), List())` |
| `partir(List(7))` | una mitad con el 7 y la otra vacía |

`mezclar` recibe dos listas de enteros **ya ordenadas** y devuelve una sola
ordenada con todos sus elementos. Es el lugar natural para el `match` sobre
la tupla.

| Llamada | Resultado |
|---|---|
| `mezclar(List(1,3,5), List(2,4,6))` | `List(1,2,3,4,5,6)` |
| `mezclar(List(1,2), List(3,4))` | `List(1,2,3,4)` |
| `mezclar(List(3,4), List(1,2))` | `List(1,2,3,4)` |
| `mezclar(List(), List(1,2))` | `List(1,2)` |
| `mezclar(List(1,2), List())` | `List(1,2)` |
| `mezclar(List(), List())` | `List()` |
| `mezclar(List(1,2,2), List(2,3))` | `List(1,2,2,2,3)` |

Fíjese en el último: los repetidos no se descartan.

`mergeSort` ordena de menor a mayor combinando las dos anteriores. Los casos
base son la lista vacía y la de un elemento. La tupla que devuelve `partir`
se abre con `val (izq, der) = partir(l)`.

| Llamada | Resultado |
|---|---|
| `mergeSort(List())` | `List()` |
| `mergeSort(List(7))` | `List(7)` |
| `mergeSort(List(2,1))` | `List(1,2)` |
| `mergeSort(List(5,3,8,1,9,2,7))` | `List(1,2,3,5,7,8,9)` |
| `mergeSort(List(1,2,3,4))` | `List(1,2,3,4)` |
| `mergeSort(List(4,3,2,1))` | `List(1,2,3,4)` |
| `mergeSort(List(3,1,3,2,1))` | `List(1,1,2,3,3)` |

Hay además una prueba con 500 elementos que compara contra `sorted`.

Con eso resuelto viene la versión para cualquier tipo:

```scala
def mezclarCon[T](l1: List[T], l2: List[T])(menor: (T, T) => Boolean): List[T]
def mergeSortGen[T](l: List[T])(menor: (T, T) => Boolean): List[T]
def mergeSortCon(l: List[Int], menor: (Int, Int) => Boolean): List[Int]
```

`mezclarCon` es `mezclar` con la comparación como parámetro: `menor(a, b)`
responde si `a` debe ir antes que `b`. `mergeSortGen` es `mergeSort` armado
con `partir` y `mezclarCon`; el criterio se pasa hacia abajo en cada llamada.
`mergeSortCon` es el caso de los enteros y sale de `mergeSortGen` en una
línea.

| Llamada | Resultado |
|---|---|
| `mezclarCon(List("avena","cereza"), List("banana"))((a, b) => a < b)` | `List("avena","banana","cereza")` |
| `mezclarCon(List(5,3), List(4,1))((a, b) => a > b)` | `List(5,4,3,1)` |
| `mezclarCon(List[Int](), List(1,2))((a, b) => a < b)` | `List(1,2)` |
| `mergeSortGen(List("banana","avena","cereza"))((a, b) => a < b)` | `List("avena","banana","cereza")` |
| `mergeSortGen(List(2.5, -1.0, 3.25, 0.0))((a, b) => a < b)` | `List(-1.0, 0.0, 2.5, 3.25)` |
| `mergeSortGen(List[String]())((a, b) => a < b)` | `List()` |
| `mergeSortGen(List(7))((a, b) => a < b)` | `List(7)` |
| `mergeSortGen(List(("ana",3), ("luis",1), ("eva",2)))((a, b) => a._2 < b._2)` | `List(("luis",1), ("eva",2), ("ana",3))` |
| `mergeSortGen(List(List(2,1), List(1,9,9), List(1,2,3), List()))((a, b) => comparar(a, b) < 0)` | `List(List(), List(1,2,3), List(1,9,9), List(2,1))` |
| `mergeSortCon(List(5,3,8,1), (a, b) => a < b)` | `List(1,3,5,8)` |
| `mergeSortCon(List(5,3,8,1), (a, b) => a > b)` | `List(8,5,3,1)` |

Las dos filas de en medio son el punto: el mismo algoritmo ordena pares por
su segunda componente y ordena listas de enteros usando `comparar` del punto
2 como criterio. Hay también una prueba con 500 elementos en orden
descendente.

### Punto 4: `mapear`, `filtrar`, `plegarIzq` y `ordenarPor`

```scala
def mapear[A, B](l: List[A])(f: A => B): List[B]
def filtrar[A](l: List[A])(p: A => Boolean): List[A]
def plegarIzq[A, B](l: List[A])(z: B)(op: (B, A) => B): B
```

Son `map`, `filter` y `foldLeft` escritas con recursión sobre la lista.
`mapear` devuelve la lista con `f` aplicada a cada elemento; `filtrar`, los
elementos que cumplen `p`; `plegarIzq` combina los elementos de izquierda a
derecha partiendo de `z`: para `List(x1, x2, x3)` calcula
`op(op(op(z, x1), x2), x3)`, y con la lista vacía devuelve `z`. La lista y
la función van en grupos de parámetros separados para que Scala deduzca los
tipos de la función a partir de la lista.

| Llamada | Resultado |
|---|---|
| `mapear(List(1,2,3,4))(x => x * x)` | `List(1,4,9,16)` |
| `mapear(List(3,1,2))(x => x * 10)` | `List(30,10,20)` |
| `mapear(List[Int]())(x => x + 1)` | `List()` |
| `mapear(List("hola","mundo","scala"))(s => s.length)` | `List(4,5,5)` |
| `mapear(List(1,2,3,4))(x => x % 2 == 0)` | `List(false,true,false,true)` |
| `filtrar(List(2,4,7,3,8,1))(x => x % 2 == 0)` | `List(2,4,8)` |
| `filtrar(List(-3.0,1.0,-2.0,4.0,0.0))(x => x > 0)` | `List(1.0,4.0)` |
| `filtrar(List(1,3,5))(x => x % 2 == 0)` | `List()` |
| `filtrar(List[Int]())(x => x > 0)` | `List()` |
| `plegarIzq(List(1,2,3,4))(0)((acc, x) => acc + x)` | `10` |
| `plegarIzq(List(1,2,3,4))(1)((acc, x) => acc * x)` | `24` |
| `plegarIzq(List[Int]())(0)((acc, x) => acc + x)` | `0` |
| `plegarIzq(List[Int]())(1)((acc, x) => acc * x)` | `1` |
| `plegarIzq(List(1,2,3))(0)((acc, x) => acc - x)` | `-6` |
| `plegarIzq(List(1,2,3))(List[Int]())((acc, x) => x :: acc)` | `List(3,2,1)` |
| `plegarIzq(List("a","b","c"))("")((acc, s) => acc + s)` | `"abc"` |
| `plegarIzq(List(1,2,3))("")((acc, x) => acc + x)` | `"123"` |

Dos filas separan el plegado por la izquierda del plegado por la derecha:
con la resta, `((0 - 1) - 2) - 3` es `-6` mientras que por la derecha daría
`2`; y con `x :: acc` la lista sale invertida, que es `reverse` en una sola
pasada. Las dos últimas muestran que el resultado puede ser de un tipo
distinto al de los elementos.

```scala
def ordenarPor[T, K](l: List[T])(clave: T => K)(menor: (K, K) => Boolean): List[T]
```

`ordenarPor` ordena los elementos según una clave que se calcula de cada uno:
`clave` la obtiene y `menor` compara claves. Devuelve los elementos
originales, no las claves. `mergeSortGen` hace el ordenamiento; lo que hay
que resolver es cómo llevar la clave junto al elemento para ordenar por la
una y devolver el otro. Las tuplas y `mapear` sirven para eso.

| Llamada | Resultado |
|---|---|
| `ordenarPor(List("banana","kiwi","uva","cerezas"))(s => s.length)((a, b) => a < b)` | `List("uva","kiwi","banana","cerezas")` |
| `ordenarPor(List(-3,1,-2,4))(x => x * x)((a, b) => a < b)` | `List(1,-2,-3,4)` |
| `ordenarPor(List(("ana",3), ("luis",1), ("eva",2)))(p => p._2)((a, b) => a > b)` | `List(("ana",3), ("eva",2), ("luis",1))` |
| `ordenarPor(List[String]())(s => s.length)((a, b) => a < b)` | `List()` |

La segunda fila es la que distingue: se ordena por el cuadrado, pero lo que
sale son los números originales, `1, -2, -3, 4`, y no `1, 4, 9, 16`.

## Cómo está organizado el proyecto

```
app/src/main/scala/taller/
    App.scala          programa de arranque
    Ejercicio.scala    aquí van los cuatro puntos

app/src/test/scala/taller/
    AppSuite.scala        comprueba que el entorno quedó bien
    EjercicioTest.scala   los casos de las tablas
```

Su código va en `main`. Las pruebas viven aparte y no se tocan.

Hay dos pruebas con listas de 500 elementos. Sirven para ver que el
algoritmo escala: una solución cuadrática también las pasa, pero una que no
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
