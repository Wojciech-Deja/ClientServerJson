package Server;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class Jsonwritertest {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        FileWriter we = new FileWriter("staff.json",true);
        gson.toJson(123.45, we);
        we.flush();

    }
}
