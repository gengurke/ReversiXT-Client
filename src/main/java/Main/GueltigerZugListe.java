package Main;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Diese Klasse verwaltet die gueltigen Zuege mit Hilfe einer LinkedList
 */
public class GueltigerZugListe {
    private LinkedList<GueltigerZug> liste;

    public GueltigerZugListe() {
       liste = new LinkedList<GueltigerZug>();
    }

    /**
     * Erstellt gueltigen Zug und fuegt ihn am Anfang der Liste hinzu
     * @param x X Koordinate des Zuges
     * @param y Y Koordinate des Zuges
     * @param w Wert der trivialen Heuristik nach diesem Zug
     */
    public void hinzufuegen(int x, int y, int w) {
        GueltigerZug neu = new GueltigerZug(x,y,w);
        liste.addFirst(neu);
    }

    /**
     * Fuegt gueltigen Zug am Anfang der Liste hinzu
     * @param gzug gueltiger Zug
     */
    public void hinzufuegen(GueltigerZug gzug) {
        liste.addFirst(gzug);
    }

    /**
     * Gibt zurueck ob Liste leer ist
     * @return true falls Liste leer, false sonst
     */
    public boolean isEmpty() {
        return liste.isEmpty();
    }

    /**
     * Gibt erstes Element der Liste zurueck
     * @return erstes Element, null falls Liste leer
     */
    public GueltigerZug getHead() {
        if(isEmpty()) {
            return null;
        } else {
            return liste.getFirst();
        }
    }

    /**
     * Sortiert Liste absteigend
     */
    public void SortMaxFirst() {
        liste.sort(Comparator.comparingInt(GueltigerZug::getWert).reversed());
    }

    /**
     * Sortiert Liste aufsteigend
     */
    public void SortMinFirst() {
        liste.sort(Comparator.comparingInt(GueltigerZug::getWert));
    }

    /**
     * Get und Set Methoden der Liste
     */

    public LinkedList<GueltigerZug> getListe() {
        return liste;
    }

    public void setListe(LinkedList<GueltigerZug> gliste) {
        liste = gliste;
    }

    /**
     * Gibt echte Kopie der Liste zuruck
     * @return kopierte Liste
     */
    public GueltigerZugListe clone() {
        GueltigerZugListe neu = new GueltigerZugListe();
        for(int i = 0; i < getSize(); i++) {
            neu.hinzufuegen(liste.get(i));
        }
        return neu;
    }

    /**
     * @return Anzahl an Elementen in der Liste
     */
    public int getSize() {
        return liste.size();
    }

    /**
     * Loescht Liste
     */
    public void listeLoeschen() {
        liste.clear();
    }

    /**
     *Get und Set Methoden fuer einzelne Elemente der Liste
     */
    public GueltigerZug get(int i) {
        return liste.get(i);
    }

    public void set(GueltigerZugListe l) {
        listeLoeschen();
        liste = l.getListe();
    }

    public void Print() {
        for (int i = 0; i < getSize(); i++) {
            System.out.println(liste.get(i));
        }
    }
}
