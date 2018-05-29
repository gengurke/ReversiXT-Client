import java.io.*;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
         int port = 7777;
         String ip = "127.0.0.1";


        System.out.println("test");


        for (int i = 0; i < args.length; i++) {
           if(args[i].equals("-i")){
             ip  = args[i+1];
            }
            if(args[i].equals("-p")){
              port  = Integer.parseInt(args[i+1]);
            }
            if(args[i].equals("-h")){
                System.out.println("-i ip");
                System.out.println("-p port");
                System.out.println("-h help");


            }




        }
            Client client = new Client();

        try {
            client.netzwerk(port,ip);
        } catch (IOException e) {
            System.out.println("no connection");
        }

        System.out.println("test");

/*


        //Einlesen des Dateinamens
        Scanner sc = new Scanner(System.in);
        String Datname = sc.next();
        Datname = "maps/" + Datname + ".map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gültige Züge
        Spiel.gueltigeZuege();

        //Heuristik
        Heuristik heuristik = new Heuristik(Spiel);
        System.out.println(heuristik);

        System.out.println("Zug: X Y ");
        //int s = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean ustein = false;
        if (Spiel.getUeberschreibsteine() > 0) {
            System.out.println("Ueberschreibstein (Ja/Nein)");

            if (sc.next().equals("Ja")) {
                ustein = true;
            }

        }

        Spiel.ganzerZug(1, x, y, ustein);
        sc.close();

*/

    }


}