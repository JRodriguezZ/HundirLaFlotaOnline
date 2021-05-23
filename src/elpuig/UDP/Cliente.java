package elpuig.UDP;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    private int port;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int contador;
    private List<Integer> dades;
    private MulticastSocket multisocket;
    private String nom;
    private Jugada j;

    private HundirLaFlota hundirLaFlota;

    Scanner scanner = new Scanner(System.in);

    public Cliente() throws IOException {
        port = 5557;
        multisocket = new MulticastSocket(5557);
        multicastIP = InetAddress.getByName("224.0.0.10");
        socket  = new MulticastSocket(port);
        contador = 0;
        dades = new ArrayList<>();
        j = new Jugada();
    }

    public void setNom(String n) {
        nom = n;
    }

    public int getContador() {
        return contador;
    }

    public void runClient() throws IOException {
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];
        boolean continueRunning = true;
        int disparoX = -1, disparoY = -1;

        DatagramSocket socket = new DatagramSocket();
        //activem la subscripció
//        socket.joinGroup(multicastIP);


        //el client atén el port fins que decideix finalitzar
        while(continueRunning){
            //turno J1
//            boolean posicionValida = false;

            System.out.print("Introduce la coordenada vertical: ");
            disparoX = scanner.nextInt();
            System.out.print("Introduce la coordenada horizontal: ");
            disparoY = scanner.nextInt();
            System.out.println();
//            while(posicionValida == false) {
//
//                if (disparoX < 0 || disparoX > (hundirLaFlota.tablero.tamanyo-1) || disparoY < 0 || disparoY > (hundirLaFlota.tablero.tamanyo-1)){
//                    System.out.println("Posicion invalida, por favor introduce una posicion válida.");
//                } else {
//                    posicionValida = true;
//                }
//            }
            j.nom = nom;
            j.posX = disparoX;
            j.posY = disparoY;

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject(j);

            byte[] missatge = os.toByteArray();
            //creació del paquet per rebre les dades
            packet = new DatagramPacket(missatge, missatge.length, multicastIP, 5557);

            socket.send(packet);

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
//        socket.leaveGroup(multicastIP);
        socket.close();
    }

    private void getData(byte[] data, int length) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            hundirLaFlota = (HundirLaFlota) ois.readObject();
//            hundirLaFlota.map_jugadors.forEach((k,v)-> System.out.println("Intents:" + k + "->" + v));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    public void close(){
        if(socket!=null && !socket.isClosed()){
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        String jugador;

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

        //Demanem la ip del servidor i nom del jugador
        System.out.println("Nom jugador:");
        jugador = scanner.next();


        Cliente cliente = new Cliente();

        cliente.setNom(jugador);

        System.out.println("1. Jugar");
        System.out.println("2. Salir");

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
