package Client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void startConnection(String ip, int port) throws IOException {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Witam na serwerze\nDostepne komendy:login,register,stop)");
        } catch (Exception e) {
            System.out.println("Serwer niedostępny");
        }
    }

    public static void StartSession() throws IOException {

        while (true) {
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            out.println(input);
            String resp = in.readLine();
            if (input.equals("stop")) {
                System.out.println("koniec sesji");
                break;
            } else {
                System.out.println(resp);
            }
        }
    }


    public static void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException {
        startConnection("127.0.0.1", 6666);
        StartSession();
        stopConnection();
    }
}