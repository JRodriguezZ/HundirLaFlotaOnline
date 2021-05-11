package elpuig;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Thread {

    String hostname;
    int port;
    boolean continueConnected;
    Tablero serverData = null;
    int nomLlista = 0;

    public Cliente(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
    }

    public void run() {
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;


        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = new ObjectInputStream (socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            while(continueConnected){
                Tablero tablero = getRequest(serverData);
                out.writeObject(tablero);
                out.flush();
                serverData = (Tablero) in.readObject();

            }
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Tablero getRequest(Tablero serverData) {
//        if (serverData != null){
//            System.out.println("Llista " + serverData.getNom() + " rebuda:");
//            for(Integer i: serverData.getNumberList()){
//                System.out.println(i);
//            }
//        }

        return serverData;
    }


    private void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        Cliente cliente = new Cliente("localhost",5558);

        System.out.println(" __   __  __   __  __    _  ______   ___  ______      ___      _______    _______  ___      _______  _______  _______ ");
        System.out.println("|  | |  ||  | |  ||  |  | ||      | |   ||    _ |    |   |    |   _   |  |       ||   |    |       ||       ||   _   |");
        System.out.println("|  |_|  ||  | |  ||   |_| ||  _    ||   ||   | ||    |   |    |  |_|  |  |    ___||   |    |   _   ||_     _||  |_|  |");
        System.out.println("|       ||  |_|  ||       || | |   ||   ||   |_||_   |   |    |       |  |   |___ |   |    |  | |  |  |   |  |       |");
        System.out.println("|       ||       ||  _    || |_|   ||   ||    __  |  |   |___ |       |  |    ___||   |___ |  |_|  |  |   |  |       |");
        System.out.println("|   _   ||       || | |   ||       ||   ||   |  | |  |       ||   _   |  |   |    |       ||       |  |   |  |   _   |");
        System.out.println("|__| |__||_______||_|  |__||______| |___||___|  |_|  |_______||__| |__|  |___|    |_______||_______|  |___|  |__| |__|");
        System.out.println();
        System.out.println("Bienvenido al juego de Hundir la Flota Online");
        System.out.println();
        System.out.println("1. Jugar");
        System.out.println("2. Salir");

        Scanner scanner = new Scanner(System.in);
        int seleccion = scanner.nextInt();
        switch (seleccion){
            case 1 -> cliente.start();
            case 2 -> System.exit(1);
        }
    }
}
