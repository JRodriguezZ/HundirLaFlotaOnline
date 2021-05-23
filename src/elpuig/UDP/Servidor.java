package elpuig.UDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class Servidor {

    /* Servidor Multicast que proporciona la velocitat simulada d'un cos */

    MulticastSocket socket;
    MulticastSocket multisocket;
    InetAddress multicastIP;
    int port, acabats, multiport = 5557;
    boolean fi;
    HundirLaFlota hundirLaFlota = new HundirLaFlota();

    public Servidor(int portValue, String strIp) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        multisocket = new MulticastSocket(multiport);
        port = portValue;
    }

    public void runServer() throws IOException {
        byte[] recieveData = new byte[1024];
        byte[] sendingData;

        hundirLaFlota.start();

        System.out.println("Server is running...");

//        acabats < hundirLaFlota.mapJugadors.size() || acabats == 0
        while (true) {

            //Recibir datos
            DatagramPacket packet = new DatagramPacket(recieveData, recieveData.length);
            socket.receive(packet);


            String recieved = new String(packet.getData(), 0, packet.getLength());
            System.out.println(recieved);


            //Enviar datos
            sendingData = processData(packet.getData(), packet.getLength());
            packet = new DatagramPacket(sendingData, sendingData.length, packet.getAddress(), packet.getPort());
            socket.send(packet);

            DatagramPacket multipacket = new DatagramPacket(sendingData, sendingData.length, multicastIP, multiport);
            multisocket.send(multipacket);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }

        }
//        System.out.println("Parando servidor.");
//        socket.close();
    }

    private byte[] processData(byte[] data, int length) {
        Jugada j = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream oin = new ObjectInputStream(in);
            j = (Jugada) oin.readObject();
            System.out.println("Jugada: " + j.nom + " " + j.posX + ", " + j.posY);

            if(!hundirLaFlota.mapJugadors.containsKey(j.nom)) hundirLaFlota.mapJugadors.put(j.nom, 1);
            else {
                //Si el judador ja esxiteix, actualitzem la quatitat de tirades
                int tirades = hundirLaFlota.mapJugadors.get(j.nom) + 1;
                hundirLaFlota.mapJugadors.put(j.nom, tirades);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //comprobar la jugada
        fi = continueRunning();
        if(continueRunning()) {
            //augmentem la quantitat de jugadors que l'han encertat/acabat
            acabats++;
            hundirLaFlota.acabats++;
        }

        //La resposta és el tauler amb les dades de tots els jugadors
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(hundirLaFlota.combate(j.posX, j.posY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] resposta = os.toByteArray();
        return resposta;

    }

    private boolean continueRunning() {
        return hundirLaFlota.tablero.comprobarGanador() == 0;
    }

    public static void main(String[] args) throws IOException {
        //Canvieu la X.X per un número per formar un IP.
        //Que no sigui la mateixa que la d'un altre company
        Servidor servidor = new Servidor(5557, "224.0.0.10");
        servidor.runServer();
        System.out.println("Adios!");

    }

}
