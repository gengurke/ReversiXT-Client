/**
 * Begriffe:
 * (Spiel)brett = Das ganze spielbrett mit allen Eigenschaften wie Ueberschreibsteine, Bomben usw.
 * Zelle = einzelnes Feld auf dem Spielfeld
 * Spielfeld = Summer aller Zellen.
 * <p>
 * Level der 3.Dimension
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
 * 11 = obenlinks
 */
public class Heuristik {

    //Spielkonstanten
    final byte LEVEL = 11;
    final byte RICHTUNGSSHIFT = 4; // Da die Speicherung der Richtungen erst ab dem 4. Level des Arrays beginnt

    //Spielvariablen
    Spielbrett spiel;
    short breite, hoehe, brettsumme;

    char[][][] array = new char[breite][hoehe][LEVEL];
    short[][][] shortarray = new short[breite][hoehe][LEVEL];


    public Heuristik(Spielbrett spiel) {
        this.spiel = spiel;
        hoehe = (short) spiel.getHoehe();
        breite = (short) spiel.getBreite();

        //TODO - Nur zum TESTEN -------------------------
        TrivialeHeuristik();
        this.brettsumme = spielfeldWertBerechnenTest();
        //TODO ------------------------------------------
    }

    /**
     * Summiert alle Zellen auf. TODO nach test entfernen
     *
     * @return Gibt den durch die Heuristik berechneten Wert des Spielfeldes aus.
     */
    short spielfeldWertBerechnenTest() {
        short spielwert = 0;
        for (short x = 0; x < breite; x++) {
            for (short y = 0; y < hoehe; y++) {
                spielwert += Character.getNumericValue(array[x][y][4]);
            }
        }
        return spielwert;
    }


    /**
     * Summiert alle Zellen auf.
     *
     * @return Gibt den durch die Heuristik berechneten Wert des Spielfeldes aus.
     */
    short spielfeldWertBerechnen() {
        short spielwert = 0;
        for (short x = 0; x < breite; x++) {
            for (short y = 0; y < hoehe; y++) {

                spielwert += Character.getNumericValue(array[x][y][4]);
            }
        }
        return spielwert;
    }

    /**
     * Geht jedes Feld durch und ueberprueft sie auf Kriterien wie Sicherheit oder Mobilitaet.
     * Die Ergebnisse der einzelnen Zellen werden zu Heuristik Wert (Array Level 4) hinzuaddiert.
     */
    void spielfeldBewerten() {

        /* ================================ SICHERHEIT ================================ */
        /* ============================================================================ */
        checkAlleZellenAufSicherheit();

        /* ============================================================================ */
        /* ============================================================================ */


        /* ================================ MOBILITAET ================================ */
        /* ============================================================================ */


        /* ============================================================================ */
        /* ============================================================================ */

        /* ================================ SICHERHEIT ================================ */
        /* ============================================================================ */


        /* ============================================================================ */
        /* ============================================================================ */

    }


    /**
     * Todo - Test
     */
    private void TrivialeHeuristik() {
        for (short x = 0; x < breite; x++) {
            for (short y = 0; y < hoehe; y++) {
                if (array[x][y][0] == '1') {
                    array[x][y][4] = '5';
                }
            }
        }
    }

    private void berechneZelleSicherheit(short x, short y) {

        //geht alle Richtungen durch und addiert fuer jede Sichere Richtung +25
        for (short i = 0; i < 8; i++) {
            if (array[x][y][i + RICHTUNGSSHIFT] == '1') {
                array[x][y][4] += 25;
            }
        }
    }


    private void checkAlleZellenAufSicherheit() {
        for (short x = 0; x < breite; x++) {
            for (short y = 0; y < hoehe; y++) {
                for (int dir = 0; dir < 8; dir++) {
                    checkZelleAufSicherheit(x, y, Richtungen.values()[dir]);
                }
            }
        }
    }

    private void checkZelleAufSicherheit(short x, short y, Richtungen dir) {
        if (istRand(x, y, dir)) {
            Transition transition = getTransition(x, y, dir);
            short nr = transition.getNumber(x, y, (short) dir.ordinal());

            //transition.getX();
            //TODO Kathi Fragen

            //TODO Checken ob hinter der transition das eigene feld wieder liegt
        } else if (istEigeneFarbe(x, y, dir) && istSicher(x, y, dir)) {
            array[x][y][dir.ordinal() + RICHTUNGSSHIFT] = '1';
        } else if (istEigeneFarbe(x, y, dir)) {
            checkZelleAufSicherheit(x, y, dir);
        } else {
            array[x][y][dir.ordinal() + RICHTUNGSSHIFT] = '0';
        }
    }

