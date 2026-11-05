import java.util.Scanner;

public class Mapa {

    public static final int FILAS = 5;

    public static final int COLUMNAS = 5;

    public static boolean[][] visitado = new boolean[FILAS][COLUMNAS];

    public static int fila = 2;

    public static int columna = 2;

    public static Scanner lector = new Scanner(System.in);

    public static void main(String[] args) {
        visitado[fila][columna] = true;

        System.out.println("=== EL TABLERO DEL MUNDO ===");
        System.out.println("Muevete con W, A, S, D. Escribe Q para salir.");

        boolean jugando = true;
        while (jugando) {
            dibujarMapa();
            System.out.print("> Comando (W/A/S/D/Q): ");
            String comando = lector.nextLine().trim().toUpperCase();
            if (comando.equals("Q")) {
                jugando = false;
            } else {
                procesarComando(comando);
            }
        }

        System.out.println("Exploraste " + contarVisitadas() + " de "
                + (FILAS * COLUMNAS) + " casillas. Hasta pronto!");
    }

    public static void dibujarMapa() {
        System.out.println();
        System.out.println("+-----------+");
        for (int i = 0; i < FILAS; i++) {
            System.out.print("| ");
            for (int j = 0; j < COLUMNAS; j++) {
                if (i == fila && j == columna) {
                    System.out.print("@ ");
                } else if (visitado[i][j]) {
                    System.out.print("x ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.print("|");
            if (i == 1) System.out.print("     W");
            if (i == 2) System.out.print("   A + D");
            if (i == 3) System.out.print("     S");
            System.out.println();
        }
        System.out.println("+-----------+");
    }

    public static void procesarComando(String comando) {
        int nuevaFila = fila;
        int nuevaColumna = columna;

        if (comando.equals("W")) {
            nuevaFila = fila - 1;
        } else if (comando.equals("S")) {
            nuevaFila = fila + 1;
        } else if (comando.equals("D")) {
            nuevaColumna = columna + 1;
        } else if (comando.equals("A")) {
            nuevaColumna = columna - 1;
        } else {
            System.out.println("Comando invalido. Usa W, A, S, D o Q.");
            return;
        }

        if (dentroDelTablero(nuevaFila, nuevaColumna)) {
            fila = nuevaFila;
            columna = nuevaColumna;
            visitado[fila][columna] = true;
        } else {
            System.out.println("Un muro invisible te detiene: no puedes salir del mapa.");
        }
    }

    public static boolean dentroDelTablero(int f, int c) {
        return f >= 0 && f < FILAS && c >= 0 && c < COLUMNAS;
    }

    public static int contarVisitadas() {
        int total = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (visitado[i][j]) {
                    total++;
                }
            }
        }
        return total;
    }
}