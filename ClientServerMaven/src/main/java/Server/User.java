package Server;

import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.ArrayList;

public class User {
    String userLogin;
    String userPassword;
    boolean admin = false;
    ArrayList<JsonObject> messages = new ArrayList<>();
    Instant RegisterTime;


    User(String login, String password) {
        this.userPassword = password;
        this.userLogin = login;
        if (login.contains("admin")) this.admin = true;
    }

    public void setMessage(JsonObject message) {
        this.messages.add(message);
    }

    public ArrayList<JsonObject> getMessage() {
        return messages;
    }

    public void deleteMessage(int index) {
        this.messages.remove(index - 1);
    }

    public void setRegisterTime(Instant registerTime) {
        this.RegisterTime = registerTime;
    }

    public Instant getRegisterTime() {
        return RegisterTime;
    }
}
