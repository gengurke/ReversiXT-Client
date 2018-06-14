package Main;

public class GueltigerZugListe {
    private GueltigerZug head;
    private int size;

    public GueltigerZugListe() {
        head = null;
        size = 0;
    }

    public void hinzufuegen(int x, int y, int w) {
        GueltigerZug neu = new GueltigerZug(x,y,w);
        if(head == null) {
            head = neu;
        } else {
            neu.setNext(head);
            head= neu;
        }
        size++;
    }

    public void hinzufuegenSortAlpha(int x, int y, int w) {
        GueltigerZug neu = new GueltigerZug(x,y,w);
        if(head != null) {
            head = hinzufuegenAlpha(head, neu);
        } else {
            head = neu;
        }
        size++;
    }

    public GueltigerZug hinzufuegenAlpha(GueltigerZug zug, GueltigerZug neu) {
        if(zug == null) {
            return neu;
        } else {
            if(zug.getWert() <= neu.getWert()) {
                neu.setNext(zug);
                return neu;
            } else {
                zug.setNext(hinzufuegenAlpha(zug.getNext(), neu));
                return zug;
            }
        }
    }

    public void hinzufuegenSortBeta(int x, int y, int w) {
        GueltigerZug neu = new GueltigerZug(x,y,w);
        if(head != null) {
            head = hinzufuegenBeta(head, neu);
        } else {
            head = neu;
        }
        size++;
    }

    public GueltigerZug hinzufuegenBeta(GueltigerZug zug, GueltigerZug neu) {
        if(zug == null) {
            return neu;
        } else {
            if(zug.getWert() >= neu.getWert()) {
                neu.setNext(zug);
                return neu;
            } else {
                zug.setNext(hinzufuegenBeta(zug.getNext(), neu));
                return zug;
            }
        }
    }

    public GueltigerZug getHead() {
        return head;
    }

    public int getSize() {
        return size;
    }

    public void listeLoeschen() {
        head = null;
        size = 0;
    }
}
