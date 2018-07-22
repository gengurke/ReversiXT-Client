package Main;

import java.util.LinkedList;

/**
 * Diese Klasse verwaltet die Transitionen mit Hilfe einer LinkedList
 */
public class TransitionenListe {
    private LinkedList<Transition> liste;

    public TransitionenListe() {
        liste = new LinkedList<Transition>();
    }

    /**
     * @return true falls Liste leer, false sonst
     */
    public boolean isEmpty() {
        if (liste.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Fuegt Transition am Anfang der Liste ein
     * @param t Transition
     */
    public void Insert(Transition t){
        liste.addFirst(t);
    }

    /**
     * Sucht in der Liste nach einer Transition an der Stelle (x,y) mit Richtung "dir"
     * @param x X Koordinate der Transition
     * @param y Y Koordinate der Transition
     * @param dir Richtung der Tranition
     * @return
     */
    public Transition search(int x, int y, int dir) {
        for(int i = 0; i < liste.size(); i++) {
            Transition temp = liste.get(i);
            if (temp.getNumber(x, y, dir) != 0) {
                return temp;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < liste.size(); i++) {
            Transition temp = liste.get(i);
            sb.append(temp.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
