package elpuig.UDP;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    int port;
    MulticastSocket socket;
    InetAddress multicastIP;
    int contador;
    List<Integer> dades;

    public Cliente() throws IOException {
        port = 5557;
        multicastIP = InetAddress.getByName("224.0.10.10");
        socket  = new MulticastSocket(port);
        contador = 0;
        dades = new ArrayList<>();
    }

    public void runClient() throws IOException {
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];
        boolean continueRunning = true;

        //activem la subscripció
        socket.joinGroup(multicastIP);


        //el client atén el port fins que decideix finalitzar
        while(continueRunning){

            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);

            //espera de les dades, màxim 5 segons
            socket.setSoTimeout(10000);
            try{

                //espera de les dades
                socket.receive(packet);

                //processament de les dades rebudes i obtenció de la resposta
                getData(packet.getData(), packet.getLength());
            } catch(SocketTimeoutException e){
                System.out.println("S'ha perdut la connexió amb el servidor.");
                continueRunning = false;
            }
        }

        //es cancel·la la subscripció
        socket.leaveGroup(multicastIP);
        socket.close();
    }

    private void getData(byte[] data, int length) {


    }


    public void close(){
        if(socket!=null && !socket.isClosed()){
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente();

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
            case 1 -> cliente.runClient();
            case 2 -> System.exit(1);
        }
    }

//    socket.joinGroup(multicast_ip);
    // bucle de comunicació
    // tasca que desenvolupa el client
    // usant DatagramPacket
//    socket.leaveGroup();

}
