package Server;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class Server2Test {

    @Test
    @DisplayName("First test with multiple assertions")
    public void assertMultipleNumbers() {
        assertEquals(3, 3);
        assertAll(
                () -> assertEquals(1, 1),
                () -> assertEquals(1, 1),
                () -> assertEquals(1, 1),
                () -> assertEquals(1, 1),
                () -> assertEquals(1, 1),
                () -> assertEquals(1, 1),
                () -> assertEquals(1, 1),
                () -> assertEquals(1, 1));
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        try {
            String LoginTestowy = "Test";
            String hasloTestowe = "haslo";
            Server2.registerLogUser("register", LoginTestowy, hasloTestowe);
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(LoginTestowy + ".json"));
            User user2 = gson.fromJson(reader, User.class);
            assertAll(
                    () -> assertEquals(LoginTestowy, user2.userLogin),
                    () -> assertEquals(hasloTestowe, user2.userPassword)
            );
        } catch (NullPointerException | IOException e) {
        }
    }

    @Test
    @DisplayName("Shoud login user")
    void shoudLoginUser() {
        try {
            String LoginTestowy = "Test";
            String hasloTestowe = "haslo";
            Server2.registerLogUser("login", LoginTestowy, hasloTestowe);
            assertTrue(Server2.userLogged == true);
        } catch (NullPointerException | IOException e) {
        }
    }

    @Test
    @DisplayName("Has not user admin permissions")
    void hasNotUserAdminPermissions() {
        try {
            String LoginTestowy = "Test";
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(LoginTestowy + ".json"));
            User user2 = gson.fromJson(reader, User.class);
            assertFalse(user2.admin);
        } catch (NullPointerException | IOException e) {
        }
    }

    @Test
    @DisplayName("has admin admin permissions")
    void hasAdminAdminPermissions() throws IOException {
        String LoginTestowy = "admin";
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(LoginTestowy + ".json"));
        User user2 = gson.fromJson(reader, User.class);
        assertTrue(user2.admin);
    }

    @Test
    @DisplayName("should throw inbox is full exception")
    void shouldThrowInboxIsFullException() throws IOException {
        Server2.sendMessage("Test", "tresc trecs tresc");
        Server2.sendMessage("Test", "tresc trecs tresc");
        Server2.sendMessage("Test", "tresc trecs tresc");
        Server2.sendMessage("Test", "tresc trecs tresc");
        Server2.sendMessage("Test", "tresc trecs tresc");
        Server2.sendMessage("Test", "tresc 6");

        assertEquals("error - recipient's inbox is full", Server2.sendMessage("Test", "tresc 6"));
    }

    @Test
    @DisplayName("Should throw message too long exception")
    void shouldThrowMessageTooLongException() throws IOException {
        assertEquals("error - message is too long", Server2.sendMessage("Test", "T1EH4FMN0PxNxQ3xN15soX53DQopkWr2UtOxZldLPw73tPXZ8LBQltDSzZt2LlA5tFuAbpTRjDknwMQPN06UFCw7uWhXJ6jnyKjTaGEhcJEkry7xkOeTybP2TL3RzkcDqvGdwUp1huhg1qMyjr5RM67id7y5JioKzU7oAgQwQJOi9OxvTxuVmvxTNWR90n1mqHtdcSRWmleiOQIQGVBjP8QouRFPz8aiKEkzrFDKGscbvnG0fqQPVoxeLmtWLSef"));
    }

    @Test
    @DisplayName("Should correctly send message to another user")
    void shouldCorrectlySendMessageToAnotherUser() throws IOException {
        int number =0;
        try {
            Server2.path = "Test.json";
            Server2.deletemsg(1);
            Server2.deletemsg(1);
            Server2.deletemsg(1);
            Server2.deletemsg(1);
            Server2.deletemsg(1);
        } catch (IndexOutOfBoundsException e) {
        }
        Server2.sendMessage("Test", "testowa wiadomosc");
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("Test.json"));
        User userTest = gson.fromJson(reader, User.class);
        number = userTest.messages.size();
        assertEquals(1,number);
    }

    @Test
    @DisplayName("Should delete chosen message")
    void shouldDeleteChosenMessage() throws IOException {
        int number = 1;
        try {
            Server2.path="Test.json";
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("Test.json"));
            User userTest = gson.fromJson(reader, User.class);
            number = userTest.messages.size();
            if (number<1){
                Server2.sendMessage("Test", "shouldDeleteThisMessage");
                System.out.println("1    "+number);
            }
            Server2.deletemsg(1);
        } catch (NullPointerException e) {
        }
        Reader reader = Files.newBufferedReader(Paths.get("Test.json"));
        Gson gson = new Gson();
        User user2 = gson.fromJson(reader, User.class);
        assertEquals(number - 1, user2.messages.size());
    }

}