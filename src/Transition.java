public class Transition {
    public short x1, y1, dir1, x2, y2, dir2;
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
            return x2;
        } else if(number == 2) {
            return x1;
        } else {
            return 0;
        }
    }

    public short getY(short number) {
        if(number == 1) {
            return y2;
        } else if(number == 2) {
            return y1;
        } else {
            return 0;
        }
    }

    public short getOppDir(short dir) {
            switch (dir) {
                case 0:
                    return 4;
                case 1:
                    return 5;
                case 2:
                    return 6;
                case 3:
                    return 7;
                case 4:
                    return 0;
                case 5:
                    return 1;
                case 6:
                    return 2;
                case 7:
                    return 3;
                default:
                    break;
            }
            return -1;
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
