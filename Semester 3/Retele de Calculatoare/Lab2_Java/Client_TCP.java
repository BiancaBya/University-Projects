import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client_TCP {

    public static void main(String[] args) {

        String address = "127.0.0.1";
        int port = 1234;

        try (Socket socket = new Socket(address, port);                                     // face socket-ul si stabileste conexiunea TCP la serverul cu ip-ul si portul
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  // citeste de la tastatura datele de trimis la server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);    // trimite mesaje la server prin fluxul de iesire al socket-ului
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // primeste datele de la server prin fluxul de intrare al socket-ului

            System.out.println("Introduceti un sir de caractere: ");
            String msg = reader.readLine();
            out.println(msg);

            System.out.println("Introduceti pozitia: ");
            msg = reader.readLine();
            out.println(msg);

            System.out.println("Introduceti lungimea: ");
            msg = reader.readLine();
            out.println(msg);


            String rez = in.readLine();
            System.out.println("Sirul rezultat este: " + rez);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

