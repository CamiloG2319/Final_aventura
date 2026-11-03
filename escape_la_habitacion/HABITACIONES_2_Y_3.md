# Habitaciones 2 y 3: guía de diseño y solución (⚠️ spoilers)

Esta guía explica qué se agregó al juego para que el equipo pueda
**sustentarlo**: qué archivos son nuevos, qué métodos se programaron y cómo se
resuelve cada habitación.

## Archivos nuevos

| Archivo | Qué es |
| --- | --- |
| `habitacion2.txt` / `zonas2.txt` / `objetos2.csv` | La casa: 41 x 13 (mismo alto que las otras; la habitación 1 mide 37 x 13), 2 cuartos con puerta central, 10 objetos |
| `habitacion3.txt` / `zonas3.txt` / `objetos3.csv` | El sótano: 41 x 13, 5 objetos |
| `arte/*.txt` | Escena y `mini_` de cada objeto nuevo + `casa.txt` y `sotano.txt` (transiciones) |

Símbolos de zona (únicos en todo el juego, para no chocar con la habitación 1: `N C A P`):

- **Casa:** `S` sofá, `M` mesa, `L` librero, `R` reloj, `D` cama, `G` guardarropa,
  `H` chimenea, `K` cocina, `V` ventanas, `P` trampilla del sótano.
- **Sótano:** `E` escalera, `T` baúl, `J` caja, `B` barriles, `P` ventana alta.

## Habitación 2: la casa (sale por el sótano)

Las **ventanas** (`V`) están selladas con tablones: no sirven para escapar y
sólo dan la pista de que el aire frío sube del piso. La salida es la
**trampilla** (`P`), que pide una clave de 3 dígitos **sorteada en cada partida**.

Cadena de resolución:

1. **Sofá** → pilas. **Mesa** → linterna. Sin las dos cosas **no se puede bajar**
   (el sótano está a oscuras; la chimenea lo insinúa: *NO BAJES SIN LUZ*).
2. **Cocina** → el papel explica la clave: *hora del reloj + página del
   marcapaginas + número de la suerte del abrigo, en ese orden*.
3. **Reloj** → 1.er dígito. **Librero** → 2.º dígito.
4. **Cama** → llave pequeña → abre el **guardarropa** → 3.er dígito.
5. Digitar los 3 dígitos en la **trampilla** → cargar la habitación 3.

## Habitación 3: el sótano (sale por la ventana)

1. **Barriles** → mensaje de tiza: la ventana está alta y atascada (hace falta
   *algo para subir* y *algo para forzarla*).
2. **Baúl** → su candado es el mismo de la habitación 1: se abre con la
   **llave oxidada** que ya está en el inventario → palanca.
3. **Caja** → se arrastra bajo la ventana (`cajaMovida = true`).
4. **Ventana** → con caja + palanca: victoria.

Así el inventario se conserva entre habitaciones: la llave de la 1 sirve en la
3, y la linterna de la 2 es obligatoria para llegar a la 3.

## Cambios en `Main.java`

| Dónde | Qué se hizo |
| --- | --- |
| Variables | `digitosCasa[]` (los 3 dígitos) y `cajaMovida` |
| `siguienteHabitacion()` | **PLACEHOLDER 4 resuelto:** `habitacionActual++`, `cargarMundo(...)`, transición y `return false` |
| `cargarMundo()` | Si es la habitación 2, llama a `sortearCodigoCasa()` |
| `preguntarObjeto()` | Despacha a `interactuarCasa()` / `interactuarSotano()` según la habitación |
| `darPista()` | Ahora responde según la habitación (`darPistaHabitacion1/Casa/Sotano`) |
| Métodos nuevos | `sortearCodigoCasa`, `interactuarCasa`, `entregarObjeto`, `escenaConDato`, `mirarObjeto`, `abrirGuardarropa`, `abrirSotano`, `interactuarSotano`, `abrirBaul`, `moverCaja`, `escaparPorVentana`, `transicion` |

Todos los métodos nuevos tienen JavaDoc y `docs/` ya fue regenerado.

## Lo que sigue pendiente (del README)

Esta entrega **no** implementa los otros 3 placeholders obligatorios:
`seleccionarTipoJuego()`, `configurarJugador()` (nombre + dificultad) y
`configurarArte()`. Tampoco el límite de intentos en `abrirPuerta()` para
DIFÍCIL ni el easter egg `X`.
