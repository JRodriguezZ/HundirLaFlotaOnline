package elpuig.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    int port;
    MulticastSocket socket;
    InetAddress multicastIP;
    int contador;
    List<Integer> dades;

    public void init() throws IOException {
        port = 5557;
        multicastIP = InetAddress.getByName("224.0.10.10");
        socket  = new MulticastSocket(port);
        contador = 0;
        dades = new ArrayList<>();
    }

    public void runClient() throws IOException {
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];

        //activem la subscripció
        socket.joinGroup(multicastIP);


        //el client atén el port fins que decideix finalitzar
        while(true){

            //creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);

            //espera de les dades, màxim 5 segons
            socket.setSoTimeout(5000);
            try{

                //espera de les dades
                socket.receive(packet);

                //processament de les dades rebudes i obtenció de la resposta
                getData(packet.getData(), packet.getLength());
            } catch(SocketTimeoutException e){

            }
        }

        //es cancel·la la subscripció
//        socket.leaveGroup(multicastIP);
    }

    private void getData(byte[] data, int length) {

        int rebut = ByteBuffer.wrap(data).getInt();

        dades.add(rebut);

        if (dades.size() == 5){
            int mitjana = (dades.get(0) + dades.get(1) + dades.get(2) + dades.get(3) + dades.get(4)) / 5;
            System.out.println("Mitjana entre " + dades.get(0) + " " + dades.get(1) + " "  + dades.get(2) + " "  + dades.get(3) + " "  + dades.get(4) + " = "  + mitjana);
            dades.clear();
        }

    }


    public void close(){
        if(socket!=null && !socket.isClosed()){
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.init();
        client.runClient();
    }

//    socket.joinGroup(multicast_ip);
    // bucle de comunicació
    // tasca que desenvolupa el client
    // usant DatagramPacket
//    socket.leaveGroup();

}
