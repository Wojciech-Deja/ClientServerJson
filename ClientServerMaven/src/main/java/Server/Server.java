package Server;
import com.google.gson.JsonObject;

import java.net.*;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class Server {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public void start(int port) throws IOException {
        Instant ServerStartTime =  Instant.now();
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true){
            String input = in.readLine();
            System.out.println("log: "+input);
            if (input.equals("stop")){
                System.out.println("koniec");
                break;
            }
            switch (input) {
                case "info":
        Date myDate = Date.from(ServerStartTime);
        JsonObject info = new JsonObject();
        info.addProperty("serverVersion", "0.1");
        info.addProperty("creationDate", String.valueOf(myDate));
        out.println(info);

                    break;
                case "uptime":
                    upTime(ServerStartTime);
                    break;
                case "help":
                    JsonObject help = new JsonObject();
                    help.addProperty("uptime", "zwraca czas życia serwera");
                    help.addProperty("info", "zwraca numer wersji serwera, datę jego utworzenia");
                    help.addProperty("help", "zwraca listę dostępnych komend z krótkim opisem");
                    help.addProperty("stop", "zatrzymuje jednocześnie serwer i klienta");
                    out.println(help);
                    break;
            }
        }
    }

        public static void upTime(Instant input){
        Instant endTime = Instant.now() ;
        Duration timeElapsed = Duration.between(input, endTime);
        int days = (int)(timeElapsed.toSeconds()/86400);
        int hours = (int)(timeElapsed.toSeconds()/3600)-days*24;
        int minutes = (int)(timeElapsed.toSeconds()/60)-hours*60;
        int seconds= (int)timeElapsed.toSeconds()-minutes*60;
            JsonObject upTime = new JsonObject();
            upTime.addProperty("Days", days);
            upTime.addProperty("Hours", hours);
            upTime.addProperty("Minutes", minutes);
            upTime.addProperty("Seconds", seconds);
            out.println(upTime);
     }

    public static void stop() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException{
        Server server=new Server();
        server.start(6666);
        stop();
    }
}