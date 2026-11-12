ESCAPE: LA HABITACION

Proyecto final del curso de Logica de Programacion.

Es un juego de escape en consola compuesto por tres habitaciones. La habitacion 1 ya esta terminada. Las habitaciones 2 y 3 deben ser creadas y programadas por el equipo.

COMO EJECUTARLO

Se necesita tener instalado el JDK.

Desde la carpeta del proyecto:

javac Main.java Mapa.java Sonido.java
java Main

ARCHIVOS DEL PROYECTO

Main.java: motor principal del juego.
habitacion1.txt: matriz visual de la habitacion 1.
zonas1.txt: matriz de movimiento.
objetos1.csv: objetos de la habitacion 1.
arte/: dibujos ASCII del juego.
Sonido.java: efectos MIDI y musica.
musica/: archivos MIDI.
docs/: documentacion JavaDoc.
Mapa.java: demostracion del movimiento en una matriz.

Las habitaciones 2 y 3 requieren los archivos habitacion2.txt, habitacion3.txt, zonas2.txt, zonas3.txt, objetos2.csv y objetos3.csv.

LAS DOS MATRICES

Cada habitacion utiliza dos matrices del mismo tamaño.

Matriz visual:
Representa los dibujos que aparecen en pantalla.

Matriz de movimiento:
Define el comportamiento de cada casilla.
.: piso.
#: pared u obstaculo.
Letra: zona de un objeto.

La posicion inicial del jugador se representa con @ en la matriz visual y con . en la matriz de movimiento.

COMO SE JUEGA

El juego tiene tres pantallas:

1. Habitacion:
El jugador se mueve con W, A, S y D. El panel muestra el progreso y el inventario.

2. Pregunta:
Al acercarse a un objeto aparece una vista previa con las opciones de mirarlo de cerca o continuar explorando.

3. Escena:
Muestra el dibujo completo del objeto y su descripcion. Se cierra con ENTER.

PUZZLE DE LA HABITACION 1

La alfombra esconde una llave.
La llave abre el candado del cofre.
El cofre revela un numero par de dos digitos.
La nota indica una operacion que debe aplicarse al numero.
La puerta solicita el resultado.

Las operaciones posibles son:
Doble.
Mitad.
Sumar 10.
Restar 10.
Invertir el numero.

TECLAS ESPECIALES

H: mostrar una pista.
M: activar o desactivar la musica.
E: activar o desactivar los efectos de sonido.
X: activar el easter egg.

SONIDO

El proyecto utiliza archivos MIDI.

Los efectos disponibles son:
exito()
error()
recoger()
golpe()
victoria()

La musica de fondo se reproduce mediante archivos .mid.

FORMATO DE OBJETOS CSV

Cada archivo utiliza el siguiente formato:

simbolo,nombre,archivo,detalle

simbolo: letra que identifica el objeto en la matriz.
nombre: nombre del objeto.
archivo: nombre de los archivos de arte.
detalle: descripcion del objeto y sus pistas.

No se deben utilizar comas dentro de los textos ni tildes o eñes.

PLACEHOLDERS OBLIGATORIOS

En Main.java hay cuatro metodos que deben implementarse:

1. seleccionarTipoJuego()

Permite elegir entre los modos HISTORIA, HABITACION UNICA y CONTRARRELOJ. Debe validar las entradas.

2. configurarJugador()

Solicita el nombre del jugador y la dificultad.

Dificultad 1: facil, con pistas ilimitadas.
Dificultad 2: normal, con pistas limitadas.
Dificultad 3: dificil, con maximo tres intentos para introducir la clave de la puerta.

3. configurarArte()

Permite personalizar el titulo ASCII y la decoracion de las habitaciones.

4. siguienteHabitacion()

Permite avanzar entre habitaciones. Al abrir la puerta de las habitaciones 1 y 2 debe cargar la siguiente habitacion, mostrar una transicion y conservar el inventario.

Al completar la habitacion 3 se muestra la pantalla de victoria.

REGLAS DEL PROYECTO

La habitacion 1 debe seguir funcionando correctamente.
Las habitaciones 2 y 3 deben ser originales.
Cada equipo debe crear sus propios puzzles, objetos, dibujos y textos.
Los cambios deben probarse progresivamente.
Todos los integrantes deben poder explicar el codigo.

METODOS IMPORTANTES DE MAIN.JAVA

Ciclo del juego:
main()

Configuracion:
seleccionarTipoJuego()
configurarJugador()
configurarArte()
siguienteHabitacion()

Carga del mundo:
cargarMundo()
cargarMatriz()
cargarObjetos()
leerLineas()
buscarInicio()

Pantalla principal:
dibujarPantalla()
filaVisual()
panelEstado()
panelObjeto()

Movimiento:
mover()
casillaZona()

Objetos:
preguntarObjeto()
buscarObjeto()

Escenas:
mostrarEscena()
abrirEscena()
esperarEnter()
imprimirArte()

Final del juego:
abrirPuerta()
mostrarVictoria()

Puzzle y pistas:
calcularClave()
darPista()
easterEgg()

Inventario:
guardarObjeto()
tieneObjeto()
recordarMirado()

COMO CREAR UN OBJETO NUEVO

1. Dibujarlo en la matriz visual.
2. Asignarle una letra en la matriz de movimiento.
3. Agregarlo al archivo CSV.
4. Crear sus dos archivos de arte: mini_x.txt y x.txt.

