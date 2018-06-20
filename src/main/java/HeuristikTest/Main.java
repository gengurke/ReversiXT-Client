package HeuristikTest;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //Einlesen des Dateinamens
        Scanner sc = new Scanner(System.in);
        String Datname = "maps/Heuristik_Test_Maps/miniQuadrat_mitTransi.map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gueltige Zuege
        Spiel.gueltigeZuege();

        //Heuristik
        DynamischeHeuristik heuristik = new DynamischeHeuristik(Spiel, 1);
        System.out.println(heuristik);
        /*TrivialeHeuristikMitAusgabe trivialeHeuristikMitAusgabe = new TrivialeHeuristikMitAusgabe(Spiel, 1);
        System.out.println(trivialeHeuristikMitAusgabe);*/
    }
}