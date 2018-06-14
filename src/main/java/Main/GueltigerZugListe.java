package Main;

import java.util.Comparator;
import java.util.LinkedList;

public class GueltigerZugListe {
    private LinkedList<GueltigerZug> liste;

    public GueltigerZugListe() {
       liste = new LinkedList<GueltigerZug>();
    }

    public void hinzufuegen(int x, int y, int w) {
        GueltigerZug neu = new GueltigerZug(x,y,w);
        liste.addFirst(neu);

    }

    public boolean isEmpty() {
        return liste.isEmpty();
    }

    public GueltigerZug getHead() {
        if(isEmpty()) {
            return null;
        } else {
            return liste.getFirst();
        }
    }

    public void SortMaxFirst() {
        liste.sort(Comparator.comparingInt(GueltigerZug::getWert).reversed());
    }

    public void SortMinFirst() {
        liste.sort(Comparator.comparingInt(GueltigerZug::getWert));
    }

    public LinkedList<GueltigerZug> getListe() {
        return liste;
    }

    public void setListe(LinkedList<GueltigerZug> gliste) {
        liste = gliste;
    }

    public GueltigerZugListe clone() {
        GueltigerZugListe neu = new GueltigerZugListe();
        neu.setListe((LinkedList<GueltigerZug>) liste.clone());
        return neu;
    }

    public int getSize() {
        return liste.size();
    }

    public void listeLoeschen() {
        liste.clear();
    }

    public GueltigerZug get(int i) {
        return liste.get(i);
    }

    public void Print() {
        for (int i = 0; i < getSize(); i++) {
            System.out.println(liste.get(i));
        }
    }
}
