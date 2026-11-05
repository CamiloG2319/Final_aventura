# Habitaciones 2 y 3: guía de diseño y solución

Archivos nuevos

| Archivo | Qué es |
| --- | --- |
| `habitacion2.txt` / `zonas2.txt` / `objetos2.csv` | La casa, 2 cuartos con puerta central, 10 objetos |
| `habitacion3.txt` / `zonas3.txt` / `objetos3.csv` | El sótano, 5 objetos |
| `arte/*.txt` | Escena y `mini_` de cada objeto nuevo + `casa.txt` y `sotano.txt` (transiciones) |

Símbolos de zona (únicos en todo el juego, para no chocar con la habitación 1: `N C A P`):

- Casa: `S` sofá, `M` mesa, `L` librero, `R` reloj, `D` cama, `G` guardarropa,
  `H` chimenea, `K` cocina, `V` ventanas, `P` trampilla del sótano.
- Sótano: `E` escalera, `T` baúl, `J` caja, `B` barriles, `P` ventana alta.

## Habitación 2: la casa 

Las ventanas (`V`) están selladas con tablones: no sirven para escapar y
sólo dan la pista de que el aire frío sube del piso. La salida es la
trampilla (`P`), que pide una clave de 3 dígitos aleatoria en cada partida.

Cadena de resolución:

1. Sofá → pilas. Mesa → linterna. Sin las dos cosas no se puede bajar
   (el sótano está a oscuras; la chimenea lo insinúa diciendo que no se puede bajar sin luz).
2. Cocina → el papel explica la clave: hora del reloj + página del marcapaginas + número de la suerte del abrigo, en ese orden.
3. Reloj → 1.er dígito. Librero → 2.º dígito.
4. Cama → llave pequeña → abre el guardarropa → 3.er dígito.
5. Digitar los 3 dígitos en la trampilla → cargar la habitación 3.

Habitación 3: el sótano 

1. Barriles → mensaje de tiza: la ventana está alta y atascada (hace falta algo para subir y algo para forzarla).
2. Baúl → su candado es el mismo de la habitación 1: se abre con la llave oxidada que ya está en el inventario → palanca.
3. Caja → se arrastra bajo la ventana (`cajaMovida = true`).
4. Ventana → con caja + palanca: victoria.

Así el inventario se conserva entre habitaciones: la llave de la 1 sirve en la
3, y la linterna de la 2 es obligatoria para llegar a la 3.

Cambios en `Main.java`

| Dónde | Qué se hizo |
| --- | --- |
| Variables | `digitosCasa[]` (los 3 dígitos) y `cajaMovida` |
| `siguienteHabitacion()` | `habitacionActual++`, `cargarMundo(...)`, transición y `return false` |
| `cargarMundo()` | Si es la habitación 2, llama a `sortearCodigoCasa()` |
| `preguntarObjeto()` | Despacha a `interactuarCasa()` / `interactuarSotano()` según la habitación |
| `darPista()` | Ahora responde según la habitación (`darPistaHabitacion1/Casa/Sotano`) |
| Métodos nuevos | `sortearCodigoCasa`, `interactuarCasa`, `entregarObjeto`, `escenaConDato`, `mirarObjeto`, `abrirGuardarropa`, `abrirSotano`, `interactuarSotano`, `abrirBaul`, `moverCaja`, `escaparPorVentana`, `transicion` |

| Qué | Dónde | Cómo funciona |
| --- | --- | --- |
| Tipo de juego | `seleccionarTipoJuego()` | Menú validado: 1 HISTORIA, 2 HABITACIÓN ÚNICA (pregunta cuál; en la 3 se empieza con la llave oxidada), 3 CONTRARRELOJ |
| Terminal del jugador | `configurarJugador()`, `leerNombre()` | Nombre (1 a 12 caracteres, ASCII) y dificultad 1-3, que se aplican de verdad (filas siguientes) |
| Arte | `configurarArte()`, `introHabitacionInicial()` | Título propio `arte/titulo_juego.txt`; escena de entrada del cuarto (`arte/cuarto.txt`), la casa y el sótano |
| Navegación | `siguienteHabitacion()` | Ya estaba resuelta (ver arriba) |
| Intentos en DIFÍCIL | `abrirPuerta()` | 3 claves falladas por puerta y el teclado se bloquea: derrota. ENTER vacío no cuenta |
| Pistas por dificultad | `darPista()` | FÁCIL ilimitadas, NORMAL 3, DIFÍCIL 1 (constante `PISTAS_POR_DIFICULTAD`) |
| Panel | `panelEstado()` | Muestra nombre, dificultad, movimientos y pistas |
| Resumen final | `imprimirResumen()` | Movimientos, objetos mirados y pistas usadas (victoria y derrota) |
| Derrota | `mostrarDerrota()` | Se queda sin intentos (DIFÍCIL) o sin movimientos (CONTRARRELOJ) |
| Easter egg | `easterEgg()` | La tecla `X` muestra "I LOVE CODING WITH FRIENDS" (`arte/easter_egg.txt`); con ENTER el juego sigue igual, sin gastar movimientos |

Límites de CONTRARRELOJ (constante `LIMITE_CONTRARRELOJ`): FÁCIL 500, NORMAL 400,
DIFÍCIL 300 movimientos. Cuentan todas las teclas `W A S D`, incluso los choques;
el recorrido más corto de las 3 habitaciones necesita unos 190.

- `localizarCarpetaJuego()` encuentra `habitacion1.txt`, `arte/` y `musica/`
  aunque el juego se ejecute desde otra carpeta o la ruta tenga espacios. Si no
  los encuentra, explica qué hacer en vez de mostrar errores.
- `leerLinea()` envuelve todas las lecturas del teclado: si la entrada se cierra
  (Ctrl+D) el juego se despide y apaga el sonido, sin error de Java.
- `leerEntero()` y `leerNombre()` validan los menús: letras, vacíos, números
  enormes o tildes no rompen nada.
- Todo el código del juego, sus textos y sus archivos son ASCII, así que se ven igual en
  cualquier terminal.
