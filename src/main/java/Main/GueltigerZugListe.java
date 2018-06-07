package Main;

public class GueltigerZugListe {
    private GueltigerZug head;
    private int size;

    public GueltigerZugListe() {
        head = null;
        size = 0;
    }

    public void hinzufuegen(int x, int y) {
        GueltigerZug neu = new GueltigerZug(x,y);
        if(head == null) {
            head = neu;
        } else {
            neu.setNext(head);
            head= neu;
        }
        size++;
    }

    /*public void hinzufuegen(int x, int y, Spielbrett spiel, int spieler) {
        GueltigerZug temp = head, neu;
        Heuristik h = new Heuristik(spiel, spieler);
        int w = h.trivialeHeuristik();
        neu = new GueltigerZug(x, y, w);
        if(temp != null) {
            if(temp.getWert() <= w) {
                while(temp.getNext() != null) {
                    if(temp.getNext().getWert() <= w) {
                        neu.setNext(temp.getNext());
                        temp.setNext(neu);
                        temp = null;
                    } else {
                        temp = temp.getNext();
                    }
                }
            } else {
                neu.setNext(head);
                head = neu;
            }
        } else {
            neu.setNext(head);
            head = neu;
        }
        size++;
    }*/

    public GueltigerZug getHead() {
        return head;
    }

    public int getSize() {
        return size;
    }

    public void listeLoeschen() {
        head = null;
    }
}
