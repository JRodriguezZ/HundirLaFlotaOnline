package elpuig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    int port;
    Tablero tablero;

    public Servidor(int port, Tablero tablero) {
        this.port = port;
        this.tablero = tablero;
    }

    public void listen() {
        ServerSocket serverSocket;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                //espera connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                ThreadServidor FilServidor = new ThreadServidor(clientSocket, tablero);

                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void main(String[] args) {

        Tablero tablero = new Tablero();

        Servidor servidor = new Servidor(5558, tablero);
        servidor.listen();
    }
}
