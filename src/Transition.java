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

    public short getNumber(short x, short y, short dir) {
        if((x == x1) && (y == y1) && (dir == dir1)) {
            return 1;
        } else if((x == x2) && (y == y2) && (dir == dir2)) {
            return 2;
        }
        return 0;
    }

    public short getX(short number) {
        if(number == 1) {
            return x1;
        } else if(number == 2) {
            return x2;
        } else {
            return 0;
        }
    }

    public short getY(short number) {
        if(number == 1) {
            return y1;
        } else if(number == 2) {
            return y2;
        } else {
            return 0;
        }
    }

    public short getDir(short number) {
        if(number == 1) {
            return dir1;
        } else if(number == 2) {
            return dir2;
        } else {
            return 0;
        }
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
