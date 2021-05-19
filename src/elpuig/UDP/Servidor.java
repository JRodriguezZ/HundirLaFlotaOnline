package elpuig.UDP;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class Servidor {

    /* Servidor Multicast que proporciona la velocitat simulada d'un cos */

    MulticastSocket socket;
    InetAddress multicastIP;
    int port;
    HundirLaFlota hundirLaFlota;

    public Servidor(int portValue, String strIp) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
    }

    public void runServer() throws IOException {
        DatagramPacket packet;
        byte[] recieveData;
        byte[] sendingData;

        hundirLaFlota.start();

        System.out.println("Server is running...");
//        while(hundirLaFlota.ganador == 0){
//            hundirLaFlota.combate();
//        }

        while(continueRunning()){

            //Recibir datos
            recieveData = new byte[1024];
            packet = new DatagramPacket(recieveData, recieveData.length);
            socket.receive(packet);

            String recieved = new String(packet.getData(), 0, packet.getLength());
            System.out.println(recieved);


            //Enviar datos
            sendingData = processData(packet.getData(), packet.getLength());
            packet = new DatagramPacket(sendingData, sendingData.length, packet.getAddress(), packet.getPort());
            socket.send(packet);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }

        }
        System.out.println("Parando servidor.");
        socket.close();
    }

    private byte[] processData(byte[] data, int length) {
        Jugada j = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream oin = new ObjectInputStream(in);
            j = (Jugada) oin.readObject();
            System.out.println("Jugada: " + j.nom + " " + j.posX + ", " + j.posY);

//            if(!tauler.map_jugadors.containsKey(j.Nom)) tauler.map_jugadors.put(j.Nom, 1);
//            else {
//                //Si el judador ja esxiteix, actualitzem la quatitat de tirades
//                int tirades = tauler.map_jugadors.get(j.Nom) + 1;
//                tauler.map_jugadors.put(j.Nom, tirades);

            // Comprovar la jugada
            
            } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean continueRunning() {
        return hundirLaFlota.tablero.comprobarGanador() == 0;
    }

    public static void main(String[] args) throws IOException {
        //Canvieu la X.X per un número per formar un IP.
        //Que no sigui la mateixa que la d'un altre company
        Servidor servidor = new Servidor(5557, "224.0.10.10");
        servidor.runServer();
        System.out.println("Adios!");

    }

}
