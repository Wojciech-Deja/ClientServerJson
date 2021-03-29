package Server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Server2 {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;
    static boolean userLogged = false;
    static String path, nadawca;
    static boolean isAdmin;
    static Instant Regtime;
    Instant ServerStartTime = Instant.now();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        loop:
        while (true) {
            String input = in.readLine();
            System.out.println("log: " + input);
            if (!userLogged) {
                switch (input) {
                    case "register":
                    case "login":
                        out.println("Podaj Login");
                        String Login = in.readLine();
                        out.println("Podaj haslo");
                        String haslo = in.readLine();
                        registerLogUser(input,Login,haslo);
                        break;
                    case "stop":
                        System.out.println("koniec");
                        break loop;
                    default:
                        out.println("wrong command");
                        break;
                    case "help":
                        out.println(help());
                        break;
                }
            } else {
                switch (input) {
                    case "logout":
                        userLogged = false;
                        out.println("Log out successfully");
                        break;
                    case "help":
                        out.println(help());
                        break;
                    case "info":
                        out.println(info());
                        break;
                    case "uptime":
                        out.println(upTime(ServerStartTime));
                        break;
                    case "wyslij":
                        out.println("Podaj odbiorce: ");
                        String odbiorca = in.readLine();
                        out.println("Tresc wiadomosci: ");
                        String tresc = in.readLine();
                        out.println(sendMessage(odbiorca,tresc));
                        break;
                    case "inbox":
                        out.println(inbox());
                        break;
                    case "deletemsg":
                        out.println("Podaj indeks wiadomosci 1-5: ");
                        int indeks = Integer.parseInt(in.readLine());
                        out.println(deletemsg(indeks));
                        break;
                    default:
                        out.println("wrong command");
                        break;
                }
            }
        }
    }

    static String deletemsg(int indeks) throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        User user = gson.fromJson(reader, User.class);
        try {
            user.deleteMessage(indeks);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Liczba z poza zakresu";
        }
        FileWriter we = new FileWriter(path);
        gson.toJson(user, we);
        we.flush();
        return "Wiadomość usunięta";
    }

    static String sendMessage(String odbiorca, String tresc) throws IOException {
        if (tresc.length() < 255) {
            Gson gson = new Gson();
            JsonObject wtresc = new JsonObject();
            wtresc.addProperty("Nadawca", nadawca);
            wtresc.addProperty("tresc", tresc);
            try {
                Reader reader = Files.newBufferedReader(Paths.get(odbiorca + ".json"));
                User user = gson.fromJson(reader, User.class);
                if (user.messages.size() < 5 || user.admin) {
                    user.setMessage(wtresc);
                    FileWriter we = new FileWriter(odbiorca + ".json");
                    gson.toJson(user, we);
                    we.flush();
                    return "wiadomosc wyslana";
                } else {
                    return "error - recipient's inbox is full";
                }
            } catch (Exception e) {
                return "Nie ma takiego użytkownika";
            }
        } else {
            return "error - message is too long";
        }
    }

    ArrayList<JsonObject> inbox() throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        User user = gson.fromJson(reader, User.class);
        return user.getMessage();
    }

    public static JsonObject upTime(Instant input) {
        Instant endTime = Instant.now();
        Duration timeElapsed = Duration.between(input, endTime);
        int days = (int) (timeElapsed.toSeconds() / 86400);
        int hours = (int) (timeElapsed.toSeconds() / 3600) - days * 24;
        int minutes = (int) (timeElapsed.toSeconds() / 60) - hours * 60;
        int seconds = (int) timeElapsed.toSeconds() - minutes * 60;
        JsonObject upTime = new JsonObject();
        if (isAdmin) {
            upTime.addProperty("Days", days);
            upTime.addProperty("Hours", hours);
            upTime.addProperty("Minutes", minutes);
            upTime.addProperty("Seconds", seconds);
        } else {
            upTime.addProperty("Error", "Brak uprawnień");
        }
        return upTime;
    }

    JsonObject info() {
        Date myDate = Date.from(ServerStartTime);
        JsonObject info = new JsonObject();
        info.addProperty("Login", nadawca);
        info.addProperty("RegistrationDate", String.valueOf(Regtime));
        if (isAdmin) {
            info.addProperty("Admin", "yes");
            info.addProperty("ServerStartDate", String.valueOf(myDate));
        } else {
            info.addProperty("Admin", "no");
        }

        return info;
    }

    JsonObject help() {
        JsonObject help = new JsonObject();
        if (userLogged) {
            if (isAdmin) {
                help.addProperty("uptime", "zwraca czas życia serwera");
//                help.addProperty("passreset", "pozwala zmienic hasło usera");
            }
            help.addProperty("help", "zwraca listę dostępnych komend z krótkim opisem");
            help.addProperty("info", "informacje o koncie");
            help.addProperty("wyslij", "wyslij wiadomość");
            help.addProperty("inbox", "pokazuje wiadomosci przychodzące");
            help.addProperty("deletemsg", "pozwala usunąć wiadomość");
            help.addProperty("logout", "pozwala na wylogowanie");
//            help.addProperty("deleateacc", "usun konto");
        } else {
            help.addProperty("Register", "informacje o koncie");
            help.addProperty("login", "informacje o koncie");
            help.addProperty("stop", "zakancza sesje");
        }
        return help;
    }

    static void registerLogUser(String type,String Login,String haslo) throws IOException {
        User user = new User(Login, haslo);
        Gson gson = new Gson();
        nadawca = Login;
        path = Login + ".json";
        if (type.equals("register")) {
            Instant RegisterTime = Instant.now();
            user.setRegisterTime(RegisterTime);
            FileWriter we = new FileWriter(path);
            gson.toJson(user, we);
            we.flush();
            out.println("New user created");
        } else if (type.equals("login")) {
            try {
                Reader reader = Files.newBufferedReader(Paths.get(path));
                user = gson.fromJson(reader, User.class);
                if (haslo.equals(user.userPassword)) {
                    out.println("Logged in correctly");
                    userLogged = true;
                    isAdmin = user.admin;
                    Regtime = user.getRegisterTime();
                } else {
                    out.println("Wrong password");
                }

                reader.close();
            } catch (Exception e) {
                out.println("wrong username");
            }
        }
    }

    public static void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        Server2 server = new Server2();
        server.start(6666);
        stop();
    }
}
