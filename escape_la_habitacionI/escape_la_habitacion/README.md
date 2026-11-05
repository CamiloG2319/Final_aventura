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

REGLAS DE ENTREGA

Los equipos deben tener entre 2 y 3 integrantes.
Todos deben realizar commits propios y equitativos.
Todos deben participar en la sustentacion.
Cada metodo debe cumplir una sola funcion.