COMO AGREGAR COMPORTAMIENTOS ESPECIALES

Los comportamientos especiales se programan en preguntarObjeto().

Se pueden crear objetos que entreguen elementos, exijan objetos del inventario o cambien el estado del juego.

INVENTARIO

Los metodos guardarObjeto() y tieneObjeto() permiten crear puzzles que conecten diferentes objetos y habitaciones.

RETOS ADICIONALES

Contador de movimientos u oxigeno.
Colores ANSI.
Musica diferente por habitacion.
Implementacion del easter egg con la tecla X.
Nuevas pistas para cada habitacion.

DOCUMENTACION JAVADOC

La documentacion se encuentra en docs/index.html.

Para regenerarla:

javadoc -d docs -charset UTF-8 *.java

ENTREGABLES

1. Juego completo con las tres habitaciones.
2. Puzzles, objetos, matrices y dibujos originales.
3. Navegacion completa hasta la victoria.
4. Implementacion de los cuatro placeholders obligatorios.
5. Validacion de las entradas del jugador.
6. Documentacion JavaDoc actualizada.
7. Presentacion del proyecto con arquitectura, explicacion de los placeholders y demostracion en vivo.

PRIMEROS PASOS

1. Descargar y descomprimir el proyecto.
2. Crear un repositorio llamado final_aventura en GitHub.
3. Subir el codigo original como primer commit, sin archivos .class.
4. Agregar a los integrantes como colaboradores.
5. Jugar la habitacion 1.
6. Revisar los placeholders antes de comenzar a programar.

**Paso a paso**

SOLUCIÓN DEL JUEGO: ESCAPE — LA HABITACIÓN
==========================================

Para jugar las tres habitaciones seguidas, selecciona el modo HISTORIA.
Cuando te acerques a un objeto, elige 1 para interactuar.

HABITACIÓN 1 — EL CUARTO
-----------------------
1. Busca la ALFOMBRA (parte inferior izquierda).
   Interactúa con ella y recoge la LLAVE OXIDADA.

2. Ve al COFRE (lado derecho de la habitación).
   Se abrirá con la llave oxidada. Anota el NÚMERO que aparece.

3. Ve a la NOTA (parte superior izquierda).
   Léela para saber qué operación debes hacer con el número del cofre.

4. Calcula la clave según indique la nota:
   - DOBLE: multiplica el número por 2.
   - MITAD: divide el número entre 2.
   - MÁS 10: suma 10.
   - MENOS 10: resta 10.
   - AL REVÉS: invierte los dos dígitos.

5. Ve a la PUERTA (pared inferior) y escribe el resultado.
   Así pasarás a la siguiente habitación.

IMPORTANTE: el número del cofre y la operación cambian entre partidas.
No existe una clave fija.


HABITACIÓN 2 — LA CASA
---------------------
1. Ve al SOFÁ (parte inferior derecha) y recoge las PILAS.

2. Ve a la MESA (zona central derecha) y recoge la LINTERNA.
   Necesitas la linterna y las pilas para bajar al sótano.

3. Ve a la COCINA (parte superior derecha) y lee el papel
   de la nevera. Allí aparece el orden de la clave:
      Primero: la hora del reloj.
      Segundo: la página del libro.
      Tercero: el número de la suerte del abrigo.

4. Ve al RELOJ (parte superior izquierda).
   Anota el número mostrado: es el PRIMER DÍGITO.

5. Ve al LIBRERO (parte superior izquierda).
   Anota el número de página: es el SEGUNDO DÍGITO.

6. Ve a la CAMA (parte inferior izquierda).
   Mira debajo del colchón y recoge la LLAVE PEQUEÑA.

7. Ve al GUARDARROPA (parte inferior izquierda).
   Ábrelo con la llave pequeña. Anota el número de la suerte
   del abrigo: es el TERCER DÍGITO.

8. Une los tres dígitos SIN SUMARLOS:
      RELOJ + LIBRO + ABRIGO
   Por ejemplo, si aparecen 4, 7 y 2, la clave es 472.
   (472 es solo un ejemplo; la clave real cambia en cada partida).

9. Ve a la TRAMPILLA (parte inferior derecha).
   Escribe la clave de tres dígitos para bajar al sótano.
   Si no te deja bajar, comprueba que tengas PILAS y LINTERNA.


HABITACIÓN 3 — EL SÓTANO
------------------------
1. Ve al BAÚL (parte inferior derecha).
   Ábrelo con la LLAVE OXIDADA que recogiste en la habitación 1.
   En su interior conseguirás una PALANCA.

2. Ve a la CAJA DE MADERA (centro de la habitación).
   Interactúa para moverla debajo de la ventana alta.

3. Ve a la VENTANA ALTA (parte superior derecha).
   Como la caja ya está en su lugar, podrás alcanzarla.
   Usa la palanca para forzar la ventana.

4. Sal por la ventana. ¡GANASTE EL JUEGO!

NOTA: si empiezas directamente en la habitación 3 con el modo
HABITACIÓN ÚNICA, el juego te proporciona la llave oxidada.

REGLAS DE ENTREGA

Los equipos deben tener entre 2 y 3 integrantes.
Todos deben realizar commits propios y equitativos.
Todos deben participar en la sustentacion.
Cada metodo debe cumplir una sola funcion.
