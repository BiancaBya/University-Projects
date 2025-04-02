import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client_UDP {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        DatagramPacket toSendPacket, toReceivePacket;

        InetAddress IPAddress = InetAddress.getByName("localhost");
        int PORT = 1234;

        byte[] dataToSend;
        byte[] receivedData = new byte[1024];

//        //read sir
//        System.out.println("Introduceti un sir de caractere: ");
//        String sir = br.readLine().trim();
//        dataToSend = sir.getBytes();
//        toSendPacket = new DatagramPacket(dataToSend, dataToSend.length, IPAddress, PORT);
//        clientSocket.send(toSendPacket);
//
//        //read poz
//        System.out.println("Introduceti pozitia: ");
//        String poz = br.readLine().trim();
//        dataToSend = poz.getBytes();
//        toSendPacket = new DatagramPacket(dataToSend, dataToSend.length, IPAddress, PORT);
//        clientSocket.send(toSendPacket);
//
//        //read lg
//        System.out.println("Introduceti lungimea: ");
//        String lg = br.readLine().trim();
//        dataToSend = lg.getBytes();
//        toSendPacket = new DatagramPacket(dataToSend, dataToSend.length, IPAddress, PORT);
//        clientSocket.send(toSendPacket);

        System.out.println("Introduceti un sir de caractere: ");
        String sir = br.readLine().trim();

        System.out.println("Introduceti pozitia: ");
        String poz = br.readLine().trim();

        System.out.println("Introduceti lungimea: ");
        String lg = br.readLine().trim();

        String message = sir + ',' + poz + ',' + lg;

        dataToSend = message.getBytes();
        toSendPacket = new DatagramPacket(dataToSend, dataToSend.length, IPAddress, PORT);
        clientSocket.send(toSendPacket);


        toReceivePacket = new DatagramPacket(receivedData, 1024);
        clientSocket.receive(toReceivePacket);

        String receivedDataString = new String(toReceivePacket.getData(), 0, toReceivePacket.getLength());
        System.out.println(receivedDataString);

        clientSocket.close();

    }

}

