package Main;

public class GueltigerZug {
    private int x, y, wert;
    private GueltigerZug next;

    public GueltigerZug(int x, int y, int w) {
        this.x = x;
        this.y = y;
        this.wert = w;
        next = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWert() {
        return wert;
    }

    public GueltigerZug getNext() {
        return next;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWert(int w) {
        wert = w;
    }

    public void setNext(GueltigerZug next) {
        this.next = next;
    }

    public String toString() {
        return "("+x+","+y+") "+wert;
    }
}
