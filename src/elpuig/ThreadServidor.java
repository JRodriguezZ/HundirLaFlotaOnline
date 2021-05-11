package elpuig;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadServidor implements Runnable {

    Socket clientSocket;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    Tablero tablero;
    boolean acabat;

    public ThreadServidor(Socket clientSocket, Tablero tablero) throws IOException {
        this.clientSocket = clientSocket;
        this.tablero = tablero;
        acabat = false;
        in = new ObjectInputStream(clientSocket.getInputStream());
        out= new ObjectOutputStream(clientSocket.getOutputStream());

    }

    @Override
    public void run() {
        while(!acabat) {

            try {
                tablero = (Tablero) in.readObject();
                tablero = generaResposta(tablero);
                out.writeObject(tablero);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Tablero generaResposta(Tablero tablero) {
        return tablero;
    }
}
