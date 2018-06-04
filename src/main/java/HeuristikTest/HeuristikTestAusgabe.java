package HeuristikTest;

import java.io.IOException;

public class HeuristikTestAusgabe {

    public HeuristikTestAusgabe() throws IOException {

        String Datname = "maps/diamond1.map";
        HeuristikTest.Spielbrett Spiel = new Spielbrett(Datname);
        int spieler = 1;

        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gültige Züge
        Spiel.gueltigeZuege();

        //MainPackage.Heuristik
        //DynamischeHeuristik heuristik = new DynamischeHeuristik(Spiel);
        //System.out.println(heuristik);
    }
}
