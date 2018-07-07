package Main;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        boolean a = true, w = false, s = false, info = false; //Alpha-Beta und Zugsortierung an, Aspiration Windows aus
        int tiefe = 0, zeit = 2; //Nur fuer die Infodatei

        int port = 7777;
        String ip = "127.0.0.1";
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    ip = args[i + 1];
                    break;
                case "-p":
                    port = Integer.parseInt(args[i + 1]);
                    break;
                case "-h":
                    System.out.println("-i ip");
                    System.out.println("-p port");
                    System.out.println("-h help");
                    System.out.println("-a alphabeta aus");
                    System.out.println("-s zugsortierung aus");
                    System.out.println("-w aspiration windows an");
                    break;
                case "-d":
                    tiefe = Integer.parseInt(args[i + 1]);
                    break;
                case "-t":
                    zeit = Integer.parseInt(args[i + 1]);
                case "-a":
                    a = false;
                    break;
                case "-w":
                    w = true;
                    break;
                case "-s":
                    s = false;
                    break;
                default:
                    break;

            }
        }
        Client client = new Client(a, w, s, info, tiefe, zeit);
        try {
            client.netzwerk(port, ip);
        } catch (IOException e) {
            System.out.println("no connection");
            System.exit(0);
        }
    }


}