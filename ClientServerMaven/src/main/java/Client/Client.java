package Client;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    public static void StartSession() throws IOException{
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("Wpisz (help, info, uptime, stop)");
            String input = sc.next();
            out.println(input);
            String resp = in.readLine();
            if (input.equals("stop")){
                System.out.println("koniec sesji");
                break;
            } else {
                System.out.println(resp);
            }
        }
    }
    public static void stopConnection() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
    }
    public static void main(String[] args) throws IOException{
        startConnection("127.0.0.1",6666);
        StartSession();
        stopConnection();
    }
}