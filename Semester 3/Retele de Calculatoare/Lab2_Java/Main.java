// Problema 7
// Un client trimite unui server un sir de caractere si doua numere (fie acestea s, i, l).
// Serverul va returna clientului subsirul de lungime l a lui s care incepe la pozitia i.

//client trimite la server prin TCP si serverul trimite la client prin UDP


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        String address = "127.0.0.1";

        try (Socket TCP_socket = new Socket(address, 1234);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(TCP_socket.getOutputStream(), true);
             DatagramSocket UDP_socket = new DatagramSocket()) {

            System.out.println("Introduceti un sir de caractere: ");
            String msg = reader.readLine();
            out.println(msg);

            System.out.println("Introduceti pozitia: ");
            msg = reader.readLine();
            out.println(msg);

            System.out.println("Introduceti lungimea: ");
            msg = reader.readLine();
            out.println(msg);

            int UDP_port = UDP_socket.getLocalPort();
            out.println(UDP_port);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            UDP_socket.receive(packet);
            String rez = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Sirul rezultat este: " + rez);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}



