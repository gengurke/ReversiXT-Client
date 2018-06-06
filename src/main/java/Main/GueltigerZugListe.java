package Main;

public class GueltigerZugListe {
    private GueltigerZug head;
    private int size;

    public GueltigerZugListe() {
        head = null;
        size = 0;
    }

    public void hinzufuegen(int x, int y) {
        GueltigerZug neu = new GueltigerZug(x, y);
        if(head == null) {
            head = neu;
        } else {
            neu.setNext(head);
            head = neu;
        }
        size++;
    }

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
