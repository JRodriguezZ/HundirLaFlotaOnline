package elpuig.UDP;

import java.util.Scanner;

public class Tablero {
    public int tamanyo = 10;
    public Casilla[][] casillas = new Casilla[tamanyo][tamanyo];
    public int numeroNaves = 12;
    public int naves[] = new int[2];

    Tablero(){
        for (int i = 0; i < tamanyo; i++) {
            for (int j = 0; j < tamanyo; j++) {
                casillas[i][j] = new Casilla();
            }
        }
    }

    void colocarNaves(){
        for (int i = 1; i <= numeroNaves;) {
            int x = (int) (Math.random() * tamanyo);
            int y = (int) (Math.random() * tamanyo);

            if ((x >= 0 && x < tamanyo) && (y >= 0 && y < tamanyo) && (casillas[x][y].contenido.equals(" "))) {
                casillas[x][y].contenido = "0";
                i++;
            }
        }
        naves[0] = numeroNaves;
//        for (int i = 1; i <= numeroNaves;) {
//            int x = (int) (Math.random() * tamanyo);
//            int y = (int) (Math.random() * tamanyo);
//
//            if ((x >= 0 && x < tamanyo) && (y >= 0 && y < tamanyo) && (casillas[x][y].contenido.equals(" "))) {
//                casillas[x][y].contenido = "H";
//                i++;
//            }
//        }
//        naves[1] = numeroNaves;
        System.out.println("Las naves estan representadas con un 0");
    }

    void vaciarCasillas(){
        for (int i = 0; i < tamanyo; i++) {
            for (int j = 0; j < tamanyo; j++) {
                casillas[i][j].contenido = " ";
            }
        }
    }

    void mostrarNavesRestantes(){
        System.out.println();
        System.out.println("Naves restantes: " + naves[0]);
        System.out.println();
    }

    int comprobarGanador(){
//        if (naves[0] == 0) {
//            return 2;
//        } else if (naves[1] == 0) {
//            return 1;
//        } else {
//            return 0;
//        }
        return 0;
    }

}

class Casilla {
    public String contenido = " ";
}
