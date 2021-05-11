package elpuig.UDP;

import java.util.Scanner;

public class Tablero {
    public int tamanyo = 10;
    public Casilla[][] caselles = new Casilla[tamanyo][tamanyo];
    public int numeroNaves = 0;
    public int naves[] = new int[2];

    Tablero(){
        for (int i = 0; i < tamanyo; i++) {
            for (int j = 0; j < tamanyo; j++) {
                caselles[i][j] = new Casilla();
            }
        }
    }

    void colocarNaves(){
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Con cuantas naves quieres jugar? (min 1 / max 50)");
        boolean cantNavesValida = false;
        while(cantNavesValida == false) {
            numeroNaves = scanner.nextInt();
            if (numeroNaves < 1 || numeroNaves > 50){
                System.out.println("Introduce un numero válido por favor.");
            } else {
                cantNavesValida = true;
            }

        }
        for (int i = 1; i <= numeroNaves;) {
            int x = (int) (Math.random() * tamanyo);
            int y = (int) (Math.random() * tamanyo);

            if ((x >= 0 && x < tamanyo) && (y >= 0 && y < tamanyo) && (caselles[x][y].contingut.equals(" "))) {
                caselles[x][y].contingut = "0";
                i++;
            }
        }
        naves[0] = numeroNaves;
        for (int i = 1; i <= numeroNaves;) {
            int x = (int) (Math.random() * tamanyo);
            int y = (int) (Math.random() * tamanyo);

            if ((x >= 0 && x < tamanyo) && (y >= 0 && y < tamanyo) && (caselles[x][y].contingut.equals(" "))) {
                caselles[x][y].contingut = "H";
                i++;
            }
        }
        naves[1] = numeroNaves;
        System.out.println("Tus naves estan representadas con un 0");
        System.out.println("Las naves enemigas estan representadas con una H");
    }

    void limpiarCasillas(){
        for (int i = 0; i < tamanyo; i++) {
            for (int j = 0; j < tamanyo; j++) {
                caselles[i][j].contingut = " ";
            }
        }
    }

    void mostrarNavesRestantes(){
        System.out.println();
        System.out.println("Naves restantes del J1: " + naves[0] + ". Naves restantes del J2: " + naves[1]);
        System.out.println();
    }

    int comprobarGanador(){
        if (naves[0] == 0){
            return 2;
        } else if (naves[1] == 0) {
            return 1;
        } else {
            return 0;
        }
    }

}

class Casilla {
    public String contingut = " ";
}
