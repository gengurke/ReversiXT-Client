package Main;

import java.util.LinkedList;

public class TransitionenListe {
    private LinkedList<Transition> liste;

    public TransitionenListe() {
        liste = new LinkedList<Transition>();
    }

    public boolean isEmpty() {
        if (liste.isEmpty()) {
            return true;
        }
        return false;
    }

    public Transition getHead(){
            if(isEmpty()) {
                return null;
            } else {
                return liste.getFirst();
            }
    }

    public void Insert(Transition t){
        liste.addFirst(t);
    }

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
