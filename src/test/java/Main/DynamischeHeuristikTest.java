package Main;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DynamischeHeuristikTest {

    @Test
    public void miniQuadrat_leer() throws IOException {
        //Einlesen des Dateinamens
        String Datname = "maps/Heuristik_Test_Maps/JUnit_maps/miniQuadrat_leer.map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gueltige Zuege
        Spiel.gueltigeZuege(1);

        //Heuristik
        DynamischeHeuristik heuristik = new DynamischeHeuristik(Spiel, 1);
        System.out.println(heuristik);
        assertEquals(heuristik.getSpielbewertung(), 0);

    }

    @Test
    public void miniQuadrat_vierSicher() throws IOException {
        //Einlesen des Dateinamens
        String Datname = "maps/Heuristik_Test_Maps/JUnit_maps/miniQuadrat_vierSicher.map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gueltige Zuege
        Spiel.gueltigeZuege(1);

        //Heuristik
        DynamischeHeuristik heuristik = new DynamischeHeuristik(Spiel, 1);
        System.out.println(heuristik);
        assertEquals(heuristik.getSpielbewertung(), 0);

    }

    @Test
    public void miniQuadrat_dreiSicher() throws IOException {
        //Einlesen des Dateinamens
        String Datname = "maps/Heuristik_Test_Maps/JUnit_maps/miniQuadrat_dreiSicher.map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gueltige Zuege
        Spiel.gueltigeZuege(1);

        //Heuristik
        DynamischeHeuristik heuristik = new DynamischeHeuristik(Spiel, 1);
        System.out.println(heuristik);
        assertEquals(heuristik.getSpielbewertung(), 0);

    }

    @Test
    public void miniQuadrat_zweiSicher() throws IOException {
        //Einlesen des Dateinamens
        String Datname = "maps/Heuristik_Test_Maps/JUnit_maps/miniQuadrat_zweiSicher.map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gueltige Zuege
        Spiel.gueltigeZuege(1);

        //Heuristik
        DynamischeHeuristik heuristik = new DynamischeHeuristik(Spiel, 1);
        System.out.println(heuristik);
        assertEquals(heuristik.getSpielbewertung(), 0);

    }

    @Test
    public void miniQuadrat_eineSicher() throws IOException {
        //Einlesen des Dateinamens
        String Datname = "maps/Heuristik_Test_Maps/JUnit_maps/miniQuadrat_eineSicher.map";
        //Erzeugen des Spielbretts
        Spielbrett Spiel = new Spielbrett(Datname);
        //Ausgabe des Spielbretts
        System.out.println(Spiel);

        //gueltige Zuege
        Spiel.gueltigeZuege(1);

        //Heuristik
        DynamischeHeuristik heuristik = new DynamischeHeuristik(Spiel, 1);
        System.out.println(heuristik);
        assertEquals(heuristik.getSpielbewertung(), 0);

    }
}