    /**
     * Schaut ob in die Richtung der Ran ist oder ein "Rand" Feld
     *
     * @param x
     * @param y
     * @param dir
     * @return Liefert True oder False ob rand da ist oder nicht
     */
    private boolean istRand(short x, short y, Richtungen dir) {
        switch (dir) {
            case OBEN:
                if (y == 0) {
                    return true;
                } else return array[x][y - 1][0] == '-';


            case OBENRECHTS:
                if (y == 0 && x == spiel.getBreite() - 1) {
                    return true;
                } else return array[x + 1][y - 1][0] == '-';

            case RECHTS:
                if (x == spiel.getBreite() - 1) {
                    return true;
                } else return array[x + 1][y][0] == '-';

            case UNTENRECHTS:
                if (y == spiel.getHoehe() - 1 && x == spiel.getBreite() - 1) {
                    return true;
                } else return array[x + 1][y + 1][0] == '-';

            case UNTEN:
                if (y == spiel.getHoehe() - 1) {
                    return true;
                } else return array[x][y + 1][0] == '-';

            case UNTENLINKS:
                if (y == spiel.getHoehe() - 1 && x == 0) {
                    return true;
                } else return array[x - 1][y + 1][0] == '-';

            case LINKS:
                if (x == 0) {
                    return true;
                } else return array[x - 1][y][0] == '-';

            case OBENLINKS:
                if (y == 0 && x == 0) {
                    return true;
                } else return array[x - 1][y - 1][0] == '-';

            default:
                return false;
        }
    }

    /**
     * Die Funktion liefert Boolwerte jenachdem ob eine Transition in diese Richtung vorhanden ist
     *
     * @param x   X Koordinate
     * @param y   Y Koordinate
     * @param dir Richtung der potentiellen Transition
     * @return Liefert True oder False wenn Transition da ist oder nicht
     */
    private boolean hatTransition(short x, short y, Richtungen dir) {
        TransitionenListe[] transitionen = spiel.getTransitionen();
        Transition transition = new Transition((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);//TODO - Default Construktor erstellen?

        if (transitionen[array[x][y][2]].isEmpty()) {
            return false;
        } else if ((transition = transitionen[array[x][y][2]].search(x, y, (short) dir.ordinal())) != null) {
            return true;
        } else {
            return false;
        }
    }

    private Transition getTransition(short x, short y, Richtungen dir) {
        TransitionenListe[] transitionen = spiel.getTransitionen();

        if (hatTransition(x, y, dir)) {
            return transitionen[array[x][y][2]].search(x, y, (short) dir.ordinal());
        } else {
            return null;
        }
    }

    private boolean istEigeneZelle() {

        return false;
    }

    private boolean istNichtAngeschautOderUnsicher(short x, short y, Richtungen dir) {
        switch (dir) {
            case OBEN:
                return array[x][y][3] != '1';

            case OBENRECHTS:
                return array[x + 1][y - 1][4] != '1';

            case RECHTS:
                return array[x + 1][y][5] != '1';

            case UNTENRECHTS:
                return array[x + 1][y + 1][6] != '1';

            case UNTEN:
                return array[x][y + 1][7] != '1';

            case UNTENLINKS:
                return array[x - 1][y + 1][8] != '1';

            case LINKS:
                return array[x - 1][y][9] != '1';

            case OBENLINKS:
                return array[x - 1][y - 1][10] != '1';
            default:
                return false;
        }
    }

    private boolean istSicher(short x, short y, Richtungen dir) {
        switch (dir) {
            case OBEN:
                return array[x][y][3] == '1';

            case OBENRECHTS:
                return array[x + 1][y - 1][4] == '1';

            case RECHTS:
                return array[x + 1][y][5] == '1';

            case UNTENRECHTS:
                return array[x + 1][y + 1][6] == '1';

            case UNTEN:
                return array[x][y + 1][7] == '1';

            case UNTENLINKS:
                return array[x - 1][y + 1][8] == '1';

            case LINKS:
                return array[x - 1][y][9] == '1';

            case OBENLINKS:
                return array[x - 1][y - 1][10] == '1';
            default:
                return false;
        }
    }

    /**
     * Gibt an ob das Feld in die Richtung die eigene Farbe hat. Kuemmert sich nicht darum ob das Feld Rand oder Aenliches ist!!!
     *
     * @param x
     * @param y
     * @param dir
     * @return
     */
    private boolean istEigeneFarbe(short x, short y, Richtungen dir) {
        switch (dir) {

            case OBEN:
                return array[x][y - 1][0] == '1';

            case OBENRECHTS:
                return array[x + 1][y - 1][0] == '1';

            case RECHTS:
                return array[x + 1][y][0] == '1';

            case UNTENRECHTS:
                return array[x + 1][y + 1][0] == '1';

            case UNTEN:
                return array[x][y + 1][0] == '1';

            case UNTENLINKS:
                return array[x - 1][y + 1][0] == '1';

            case LINKS:
                return array[x - 1][y][0] == '1';

            case OBENLINKS:
                return array[x - 1][y - 1][0] == '1';
            default:
                return false;
        }
    }

    short getSpielbewertung() {
        return brettsumme;
    }
}
