public class GueltigerZug {
    private int x, y;
    private GueltigerZug next;

    public GueltigerZug(int x, int y) {
        this.x = x;
        this.y = y;
        next = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public void setNext(GueltigerZug next) {
        this.next = next;
    }

    public String toString() {
        return "("+x+","+y+")";
    }
}
