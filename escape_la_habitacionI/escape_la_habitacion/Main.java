import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static Scanner lector = new Scanner(System.in);

    public static char[][] visual;

    public static char[][] zonas;

    public static String[][] objetos;

    public static int habitacionActual = 1;

    public static final int TOTAL_HABITACIONES = 3;

    public static String nombreJugador = "aventurero";

    public static int dificultad = 1;

    public static int tipoJuego = 1;

    public static int fila;

    public static int columna;

    public static String mensaje = "Despiertas encerrado y no recuerdas nada. Explora.";

    public static String mirados = "";

    public static String[] inventario = new String[10];

    public static int totalInventario = 0;

    public static int anchoMapa;

    public static final int ANCHO_PANEL = 26;

    public static final int ANCHO_ESCENA = 64;

    public static int numeroCofre;

    public static String[] operaciones = {
        "el DOBLE del numero",
        "la MITAD del numero",
        "el numero MAS 10",
        "el numero MENOS 10",
        "el numero escrito AL REVES"
    };

    public static int operacionElegida;

    public static String clave;

    public static int[] digitosCasa = new int[3];

    public static boolean cajaMovida = false;

    public static int habitacionInicial = 1;

    public static int habitacionFinal = TOTAL_HABITACIONES;

    public static int movimientos = 0;

    public static int limiteMovimientos = 0;

    public static int pistasRestantes = -1;

    public static int pistasUsadas = 0;

    public static int intentosPuerta = 0;

    public static int miradosAcumulados = 0;

    public static boolean finMostrado = false;

    public static final int INTENTOS_DIFICIL = 3;

    public static final int MAX_NOMBRE = 12;

    public static final int[] LIMITE_CONTRARRELOJ = {500, 400, 300};

    public static final int[] PISTAS_POR_DIFICULTAD = {-1, 3, 1};

    public static String carpetaJuego = "";

    public static void main(String[] args) {
        if (!localizarCarpetaJuego()) {
            return;
        }
        Sonido.iniciar();

        configurarArte();
        tipoJuego = seleccionarTipoJuego();
        configurarJugador();

        habitacionActual = habitacionInicial;
        if (!cargarMundo(habitacionActual)) {
            Sonido.apagar();
            return;
        }
        Sonido.musicaFondo(rutaJuego("musica/ambiente.mid"));
        prepararPartida();

        boolean jugando = true;
        while (jugando) {
            dibujarPantalla(panelEstado());
            System.out.print(" > ");
            String comando = leerLinea().trim().toUpperCase();
            if (comando.equals("Q")) {
                jugando = false;
            } else if (comando.equals("H")) {
                mensaje = darPista();
            } else if (comando.equals("M")) {
                Sonido.alternarMusica();
                mensaje = "Musica de fondo: " + (Sonido.musicaActiva ? "ACTIVADA" : "APAGADA");
            } else if (comando.equals("E")) {
                Sonido.alternarEfectos();
                mensaje = "Efectos de sonido: " + (Sonido.efectosActivos ? "ACTIVADOS" : "APAGADOS");
            } else if (comando.equals("X")) {
                easterEgg();
            } else if (mover(comando)) {
                jugando = false;
            }
        }

        if (!finMostrado) {
            System.out.println();
            System.out.println(" Te rindes, " + nombreJugador + ". La habitacion gana esta vez...");
        }
        Sonido.apagar();
    }

    public static int seleccionarTipoJuego() {
        System.out.println(" +------------------ TIPO DE JUEGO ------------------+");
        System.out.println("   1. HISTORIA          las 3 habitaciones, una tras otra");
        System.out.println("   2. HABITACION UNICA  eliges cual y escapas solo de esa");
        System.out.println("   3. CONTRARRELOJ      la historia con movimientos limitados");
        System.out.println();
        int modo = leerEntero(" > Elige el modo (1-3): ", 1, 3);

        habitacionInicial = 1;
        habitacionFinal = TOTAL_HABITACIONES;
        if (modo == 2) {
            System.out.println();
            System.out.println("   1. El cuarto    2. La casa    3. El sotano");
            habitacionInicial = leerEntero(" > Elige la habitacion (1-" + TOTAL_HABITACIONES + "): ",
                    1, TOTAL_HABITACIONES);
            habitacionFinal = habitacionInicial;
        }
        System.out.println();
        return modo;
    }

    public static void configurarJugador() {
        System.out.println(" +---------------- TERMINAL DEL JUGADOR ----------------+");
        nombreJugador = leerNombre();

        System.out.println();
        System.out.println("   1. FACIL    pistas ilimitadas");
        System.out.println("   2. NORMAL   " + PISTAS_POR_DIFICULTAD[1] + " pistas");
        System.out.println("   3. DIFICIL  " + PISTAS_POR_DIFICULTAD[2] + " pista y solo "
                + INTENTOS_DIFICIL + " intentos de clave por puerta");
        dificultad = leerEntero(" > Elige la dificultad (1-3): ", 1, 3);

        pistasRestantes = PISTAS_POR_DIFICULTAD[dificultad - 1];
        pistasUsadas = 0;
        movimientos = 0;
        limiteMovimientos = 0;
        if (tipoJuego == 3) {
            limiteMovimientos = LIMITE_CONTRARRELOJ[dificultad - 1];
        }

        System.out.println();
        System.out.println(" Listo, " + nombreJugador + ". Dificultad " + nombreDificultad()
                + (limiteMovimientos > 0 ? " con " + limiteMovimientos + " movimientos." : "."));
        System.out.println();
    }

    public static String leerNombre() {
        String nombre = "";
        while (nombre.isEmpty()) {
            System.out.print(" > Tu nombre (maximo " + MAX_NOMBRE + " letras): ");
            String entrada = leerLinea().trim();
            if (entrada.isEmpty()) {
                System.out.println(" El nombre no puede estar vacio.");
            } else if (entrada.length() > MAX_NOMBRE) {
                System.out.println(" Muy largo: maximo " + MAX_NOMBRE + " caracteres.");
            } else if (!nombreValido(entrada)) {
                System.out.println(" Usa solo letras sin tilde, numeros, espacio, - o _.");
            } else {
                nombre = entrada;
            }
        }
        return nombre;
    }

    public static boolean nombreValido(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            boolean letra = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
            boolean digito = c >= '0' && c <= '9';
            if (!letra && !digito && c != ' ' && c != '-' && c != '_') {
                return false;
            }
        }
        return true;
    }

    public static String nombreDificultad() {
        if (dificultad == 2) {
            return "NORMAL";
        }
        if (dificultad == 3) {
            return "DIFICIL";
        }
        return "FACIL";
    }

    public static String nombreModo() {
        if (tipoJuego == 2) {
            return "HABITACION UNICA";
        }
        if (tipoJuego == 3) {
            return "CONTRARRELOJ";
        }
        return "HISTORIA";
    }

    public static void configurarArte() {
        imprimirArte("arte/titulo_juego.txt");
        System.out.println();
    }

    public static void prepararPartida() {
        if (habitacionActual == 3 && habitacionInicial == 3) {
            guardarObjeto("llave oxidada");
        }
        introHabitacionInicial();
    }

    public static void introHabitacionInicial() {
        if (habitacionActual == 1) {
            transicion("el cuarto", "cuarto",
                    "Despiertas encerrado y no recuerdas nada. Una puerta con teclado numerico es la unica salida. Tiene que haber una clave...");
        } else if (habitacionActual == 2) {
            transicion("la casa", "casa",
                    "Despiertas en una casa silenciosa y repleta de muebles. Las ventanas dejan pasar una luz gris. Tiene que haber otra salida...");
            mensaje = "Despiertas en la casa. Revisa los muebles con calma.";
        } else {
            transicion("el sotano", "sotano",
                    "Despiertas en un sotano humedo. En el bolsillo hay una llave oxidada que no recuerdas haber guardado. Algo de luz entra desde arriba.");
            mensaje = "Despiertas en el sotano con una llave oxidada en el bolsillo.";
        }
    }

    public static boolean siguienteHabitacion() {
        habitacionActual++;
        if (!cargarMundo(habitacionActual)) {
            return true;
        }
        if (habitacionActual == 2) {
            transicion("la casa", "casa",
                    "La puerta se abre y das con una casa silenciosa y repleta de muebles. Las ventanas dejan pasar una luz gris. Tiene que haber otra salida...");
            mensaje = "Entras a la casa. Revisa los muebles con calma.";
        } else {
            transicion("el sotano", "sotano",
                    "Abres la trampilla y bajas con la linterna encendida. Arriba el cerrojo se cierra de golpe: ya no hay vuelta atras.");
            mensaje = "El sotano huele a humedad. Algo de luz entra desde arriba.";
        }
        return false;
    }

    public static boolean conectarHabitacionExterna(int numero) {

        return false;
    }

    public static boolean cargarMundo(int numero) {
        visual = cargarMatriz("habitacion" + numero + ".txt");
        zonas = cargarMatriz("zonas" + numero + ".txt");
        objetos = cargarObjetos("objetos" + numero + ".csv");
        if (visual == null || zonas == null || objetos == null) {
            return false;
        }
        calcularAnchoMapa();
        buscarInicio();
        miradosAcumulados = miradosAcumulados + mirados.length();
        mirados = "";
        intentosPuerta = 0;

        int decena = 1 + (int) (Math.random() * 9);
        int[] unidadesPares = {2, 4, 6, 8};
        int unidad = unidadesPares[(int) (Math.random() * unidadesPares.length)];
        numeroCofre = decena * 10 + unidad;

        operacionElegida = (int) (Math.random() * operaciones.length);
        clave = calcularClave(operacionElegida, numeroCofre);
        if (numero == 2) {
            sortearCodigoCasa();
        }
        return true;
    }

    public static void calcularAnchoMapa() {
        anchoMapa = 0;
        for (int i = 0; i < visual.length; i++) {
            if (visual[i].length > anchoMapa) {
                anchoMapa = visual[i].length;
            }
        }
    }

    public static void buscarInicio() {
        fila = 1;
        columna = 1;
        for (int i = 0; i < visual.length; i++) {
            for (int j = 0; j < visual[i].length; j++) {
                if (visual[i][j] == '@') {
                    fila = i;
                    columna = j;
                    visual[i][j] = ' ';
                }
            }
        }
    }

    public static void dibujarPantalla(String[] panel) {
        System.out.println();
        System.out.println(" +============ ESCAPE: LA HABITACION ============+");
        System.out.println();
        int total = Math.max(visual.length, panel.length);
        for (int i = 0; i < total; i++) {
            String izquierda = (i < visual.length) ? filaVisual(i) : repetir(' ', anchoMapa);
            String derecha = (i < panel.length) ? panel[i] : "";
            System.out.println(" " + izquierda + "   " + derecha);
        }
        System.out.println();
        System.out.println(" " + mensaje);
    }

    public static String filaVisual(int i) {
        String linea = "";
        for (int j = 0; j < anchoMapa; j++) {
            if (i == fila && j == columna) {
                linea = linea + '@';
            } else if (j < visual[i].length) {
                linea = linea + visual[i][j];
            } else {
                linea = linea + ' ';
            }
        }
        return linea;
    }

    public static String[] panelEstado() {
        int lineasInventario = Math.max(1, totalInventario);
        String[] contenido = new String[6 + lineasInventario];
        contenido[0] = nombreJugador + " - " + nombreDificultad();
        contenido[1] = "Habitacion " + habitacionActual + " de " + TOTAL_HABITACIONES;
        contenido[2] = "Objetos mirados: " + mirados.length() + " de " + objetos.length;
        if (limiteMovimientos > 0) {
            contenido[3] = "Movimientos: " + movimientos + "/" + limiteMovimientos;
        } else {
            contenido[3] = "Movimientos: " + movimientos;
        }
        if (pistasRestantes < 0) {
            contenido[4] = "Pistas: ilimitadas";
        } else {
            contenido[4] = "Pistas: " + pistasRestantes;
        }
        contenido[5] = "Inventario:";
        if (totalInventario == 0) {
            contenido[6] = "  (vacio)";
        } else {
            for (int i = 0; i < totalInventario; i++) {
                contenido[6 + i] = "  - " + inventario[i];
            }
        }

        String[] caja = enmarcar(contenido);
        String[] panel = new String[caja.length + 4];
        for (int i = 0; i < caja.length; i++) {
            panel[i] = caja[i];
        }
        panel[caja.length] = "";
        panel[caja.length + 1] = " Moverse: W A S D";
        panel[caja.length + 2] = " Pista: H | Rendirse: Q";
        panel[caja.length + 3] = " Musica: M | Efectos: E";
        return panel;
    }

    public static boolean mover(String comando) {
        boolean termino = ejecutarMovimiento(comando);
        if (!termino && limiteMovimientos > 0 && movimientos >= limiteMovimientos) {
            mostrarDerrota("Se acabaron los " + limiteMovimientos
                    + " movimientos. La habitacion te atrapa para siempre, " + nombreJugador + ".");
            return true;
        }
        return termino;
    }

    public static boolean ejecutarMovimiento(String comando) {
        int nuevaFila = fila;
        int nuevaColumna = columna;
        if (comando.equals("W")) {
            nuevaFila--;
        } else if (comando.equals("S")) {
            nuevaFila++;
        } else if (comando.equals("D")) {
            nuevaColumna++;
        } else if (comando.equals("A")) {
            nuevaColumna--;
        } else {
            mensaje = "No entiendo '" + comando + "'. Usa W A S D (H = pista, Q = rendirse).";
            return false;
        }
        movimientos++;

        char casilla = casillaZona(nuevaFila, nuevaColumna);
        if (casilla == '.') {
            fila = nuevaFila;
            columna = nuevaColumna;
            mensaje = "Caminas hacia " + nombreDireccion(comando) + ".";
        } else if (casilla >= 'A' && casilla <= 'Z') {
            return preguntarObjeto(casilla);
        } else {
            Sonido.golpe();
            mensaje = "Algo solido te bloquea el paso.";
        }
        return false;
    }

    public static char casillaZona(int f, int c) {
        if (f < 0 || f >= zonas.length) {
            return '#';
        }
        if (c < 0 || c >= zonas[f].length) {
            return '#';
        }
        return zonas[f][c];
    }

    public static String nombreDireccion(String comando) {
        if (comando.equals("W")) return "arriba";
        if (comando.equals("S")) return "abajo";
        if (comando.equals("D")) return "la derecha";
        return "la izquierda";
    }

    public static boolean preguntarObjeto(char simbolo) {
        String[] objeto = buscarObjeto(simbolo);
        if (objeto == null) {
            mensaje = "Hay un objeto sin registrar ('" + simbolo + "'). Revisen el CSV.";
            return false;
        }

        dibujarPantalla(panelObjeto(objeto));
        int opcion = leerOpcion();
        if (opcion == 2) {
            mensaje = "Decides seguir explorando.";
            return false;
        }

        if (habitacionActual == 2) {
            return interactuarCasa(simbolo, objeto);
        }
        if (habitacionActual == 3) {
            return interactuarSotano(simbolo, objeto);
        }

        if (simbolo == 'A') {
            abrirEscena(objeto);
            recordarMirado(simbolo);
            if (!tieneObjeto("llave oxidada")) {
                guardarObjeto("llave oxidada");
                Sonido.recoger();
                mensaje = "Guardaste la LLAVE OXIDADA en tu inventario.";
            } else {
                mensaje = "Bajo la alfombra ya solo queda polvo.";
            }
            return false;
        }

        if (simbolo == 'C') {
            if (!tieneObjeto("llave oxidada")) {
                abrirEscena(objeto);
                Sonido.error();
                mensaje = "El candado del cofre no cede. Algo debe abrirlo...";
            } else {
                Sonido.exito();
                mostrarEscena("el cofre abierto", "cofre_abierto",
                        "Usas la llave oxidada y el candado cae. CLIC. Grabado en la tapa esta el numero "
                        + numeroCofre + ".");
                esperarEnter();
                recordarMirado(simbolo);
                mensaje = "El cofre revelo su numero: " + numeroCofre + ".";
            }
            return false;
        }

        if (simbolo == 'N') {
            mostrarEscena(objeto[1], objeto[2],
                    objeto[3] + " Hoy la operacion es: " + operaciones[operacionElegida] + ".");
            esperarEnter();
            recordarMirado(simbolo);
            mensaje = "Miraste " + objeto[1] + ".";
            return false;
        }

        if (simbolo == 'P') {
            return abrirPuerta(objeto);
        }
        abrirEscena(objeto);
        recordarMirado(simbolo);
        mensaje = "Miraste " + objeto[1] + ". Sigues frente al objeto.";
        return false;
    }

    public static String[] panelObjeto(String[] objeto) {
        String[] mini = leerLineas("arte/mini_" + objeto[2] + ".txt");
        if (mini == null) {
            mini = new String[] { "( sin dibujo )" };
        }

        String[] contenido = new String[mini.length + 2];
        for (int i = 0; i < mini.length; i++) {
            contenido[i] = mini[i];
        }
        contenido[mini.length] = "";
        contenido[mini.length + 1] = centrar(objeto[1].toUpperCase(), ANCHO_PANEL - 2);

        String[] caja = enmarcar(contenido);
        String[] panel = new String[caja.length + 4];
        for (int i = 0; i < caja.length; i++) {
            panel[i] = caja[i];
        }
        panel[caja.length] = "";
        panel[caja.length + 1] = " Que haces?";
        panel[caja.length + 2] = "   1. Mirar de cerca";
        panel[caja.length + 3] = "   2. Seguir explorando";
        return panel;
    }

    public static String[] buscarObjeto(char simbolo) {
        for (int i = 0; i < objetos.length; i++) {
            if (objetos[i][0].charAt(0) == simbolo) {
                return objetos[i];
            }
        }
        return null;
    }

    public static void abrirEscena(String[] objeto) {
        mostrarEscena(objeto[1], objeto[2], objeto[3]);
        esperarEnter();
    }

    public static void esperarEnter() {
        System.out.println(centrar("[ ENTER para volver a la habitacion ]", ANCHO_ESCENA));
        System.out.println(bordeEscena());
        leerLinea();
    }

    public static void mostrarEscena(String titulo, String archivo, String texto) {
        System.out.println();
        System.out.println(bordeEscena());
        System.out.println();
        System.out.println(centrar("~ " + titulo.toUpperCase() + " ~", ANCHO_ESCENA));
        System.out.println();
        imprimirArte("arte/" + archivo + ".txt");
        System.out.println();
        String[] lineas = partirTexto(texto, 48);
        for (int i = 0; i < lineas.length; i++) {
            System.out.println(centrar(lineas[i], ANCHO_ESCENA));
        }
        System.out.println();
    }

    public static boolean abrirPuerta(String[] objeto) {
        mostrarEscena(objeto[1], objeto[2], objeto[3]);
        recordarMirado('P');
        if (dificultad == 3) {
            System.out.println(centrar("Intentos restantes: " + (INTENTOS_DIFICIL - intentosPuerta),
                    ANCHO_ESCENA));
        }
        System.out.print(centrar("Clave (solo ENTER para alejarte): ", ANCHO_ESCENA));
        String intento = leerLinea().trim();
        if (intento.equals(clave)) {
            Sonido.exito();
            if (habitacionActual >= habitacionFinal) {
                mostrarVictoria();
                return true;
            }
            return siguienteHabitacion();
        }
        if (intento.isEmpty()) {
            mensaje = "Te alejas de la puerta sin intentar nada.";
        } else {
            Sonido.error();
            intentosPuerta++;
            if (dificultad == 3) {
                int quedan = INTENTOS_DIFICIL - intentosPuerta;
                if (quedan <= 0) {
                    mostrarDerrota("El teclado emite un pitido largo y se bloquea para siempre. Gastaste los "
                            + INTENTOS_DIFICIL + " intentos, " + nombreJugador + ".");
                    return true;
                }
                mensaje = "CLAVE INCORRECTA. Te " + (quedan == 1 ? "queda 1 intento." : "quedan " + quedan + " intentos.");
            } else {
                mensaje = "CLAVE INCORRECTA. El teclado parpadea en silencio.";
            }
        }
        return false;
    }

    public static void mostrarVictoria() {
        System.out.println();
        System.out.println(bordeEscena());
        System.out.println();
        imprimirArte("arte/victoria.txt");
        System.out.println();
        if (tipoJuego == 2) {
            System.out.println(centrar("Escapaste de la habitacion " + habitacionFinal
                    + ". Eres libre, " + nombreJugador + ".", ANCHO_ESCENA));
        } else {
            System.out.println(centrar("Escapaste de las " + TOTAL_HABITACIONES
                    + " habitaciones. Eres libre, " + nombreJugador + ".", ANCHO_ESCENA));
        }
        System.out.println();
        imprimirResumen();
        System.out.println();
        System.out.println(bordeEscena());
        Sonido.detenerMusica();
        Sonido.victoria();
        finMostrado = true;
        System.out.println(centrar("[ ENTER para salir ]", ANCHO_ESCENA));
        leerLinea();
    }

    public static void imprimirResumen() {
        String movs = "" + movimientos;
        if (limiteMovimientos > 0) {
            movs = movs + " de " + limiteMovimientos;
        }
        System.out.println(centrar("--- RESUMEN ---", ANCHO_ESCENA));
        System.out.println(centrar("Jugador: " + nombreJugador + " | Modo: " + nombreModo()
                + " | Dificultad: " + nombreDificultad(), ANCHO_ESCENA));
        System.out.println(centrar("Movimientos: " + movs + " | Objetos mirados: "
                + (miradosAcumulados + mirados.length()) + " | Pistas usadas: " + pistasUsadas,
                ANCHO_ESCENA));
    }

    public static void mostrarDerrota(String motivo) {
        System.out.println();
        System.out.println(bordeEscena());
        System.out.println();
        imprimirArte("arte/derrota.txt");
        System.out.println();
        String[] lineas = partirTexto(motivo, 48);
        for (int i = 0; i < lineas.length; i++) {
            System.out.println(centrar(lineas[i], ANCHO_ESCENA));
        }
        System.out.println();
        imprimirResumen();
        System.out.println();
        System.out.println(bordeEscena());
        Sonido.detenerMusica();
        Sonido.error();
        finMostrado = true;
        System.out.println(centrar("[ ENTER para salir ]", ANCHO_ESCENA));
        leerLinea();
    }

    public static String calcularClave(int operacion, int numero) {
        if (operacion == 0) {
            return "" + (numero * 2);
        }
        if (operacion == 1) {
            return "" + (numero / 2);
        }
        if (operacion == 2) {
            return "" + (numero + 10);
        }
        if (operacion == 3) {
            return "" + (numero - 10);
        }

        String texto = "" + numero;
        String alReves = "";
        for (int i = texto.length() - 1; i >= 0; i--) {
            alReves = alReves + texto.charAt(i);
        }
        return alReves;
    }

    public static String darPista() {
        if (pistasRestantes == 0) {
            return "Ya no te quedan pistas, " + nombreJugador + ". Ahora depende de ti.";
        }
        String pista;
        if (habitacionActual == 2) {
            pista = darPistaCasa();
        } else if (habitacionActual == 3) {
            pista = darPistaSotano();
        } else {
            pista = darPistaHabitacion1();
        }
        pistasUsadas++;
        if (pistasRestantes > 0) {
            pistasRestantes--;
            pista = pista + " [Pistas que quedan: " + pistasRestantes + "]";
        }
        return pista;
    }

    public static String darPistaHabitacion1() {
        if (mirados.length() == 0) {
            return "PISTA: los objetos guardan secretos. Camina hacia uno y miralo de cerca.";
        }
        if (!tieneObjeto("llave oxidada")) {
            return "PISTA: en esta habitacion hay algo escondido DEBAJO de algo.";
        }
        if (mirados.indexOf('C') < 0) {
            return "PISTA: la llave que cargas abre algo... algo que guarda un numero.";
        }
        if (mirados.indexOf('N') < 0) {
            return "PISTA: ya tienes el numero. La nota dice que OPERACION aplicarle.";
        }
        return "PISTA: aplica la operacion de la nota al numero del cofre. El resultado abre la puerta.";
    }

    public static void easterEgg() {
        System.out.println();
        System.out.println(bordeEscena());
        System.out.println();
        imprimirArte("arte/easter_egg.txt");
        System.out.println();
        Sonido.exito();
        System.out.println(centrar("[ ENTER para volver al juego ]", ANCHO_ESCENA));
        System.out.println(bordeEscena());
        leerLinea();
        mensaje = "Encontraste la tecla secreta. Sigue escapando, " + nombreJugador + "!";
    }

    public static void sortearCodigoCasa() {
        clave = "";
        for (int i = 0; i < digitosCasa.length; i++) {
            digitosCasa[i] = 1 + (int) (Math.random() * 9);
            clave = clave + digitosCasa[i];
        }
    }

    public static boolean interactuarCasa(char simbolo, String[] objeto) {
        if (simbolo == 'S') {
            return entregarObjeto(objeto, "pilas",
                    "Metes la mano entre los cojines y sacas dos PILAS.",
                    "Entre los cojines ya solo queda polvo.");
        }
        if (simbolo == 'M') {
            return entregarObjeto(objeto, "linterna",
                    "La tomas: ahora tienes una LINTERNA.",
                    "Sobre la mesa ya solo queda el mantel.");
        }
        if (simbolo == 'D') {
            return entregarObjeto(objeto, "llave pequena",
                    "Levantas el colchon y encuentras una LLAVE PEQUENA.",
                    "Bajo el colchon ya no hay nada mas.");
        }
        if (simbolo == 'R') {
            escenaConDato(objeto, "Las agujas quedaron clavadas en las "
                    + digitosCasa[0] + " en punto.");
            return false;
        }
        if (simbolo == 'L') {
            escenaConDato(objeto, "Abres el libro por el marcapaginas: pagina "
                    + digitosCasa[1] + ".");
            return false;
        }
        if (simbolo == 'G') {
            return abrirGuardarropa(objeto);
        }
        if (simbolo == 'P') {
            return abrirSotano(objeto);
        }
        mirarObjeto(objeto);
        return false;
    }

    public static boolean entregarObjeto(String[] objeto, String item,
            String textoNuevo, String textoVacio) {
        if (!tieneObjeto(item)) {
            mostrarEscena(objeto[1], objeto[2], objeto[3] + " " + textoNuevo);
            esperarEnter();
            guardarObjeto(item);
            Sonido.recoger();
            mensaje = "Guardaste en tu inventario: " + item + ".";
            if (item.equals("pilas") || item.equals("linterna")) {
                if (tieneObjeto("pilas") && tieneObjeto("linterna")) {
                    mensaje = mensaje + " Con las pilas puestas la linterna ya funciona.";
                }
            }
        } else {
            mostrarEscena(objeto[1], objeto[2], textoVacio);
            esperarEnter();
            mensaje = textoVacio;
        }
        recordarMirado(objeto[0].charAt(0));
        return false;
    }

    public static void escenaConDato(String[] objeto, String dato) {
        mostrarEscena(objeto[1], objeto[2], objeto[3] + " " + dato);
        esperarEnter();
        recordarMirado(objeto[0].charAt(0));
        mensaje = "Miraste " + objeto[1] + ".";
    }

    public static void mirarObjeto(String[] objeto) {
        abrirEscena(objeto);
        recordarMirado(objeto[0].charAt(0));
        mensaje = "Miraste " + objeto[1] + ". Sigues frente al objeto.";
    }

    public static boolean abrirGuardarropa(String[] objeto) {
        if (!tieneObjeto("llave pequena")) {
            abrirEscena(objeto);
            Sonido.error();
            mensaje = "El guardarropa esta cerrado con llave.";
            return false;
        }
        Sonido.exito();
        mostrarEscena("el guardarropa abierto", "armario_abierto",
                "Abres la puerta con la llave pequena. Dentro cuelga un abrigo con una etiqueta bordada: NUMERO DE LA SUERTE "
                + digitosCasa[2] + ".");
        esperarEnter();
        recordarMirado('G');
        mensaje = "El guardarropa revelo un numero de la suerte.";
        return false;
    }

    public static boolean abrirSotano(String[] objeto) {
        if (!tieneObjeto("linterna") || !tieneObjeto("pilas")) {
            mostrarEscena(objeto[1], objeto[2],
                    "Levantas un poco la trampilla: abajo no se ve NADA. Bajar a oscuras es una locura. Necesitas luz.");
            esperarEnter();
            recordarMirado('P');
            Sonido.error();
            mensaje = "Sin luz no bajas. Busca algo que alumbre... y que funcione.";
            return false;
        }
        return abrirPuerta(objeto);
    }

    public static String darPistaCasa() {
        if (mirados.length() == 0) {
            return "PISTA: esta casa tiene muchos muebles. Miralos de cerca: casi todos guardan algo.";
        }
        if (!tieneObjeto("linterna") || !tieneObjeto("pilas")) {
            return "PISTA: el sotano estara oscuro. Busca algo que de luz... y lo que necesita para funcionar.";
        }
        if (mirados.indexOf('K') < 0) {
            return "PISTA: en la cocina hay un papel que explica como se forma la clave de la trampilla.";
        }
        if (mirados.indexOf('R') < 0) {
            return "PISTA: un reloj detenido guarda el primer numero.";
        }
        if (mirados.indexOf('L') < 0) {
            return "PISTA: un libro tiene un marcapaginas que senala el segundo numero.";
        }
        if (mirados.indexOf('G') < 0) {
            if (!tieneObjeto("llave pequena")) {
                return "PISTA: algo pequeno y metalico se esconde en el dormitorio.";
            }
            return "PISTA: la llave pequena abre algo que guarda el ultimo numero.";
        }
        return "PISTA: junta las tres pistas EN ORDEN (hora, pagina, numero de la suerte) y digitalas en la trampilla.";
    }

    public static boolean interactuarSotano(char simbolo, String[] objeto) {
        if (simbolo == 'T') {
            return abrirBaul(objeto);
        }
        if (simbolo == 'J') {
            return moverCaja(objeto);
        }
        if (simbolo == 'P') {
            return escaparPorVentana(objeto);
        }
        mirarObjeto(objeto);
        return false;
    }

    public static boolean abrirBaul(String[] objeto) {
        if (!tieneObjeto("llave oxidada")) {
            abrirEscena(objeto);
            Sonido.error();
            mensaje = "El candado oxidado no cede con las manos.";
            return false;
        }
        if (!tieneObjeto("palanca")) {
            Sonido.exito();
            mostrarEscena("el baul abierto", "baul_abierto",
                    "La llave oxidada de la primera habitacion encaja en el candado y cede con un CRAC. Entre trapos y clavos descansa una PALANCA de hierro.");
            esperarEnter();
            guardarObjeto("palanca");
            Sonido.recoger();
            mensaje = "Guardaste en tu inventario: palanca.";
        } else {
            mostrarEscena("el baul abierto", "baul_abierto",
                    "Ya no queda nada util entre los trapos y los clavos.");
            esperarEnter();
            mensaje = "El baul ya esta vacio.";
        }
        recordarMirado('T');
        return false;
    }

    public static boolean moverCaja(String[] objeto) {
        if (!cajaMovida) {
            cajaMovida = true;
            Sonido.golpe();
            mostrarEscena(objeto[1], objeto[2], objeto[3]
                    + " Tomas aire y la arrastras hasta dejarla justo debajo de la ventana. Ahora podrias subirte.");
            mensaje = "Arrastraste la caja bajo la ventana.";
        } else {
            mostrarEscena(objeto[1], objeto[2],
                    "La caja ya esta bajo la ventana, lista para servir de escalon.");
            mensaje = "La caja ya esta bajo la ventana.";
        }
        esperarEnter();
        recordarMirado('J');
        return false;
    }

    public static boolean escaparPorVentana(String[] objeto) {
        recordarMirado('P');
        if (!cajaMovida) {
            mostrarEscena(objeto[1], objeto[2],
                    objeto[3] + " Esta demasiado alta: no la alcanzas desde el piso.");
            esperarEnter();
            Sonido.error();
            mensaje = "Necesitas algo para subir hasta la ventana.";
            return false;
        }
        if (!tieneObjeto("palanca")) {
            mostrarEscena(objeto[1], objeto[2],
                    "Subido en la caja alcanzas el marco... pero esta atascado por la herrumbre. Empujas con las manos y no se mueve.");
            esperarEnter();
            Sonido.error();
            mensaje = "El marco esta atascado. Necesitas algo para forzarlo.";
            return false;
        }
        mostrarEscena(objeto[1], objeto[2],
                "Subes a la caja, metes la palanca en el marco y empujas con todo tu peso... CRAC! La ventana cede y el aire fresco te golpea la cara.");
        System.out.println(centrar("[ ENTER para salir por la ventana ]", ANCHO_ESCENA));
        System.out.println(bordeEscena());
        leerLinea();
        Sonido.exito();
        mostrarVictoria();
        return true;
    }

    public static String darPistaSotano() {
        if (mirados.indexOf('B') < 0) {
            return "PISTA: los barriles tienen un mensaje escrito con tiza. Leelo.";
        }
        if (!tieneObjeto("palanca")) {
            return "PISTA: para forzar la ventana necesitas una palanca. Hay un baul con candado... y ya viste uno parecido antes.";
        }
        if (!cajaMovida) {
            return "PISTA: la ventana esta muy alta. Algo grande y pesado podria servirte de escalon.";
        }
        return "PISTA: ya tienes todo: sube a la caja y fuerza la ventana.";
    }

    public static void transicion(String titulo, String archivo, String texto) {
        mostrarEscena(titulo, archivo, texto);
        System.out.println(centrar("[ ENTER para continuar ]", ANCHO_ESCENA));
        System.out.println(bordeEscena());
        leerLinea();
    }

    public static void guardarObjeto(String nombre) {
        if (totalInventario < inventario.length && !tieneObjeto(nombre)) {
            inventario[totalInventario] = nombre;
            totalInventario++;
        }
    }

    public static boolean tieneObjeto(String nombre) {
        for (int i = 0; i < totalInventario; i++) {
            if (inventario[i].equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public static void recordarMirado(char simbolo) {
        if (mirados.indexOf(simbolo) < 0) {
            mirados = mirados + simbolo;
        }
    }

    public static String bordeEscena() {
        return "+" + repetir('=', ANCHO_ESCENA - 2) + "+";
    }

    public static void imprimirArte(String ruta) {
        String[] arte = leerLineas(ruta);
        if (arte == null) {
            System.out.println(centrar("( falta el dibujo: " + ruta + " )", ANCHO_ESCENA));
            return;
        }
        int masAncha = 0;
        for (int i = 0; i < arte.length; i++) {
            if (arte[i].length() > masAncha) {
                masAncha = arte[i].length();
            }
        }
        String margen = repetir(' ', (ANCHO_ESCENA - masAncha) / 2);
        for (int i = 0; i < arte.length; i++) {
            System.out.println(margen + arte[i]);
        }
    }

    public static String[] enmarcar(String[] lineas) {
        String borde = "+" + repetir('-', ANCHO_PANEL) + "+";
        String[] caja = new String[lineas.length + 2];
        caja[0] = borde;
        for (int i = 0; i < lineas.length; i++) {
            String linea = " " + lineas[i];
            while (linea.length() < ANCHO_PANEL) {
                linea = linea + " ";
            }
            if (linea.length() > ANCHO_PANEL) {
                linea = linea.substring(0, ANCHO_PANEL);
            }
            caja[i + 1] = "|" + linea + "|";
        }
        caja[lineas.length + 1] = borde;
        return caja;
    }

    public static String centrar(String texto, int ancho) {
        int sobrante = ancho - texto.length();
        if (sobrante <= 0) {
            return texto;
        }
        return repetir(' ', sobrante / 2) + texto;
    }

    public static String repetir(char c, int veces) {
        String resultado = "";
        for (int i = 0; i < veces; i++) {
            resultado = resultado + c;
        }
        return resultado;
    }

    public static String[] partirTexto(String texto, int ancho) {
        String[] palabras = texto.split(" ");
        String[] temporal = new String[palabras.length];
        int total = 0;
        String linea = "";
        for (int i = 0; i < palabras.length; i++) {
            if (linea.isEmpty()) {
                linea = palabras[i];
            } else if (linea.length() + 1 + palabras[i].length() <= ancho) {
                linea = linea + " " + palabras[i];
            } else {
                temporal[total] = linea;
                total++;
                linea = palabras[i];
            }
        }
        temporal[total] = linea;
        total++;

        String[] lineas = new String[total];
        for (int i = 0; i < total; i++) {
            lineas[i] = temporal[i];
        }
        return lineas;
    }

    public static String leerLinea() {
        if (!lector.hasNextLine()) {
            System.out.println();
            System.out.println(" Entrada cerrada. Hasta pronto, " + nombreJugador + "!");
            Sonido.apagar();
            System.exit(0);
        }
        return lector.nextLine();
    }

    public static int leerEntero(String pregunta, int min, int max) {
        int numero = min - 1;
        while (numero < min || numero > max) {
            System.out.print(pregunta);
            String entrada = leerLinea().trim();
            try {
                numero = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                numero = min - 1;
            }
            if (numero < min || numero > max) {
                System.out.println(" Entrada invalida: escribe un numero de " + min + " a " + max + ".");
            }
        }
        return numero;
    }

    public static boolean localizarCarpetaJuego() {
        File clases = carpetaDeLasClases();
        String[] candidatas = new String[3];
        candidatas[0] = "";
        if (clases != null) {
            candidatas[1] = clases.getPath() + File.separator;
            if (clases.getParentFile() != null) {
                candidatas[2] = clases.getParentFile().getPath() + File.separator;
            }
        }
        for (int i = 0; i < candidatas.length; i++) {
            if (candidatas[i] != null && new File(candidatas[i] + "habitacion1.txt").exists()) {
                carpetaJuego = candidatas[i];
                return true;
            }
        }
        System.out.println();
        System.out.println(" No encuentro los archivos del juego (habitacion1.txt).");
        System.out.println(" Abre la terminal dentro de la carpeta escape_la_habitacion:");
        System.out.println();
        System.out.println("     cd ruta/a/escape_la_habitacion");
        System.out.println("     java Main");
        System.out.println();
        return false;
    }

    public static File carpetaDeLasClases() {
        try {
            File ubicacion = new File(Main.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI());
            if (ubicacion.isFile()) {
                return ubicacion.getParentFile();
            }
            return ubicacion;
        } catch (Exception e) {
            return null;
        }
    }

    public static String rutaJuego(String ruta) {
        return carpetaJuego + ruta;
    }

    public static int leerOpcion() {
        int opcion = -1;
        do {
            System.out.print(" > Elige (1 o 2): ");
            String entrada = leerLinea().trim();
            if (entrada.equals("1") || entrada.equals("2")) {
                opcion = Integer.parseInt(entrada);
            } else {
                System.out.println(" Entrada invalida. Intenta de nuevo.");
            }
        } while (opcion == -1);
        return opcion;
    }

    public static String[] leerLineas(String ruta) {
        String[] temporal = new String[200];
        int total = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaJuego(ruta)));
            String linea;
            while ((linea = br.readLine()) != null && total < 200) {
                temporal[total] = linea;
                total++;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("No se pudo leer " + ruta + ": " + e.getMessage());
            return null;
        }
        String[] lineas = new String[total];
        for (int i = 0; i < total; i++) {
            lineas[i] = temporal[i];
        }
        return lineas;
    }

    public static char[][] cargarMatriz(String ruta) {
        String[] lineas = leerLineas(ruta);
        if (lineas == null) {
            return null;
        }
        char[][] matriz = new char[lineas.length][];
        for (int i = 0; i < lineas.length; i++) {
            matriz[i] = lineas[i].toCharArray();
        }
        return matriz;
    }

    public static String[][] cargarObjetos(String ruta) {
        String[] lineas = leerLineas(ruta);
        if (lineas == null || lineas.length < 2) {
            System.out.println(ruta + " esta vacio o no existe.");
            return null;
        }
        String[][] tabla = new String[lineas.length - 1][];
        for (int i = 1; i < lineas.length; i++) {
            tabla[i - 1] = lineas[i].split(",");
        }
        return tabla;
    }
}
