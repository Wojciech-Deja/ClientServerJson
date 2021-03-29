//package Server;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Date;
//
//public class Server {
//    private static ServerSocket serverSocket;
//    private static Socket clientSocket;
//    private static PrintWriter out;
//    private static BufferedReader in;
//
//    public void start(int port) throws IOException {
//        boolean isLogged = false;
//        boolean isAdmin = false;
//        String Login ="33", haslo="n";
//
//        Instant ServerStartTime =  Instant.now();
//        serverSocket = new ServerSocket(port);
//        clientSocket = serverSocket.accept();
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//                while (true){
//            String input = in.readLine();
//            System.out.println(isLogged + "log: "+input);
//
//
//
//            while (isLogged){
//                if (isAdmin) {
//                    switch (input) {
//                        case "info":
//                            Date myDate = Date.from(ServerStartTime);
//                            JsonObject info = new JsonObject();
//                            info.addProperty("serverVersion", "0.1");
//                            info.addProperty("creationDate", String.valueOf(myDate));
//                            info.addProperty("Admin", "yes");
//                            out.println(info);
//                            input = in.readLine();
//                            break;
//                        case "uptime":
//                            upTime(ServerStartTime);
//                            input = in.readLine();
//                            break;
//                        case "help":
//                            JsonObject help = new JsonObject();
//                            help.addProperty("uptime", "zwraca czas życia serwera");
//                            help.addProperty("info", "zwraca numer wersji serwera, datę jego utworzenia");
//                            help.addProperty("help", "zwraca listę dostępnych komend z krótkim opisem");
//                            help.addProperty("stop", "zatrzymuje jednocześnie serwer i klienta");
//                            out.println(help);
//                            input = in.readLine();
//                            break;
//                        case "logout":
//                            isLogged = false;
//                            isAdmin = false;
//                            out.println("User logged out");
//                            break;
//                        case "inbox":
//                            //pokaz wiadomosci
//
//
//                            //wyslij
//                            // usun
//                            break;
//                        case "wyslij":
//                            String nadawca = Login;
//                            out.println("Podaj odbiorce: ");
//                            String odbiorca = in.readLine();
//                            out.println("Tresc wiadomosci: ");
//                            String tresc = in.readLine();
//                            Gson gson = new Gson();
//                            JsonObject wtresc = new JsonObject();
//                            wtresc.addProperty("Nadawca", nadawca);
//                            wtresc.addProperty("odbiorca", odbiorca);
//                            wtresc.addProperty("tresc", tresc);
//                            User user = new User(Login, haslo);
//                            user.setMessage(wtresc);
//
//                            FileWriter we = new FileWriter(Login+".json");
//                            gson.toJson(user,we);
//                            we.flush();
//                            out.println("wiadomosc wyslana" + user.getMessage());
//                            input = in.readLine();
//                            break;
//
//                    }
//                }else{
//                    switch (input) {
//                        case "help":
//                            JsonObject help = new JsonObject();
//                            help.addProperty("info", "informacje o serwerze");
//                            help.addProperty("logout", "pozwala na wylogowanie");
//                            help.addProperty("help", "zwraca listę dostępnych komend z krótkim opisem");
//                            out.println(help);
//                            input = in.readLine();
//                            break;
//                        case "logout":
//                            isLogged = false;
//                            isAdmin = false;
//                            out.println("User logged out");
//                            break;
//                        case "info":
//                            Date myDate = Date.from(ServerStartTime);
//                            JsonObject info = new JsonObject();
//                            info.addProperty("Login", "0.1");
//                            info.addProperty("creationDate", String.valueOf(myDate));
//                            info.addProperty("Admin", "no");
//                            out.println(info);
//                            input = in.readLine();
//                            break;
//
//                        case "wyslij":
//                            out.println("Podaj odbiorce: ");
//                            String odbiorca = in.readLine();
//                            out.println("Tresc wiadomosci: ");
//                            String tresc = in.readLine();
//                            Gson gson = new Gson();
//                            JsonObject wtresc = new JsonObject();
//                            wtresc.addProperty("Nadawca", Login);
//                            wtresc.addProperty("odbiorca", odbiorca);
//                            wtresc.addProperty("tresc", tresc);
//                            String ss = odbiorca+".json";
//                            Reader reader = Files.newBufferedReader(Paths.get(ss));
//                            User user = gson.fromJson(reader,User.class);
//                            user.setMessage(wtresc);
//                            FileWriter we = new FileWriter(ss);
//                            gson.toJson(user,we);
//                            we.flush();
//                            out.println("wiadomosc wyslana" + user.getMessage());
//                            input = in.readLine();
//                            break;
//                        case "inbox":
//                            gson = new Gson();
//                            ss = Login+".json";
//                            reader = Files.newBufferedReader(Paths.get(ss));
//                            user = gson.fromJson(reader,User.class);
//                            out.println(user.getMessage());
//                            input = in.readLine();
//                            break;
//                    }
//                }
//            }
//
////            if (input.equals("stop")){
////                System.out.println("koniec");
////                break;
////            }
////                    if (!isLogged){
////                        switch (input) {
////                            case "register":
////                                out.println("Podja Login");
////                                Login = in.readLine();
////                                out.println("Podja haslo");
////                                haslo = in.readLine();
////                                User user = new User(Login, haslo);
////                                Gson gson = new Gson();
////                                FileWriter we = new FileWriter(Login+".json");
////                                gson.toJson(user,we);
////                                we.flush();
////                                out.println("New user created");
////                                break;
////                            case "login":
////                                out.println("Podja Login");
////                                Login = in.readLine();
////                                out.println("Podja haslo");
////                                haslo = in.readLine();
////                                gson = new Gson();
////                                String ss = Login+".json";
////                                Reader reader = Files.newBufferedReader(Paths.get(ss));
////                                user = gson.fromJson(reader,User.class);
////                                if(haslo.equals(user.userPassword)){
////                                    isLogged = true;
////                                    if(user.admin) {
////                                        isAdmin=true;
////                                        out.println("Login successful 1.help 2.info 3.inbox 4.logout 5.uptime 6.wyslij");
////                                    }else {
////                                        out.println("Login successful 1.help 2.info 3.inbox 4.logout 6.wyslij");
////                                    }
////                                }else {
////                                    out.println("Wrong password");
////                                }
////                                reader.close();
////                                break;
////                        }
////                    }
////
////        }
////    }
//
//        public static void upTime(Instant input){
//        Instant endTime = Instant.now() ;
//        Duration timeElapsed = Duration.between(input, endTime);
//        int days = (int)(timeElapsed.toSeconds()/86400);
//        int hours = (int)(timeElapsed.toSeconds()/3600)-days*24;
//        int minutes = (int)(timeElapsed.toSeconds()/60)-hours*60;
//        int seconds= (int)timeElapsed.toSeconds()-minutes*60;
//            JsonObject upTime = new JsonObject();
//            upTime.addProperty("Days", days);
//            upTime.addProperty("Hours", hours);
//            upTime.addProperty("Minutes", minutes);
//            upTime.addProperty("Seconds", seconds);
//            out.println(upTime);
//     }
//
//    public static void stop() throws IOException{
//        in.close();
//        out.close();
//        clientSocket.close();
//        serverSocket.close();
//    }
//
//    public static void main(String[] args) throws IOException{
//        Server server=new Server();
//        server.start(6666);
//        stop();
//    }
//}