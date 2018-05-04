import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
      System.out.println("test");
      Client client = new Client();
      try {

          client.netzwerk(5555);
      }catch(IOException e) {
          System.out.println("no connection");
        }





        System.out.println("test");


        //Einlesen des Dateinamens
        Scanner sc = new Scanner(System.in);
        String Datname = sc.next();
        Datname = "maps/" + Datname + ".map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        Spiel.gueltigeZuege();

        System.out.println("Zug: X Y ");
        //int s = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        //String ustein = sc.next();
        sc.close();

        Spiel.Zug(1,x,y,"Nein");
        Spiel.Faerben(1,x,y);
        Spiel.PrintSpielfeld();








    }



}