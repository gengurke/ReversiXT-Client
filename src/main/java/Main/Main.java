package Main;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //System.out.println("test");
        Client client = new Client();

        try {
            client.netzwerk(7777);
        } catch (IOException e) {
            System.out.println("no connection");
        }

        //System.out.println("test");
    }
}