public class GueltigerZugListe {
    private GueltigerZug head;

    public GueltigerZugListe() {
        head = null;
    }

    public void hinzufuegen(int x, int y) {
        GueltigerZug neu = new GueltigerZug(x, y);
        if(head == null) {
            head = neu;
        } else {
            neu.setNext(head);
            head = neu;
        }
    }

    public GueltigerZug getHead() {
        return head;
    }

    public void listeLoeschen() {
        head = null;
    }
}
