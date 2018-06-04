package MainPackage;

import HeuristikTest.HeuristikTestAusgabe;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //System.out.println("test");
        Client client = new Client();

        //TODO - Heuristik Testausgabe
        HeuristikTestAusgabe ausgabe = new HeuristikTestAusgabe();

        try {
            client.netzwerk(7777);
        } catch (IOException e) {
            System.out.println("no connection");
        }

        //System.out.println("test");
    }


}