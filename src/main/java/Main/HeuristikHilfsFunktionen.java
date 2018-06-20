package Main;

public class HeuristikHilfsFunktionen {

    /*
     * Gibt die entgegengesetzte Richtung zurueck
     * */
    static Richtungen getOppDir(Richtungen dir) {
        switch (dir) {
            case OBEN:
                return Richtungen.UNTEN;
            case OBENRECHTS:
                return Richtungen.UNTENLINKS;
            case RECHTS:
                return Richtungen.LINKS;
            case UNTENRECHTS:
                return Richtungen.OBENLINKS;
            case UNTEN:
                return Richtungen.OBEN;
            case UNTENLINKS:
                return Richtungen.OBENRECHTS;
            case LINKS:
                return Richtungen.RECHTS;
            case OBENLINKS:
                return Richtungen.UNTENRECHTS;
            default:
                break;
        }
        return Richtungen.UNTEN; // TODO
    }

    /*
     * Gibt das neue X je nach Richtung zurueck
     * */
    static int getNewX(int x, Richtungen dir) {
        switch (dir) {
            case OBEN:
            case UNTEN:
                return x;
            case OBENRECHTS:
            case RECHTS:
            case UNTENRECHTS:
                return ++x;
            case UNTENLINKS:
            case LINKS:
            case OBENLINKS:
                return --x;
        }
        return -1;
    }

    /*
     * Gibt das neue Y je nach Richtung zurueck
     * */
    static int getNewY(int y, Richtungen dir) {
        switch (dir) {
            case RECHTS:
            case LINKS:
                return y;
            case UNTENRECHTS:
            case UNTEN:
            case UNTENLINKS:
                return ++y;
            case OBENLINKS:
            case OBEN:
            case OBENRECHTS:
                return --y;
        }
        return -1;
    }
}
