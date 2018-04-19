import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //Einlesen des Dateinamens
        Scanner sc = new Scanner(System.in);
        String Datname = sc.next();
        Datname = "maps/" + Datname + ".txt";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);
        System.out.println("Spieler X Y Überschreibstein(Ja oder Nein)");
        int s = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        String ustein = sc.next();
        sc.close();


        Spiel.gueltigeZuege(Spiel.getHoehe(),Spiel.getBreite(),Spiel.getSpielfeld());

        if(Spiel.Zug(s,x,y, ustein)) {
            System.out.println("Gültiger Zug");
            Spiel.PrintSpielfeld();
        } else {
            System.out.println("Ungültiger Zug");
        }

    }
}
