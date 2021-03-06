package Main;

/**
 * Diese Klasse stellt Transitionen mit jeweils zwei X, Y und Richtungen als Attributen dar
 */
public class Transition {
    public int x1, y1, dir1, x2, y2, dir2;
    private Transition next;

    public Transition(int x1, int y1, int dir1, int x2, int y2, int dir2) {
        this.x1 = x1;
        this.y1 = y1;
        this.dir1 = dir1;
        this.x2 = x2;
        this.y2 = y2;
        this.dir2 = dir2;
        next = null;
    }

    /**
     * Gibt zurueck ob die gegebene Transition die Ein- oder Ausgangstransition ist
     * @param x X Koordinate der Transition
     * @param y Y Koordinate der Transition
     * @param dir Richtung der Tranition
     * @return 1 falls Eingangstransition, 2 falls Ausgangstransition, 0 sonst
     */
    public int getNumber(int x, int y, int dir) {
        if ((x == x1) && (y == y1) && (dir == dir1)) {
            return 1;
        } else if ((x == x2) && (y == y2) && (dir == dir2)) {
            return 2;
        }
        return 0;
    }

    /**
     * Gibt X Koordinate an die sich nach Durchlaufen der Transition ergibt
     * @param number 1 Einganstransition, 2 Ausgangstransition
     * @return neue X Koordinate
     */
    public int getX(int number) {
        if (number == 1) {
            return x2;
        } else if (number == 2) {
            return x1;
        } else {
            return 0;
        }
    }

    /**
     * Gibt Y Koordinate an die sich nach Durchlaufen der Transition ergibt
     * @param number 1 Einganstransition, 2 Ausgangstransition
     * @return neue Y Koordinate
     */
    public int getY(int number) {
        if (number == 1) {
            return y2;
        } else if (number == 2) {
            return y1;
        } else {
            return 0;
        }
    }

    /**
     * Gibt Ausgangsrichtung bei gegebender Eingangsrichtung an (entgegengesetzt)
     * @param dir Eingangsrichtung
     * @return Ausgangsruchtung
     */
    public int getOppDir(int dir) {
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

    /**
     * Set und toString Methoden der Transitionen
     */
    public Transition getNext() {
        return next;
    }

    public void setNext(Transition next) {
        this.next = next;
    }

    public String toString() {
        return "(" + x1 + "," + y1 + ") " + dir1 + " <-> (" + x2 + "," + y2 + ") " + dir2;
    }
}
