package MainPackage;/*
 * 0 = Feld
 * 1 = gueltige Zuege
 * 2 = Transition Ja/Nein
 * 3 = Wert des Feldes (Heuristik)
 * 4 = oben
 * 5 = obenrechts
 * 6 = rechts
 * 7 = untenrechts
 * 8 = unten
 * 9 = untenlinks
 * 10 = links
 * 11 = obenlinks*/

public enum Level {
    Feld(0),
    gueltigeZ(1),
    Transi(2),
    HeuristikWert(3),
    OBENSicher(4),
    OBENRECHTSSicher(5),
    RECHTSSicher(6),
    UNTENRECHTSSicher(7),
    UNTENSicher(8),
    UNTENLINKSSicher(9),
    LINKSSicher(10),
    OBENLINKSSicher(11);


    private final int map;

    private Level(int level) {
        this.map = level;
    }

    public int num() {
        return map;
    }
}