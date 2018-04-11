import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //Einlesen des Dateinamens
        Scanner sc = new Scanner(System.in);
        //String Datname = sc.next();
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett("star.txt");
        //Ausgabe des Spielbretts
        System.out.println(Spiel);
        int s = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        Spiel.Zug(s,x,y);

        sc.close();
    }
}
