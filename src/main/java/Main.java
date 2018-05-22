import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //Einlesen des Dateinamens
        Scanner sc = new Scanner(System.in);
        String Datname = sc.next();
        Datname = "maps/" + Datname + ".map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);
        StatischeHeuristik heuristik = new StatischeHeuristik(Spiel);
        System.out.println(heuristik);
        Spiel.gueltigeZuege();

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

    }
}