public class Transition {
    private short x1, y1, dir1, x2, y2, dir2;
    private Transition next;

    public Transition(short x1, short y1, short dir1, short x2, short y2, short dir2) {
        this.x1 = x1;
        this.y1 = y1;
        this.dir1 = dir1;
        this.x2 = x2;
        this.y2 = y2;
        this.dir2 = dir2;
        next = null;
    }

    public short getX1() {
        return x1;
    }

    public short getY1() {
        return y1;
    }

    public short getDir1() {
        return dir1;
    }

    public short getX2() {
        return x2;
    }

    public short getY2() {
        return y2;
    }

    public short getDir2() {
        return dir2;
    }

    public Transition getNext() {
        return next;
    }

    public void setNext(Transition next) {
        this.next = next;
    }

    public String toString() {
        return "("+x1+","+y1+") "+dir1+" <-> ("+x2+","+y2+") "+dir2;
    }
}
