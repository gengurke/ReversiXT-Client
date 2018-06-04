/**
 * Begriffe:
 * (Spiel)brett = Das ganze spielbrett mit allen Eigenschaften wie Ueberschreibsteine, Bomben usw.
 * Zelle = einzelnes Feld auf dem Spielfeld
 * Spielfeld = Summer aller Zellen.
 * <p>
 * MainPackage.Level der 3.Dimension
 * 0 = Feld
 * 1 = gueltige Zuege
 * 2 = MainPackage.Transition Ja/Nein
 * 3 = Wert des Feldes (MainPackage.Heuristik)
 * 4 = oben
 * 5 = obenrechts
 * 6 = rechts
 * 7 = untenrechts
 * 8 = unten
 * 9 = untenlinks
 * 10 = links
 * 11 = obenlinks
 *//*

public class MainPackage.Heuristik {

    //Spielkonstanten
    final byte LEVEL = 11;
    final byte RICHTUNGSSHIFT = 4; // Da die Speicherung der MainPackage.Richtungen erst ab dem 4. MainPackage.Level des Arrays beginnt

    //Spielvariablen
    MainPackage.Spielbrett spiel;
    short brettsumme;
    int breite, hoehe;
    char[][][] spielfeld;
    //short[][][] shortarray = new short[breite][hoehe][LEVEL];


    public MainPackage.Heuristik(MainPackage.Spielbrett spiel) {
        this.spiel = spiel;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();

        spielfeldBewerten();
        //TODO - Nur zum TESTEN -------------------------
        */
/*TrivialeHeuristik();*//*

        this.brettsumme = spielfeldWertBerechnenTest();
        //TODO ------------------------------------------
    }

    */
/**
     * Summiert alle Zellen auf. TODO nach test entfernen
     *
     * @return Gibt den durch die MainPackage.Heuristik berechneten Wert des Spielfeldes aus.
     *//*

    short spielfeldWertBerechnenTest() {
        short spielwert = 0;
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                spielwert += Character.getNumericValue(spielfeld[x][y][4]);
            }
        }
        return spielwert;
    }

    */
/**
     * Summiert alle Zellen auf.
     *
     * @return Gibt den durch die MainPackage.Heuristik berechneten Wert des Spielfeldes aus.
     *//*

    short spielfeldWertBerechnen() {
        short spielwert = 0;
        for (short x = 0; x < breite; x++) {
            for (short y = 0; y < hoehe; y++) {

                spielwert += Character.getNumericValue(spielfeld[x][y][4]);
            }
        }
        return spielwert;
    }

    */
/**
     * Geht jedes Feld durch und ueberprueft sie auf Kriterien wie Sicherheit oder Mobilitaet.
     * Die Ergebnisse der einzelnen Zellen werden zu MainPackage.Heuristik Wert (Array MainPackage.Level 4) hinzuaddiert.
     *//*

    void spielfeldBewerten() {

        */
/* ================================ SICHERHEIT ================================ *//*

        */
/* ============================================================================ *//*

        checkAlleZellenAufSicherheit();

        */
/* ============================================================================ *//*

        */
/* ============================================================================ *//*



        */
/* ================================ MOBILITAET ================================ *//*

        */
/* ============================================================================ *//*



        */
/* ============================================================================ *//*

        */
/* ============================================================================ *//*


        */
/* ================================ SICHERHEIT ================================ *//*

        */
/* ============================================================================ *//*



        */
/* ============================================================================ *//*

        */
/* ============================================================================ *//*


    }

    */
/**
     * Todo - Test
     *//*

    void TrivialeHeuristik() {
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                spielfeld[x][y][4] = spielfeld[x][y][0];
                if (spielfeld[x][y][0] == '1') {
                    spielfeld[x][y][4] = '5';
                }
            }
        }
    }

    private void berechneZelleSicherheit(short x, short y) {

        //geht alle MainPackage.Richtungen durch und addiert fuer jede Sichere Richtung +25
        for (short i = 0; i < 8; i++) {
            if (spielfeld[x][y][i + RICHTUNGSSHIFT] == '1') {
                spielfeld[x][y][4] += 25;
            }
        }
    }

    private void checkAlleZellenAufSicherheit() {
        for (short x = 0; x < breite; x++) {
            for (short y = 0; y < hoehe; y++) {
                for (int dir = 0; dir < 8; dir++) {
                    checkZelleAufSicherheit(x, y, MainPackage.Richtungen.values()[dir]);
                }
            }
        }
    }

    private void checkZelleAufSicherheit(short x, short y, MainPackage.Richtungen dir) {
        if (istRand(x, y, dir)) {

            MainPackage.Transition transition = getTransition(x, y, dir);


            //Schauen ob es MainPackage.Transition gibt. Wenn nicht => sicher. Sonst => Zelle hinter MainPackage.Transition pruefen
            if (transition == null) {
                spielfeld[x][y][dir.ordinal() + RICHTUNGSSHIFT] = '1';

            } else {
                short nr = transition.getNumber(x, y, (short) dir.ordinal());
                short anderesEndeX, anderesEndeY, anderesEndeDir;

                //Je nachdem welches Transitionsende es ist x und y zuweisen
                if (nr == 1) {
                    anderesEndeX = transition.getX((short) 2);
                    anderesEndeY = transition.getY((short) 2);
                    anderesEndeDir = transition.dir2;
                } else {
                    anderesEndeX = transition.getX((short) 1);
                    anderesEndeY = transition.getX((short) 1);
                    anderesEndeDir = transition.dir1;
                }

                //Auf eigene Zelle pruefen sonst Zelle hinter MainPackage.Transition pruefen
                if (!istEigeneZelle(anderesEndeX, anderesEndeY, x, y)) {
                    spielfeld[x][y][dir.ordinal()] = '1';
                } else {
                    checkZelleAufSicherheit(anderesEndeX, anderesEndeY, MainPackage.Richtungen.values()[anderesEndeDir]);
                }

            }
        } else if (istEigeneFarbe(x, y, dir) && istSicher(x, y, dir)) {
            spielfeld[x][y][dir.ordinal() + RICHTUNGSSHIFT] = '1';
        } else if (istEigeneFarbe(x, y, dir)) {
            checkZelleAufSicherheit(getNewX(x, (short) dir.ordinal()), getNewY(y, (short) dir.ordinal()), dir);
        } else {
            spielfeld[x][y][dir.ordinal() + RICHTUNGSSHIFT] = '0';
        }
    }

    private short getNewX(short x, short dir) {
        switch (dir) {
            case 0:
            case 4:
                return x;
            case 1:
            case 2:
            case 3:
                return ++x;
            case 5:
            case 6:
            case 7:
                return --x;
        }
        return -1;
    }

    private short getNewY(short y, short dir) {
        switch (dir) {
            case 2:
            case 6:
                return y;
            case 3:
            case 4:
            case 5:
                return ++y;
            case 7:
            case 0:
            case 1:
                return --y;
        }
        return -1;
    }

    */
/**
     * Schaut ob in die Richtung der Ran ist oder ein "Rand" Feld
     *
     * @param x
     * @param y
     * @param dir
     * @return Liefert True oder False ob rand da ist oder nicht
     *//*

    private boolean istRand(short x, short y, MainPackage.Richtungen dir) {
        switch (dir) {
            case OBEN:
                if (y == 0) {
                    return true;
                } else return spielfeld[x][y - 1][0] == '-';


            case OBENRECHTS:
                if ((y == 0 && x == spiel.getBreite() - 1) || y == 0 || x == spiel.getBreite() - 1) {
                    return true;
                } else return spielfeld[x + 1][y - 1][0] == '-';

            case RECHTS:
                if (x == spiel.getBreite() - 1) {
                    return true;
                } else return spielfeld[x + 1][y][0] == '-';

            case UNTENRECHTS:
                if ((y == spiel.getHoehe() - 1 && x == spiel.getBreite() - 1) || y == spiel.getHoehe() - 1 || x == spiel.getBreite() - 1) {
                    return true;
                } else return spielfeld[x + 1][y + 1][0] == '-';

            case UNTEN:
                if (y == spiel.getHoehe() - 1) {
                    return true;
                } else return spielfeld[x][y + 1][0] == '-';

            case UNTENLINKS:
                if ((y == spiel.getHoehe() - 1 && x == 0) || y == spiel.getHoehe() - 1 || x == 0) {
                    return true;
                } else return spielfeld[x - 1][y + 1][0] == '-';

            case LINKS:
                if (x == 0) {
                    return true;
                } else return spielfeld[x - 1][y][0] == '-';

            case OBENLINKS:
                if ((y == 0 && x == 0) || y == 0 || x == 0) {
                    return true;
                } else return spielfeld[x - 1][y - 1][0] == '-';

            default:
                return false;
        }
    }

    */
/**
     * Die Funktion liefert Boolwerte jenachdem ob eine MainPackage.Transition in diese Richtung vorhanden ist
     *
     * @param x   X Koordinate
     * @param y   Y Koordinate
     * @param dir Richtung der potentiellen MainPackage.Transition
     * @return Liefert True oder False wenn MainPackage.Transition da ist oder nicht
     *//*

    private boolean hatTransition(short x, short y, MainPackage.Richtungen dir) {
        MainPackage.TransitionenListe[] transitionen = spiel.getTransitionen();
        MainPackage.Transition transition = new MainPackage.Transition((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);//TODO - Default Construktor erstellen?

        if (transitionen[spielfeld[x][y][2]].isEmpty()) {
            return false;
        } else if ((transition = transitionen[spielfeld[x][y][2]].search(x, y, (short) dir.ordinal())) != null) {
            return true;
        } else {
            return false;
        }
    }

    private MainPackage.Transition getTransition(short x, short y, MainPackage.Richtungen dir) {
        MainPackage.TransitionenListe[] transitionen = spiel.getTransitionen();

        if (hatTransition(x, y, dir)) {
            return transitionen[spielfeld[x][y][2]].search(x, y, (short) dir.ordinal());
        } else {
            return null;
        }
    }

    private boolean istEigeneZelle(short anderesX, short anderesY, short x, short y) {
        if (x == anderesX && y == anderesY) {
            return true;
        } else {
            return false;
        }
    }

    private boolean istNichtAngeschautOderUnsicher(short x, short y, MainPackage.Richtungen dir) {
        switch (dir) {
            case OBEN:
                return spielfeld[x][y][3] != '1';

            case OBENRECHTS:
                return spielfeld[x + 1][y - 1][4] != '1';

            case RECHTS:
                return spielfeld[x + 1][y][5] != '1';

            case UNTENRECHTS:
                return spielfeld[x + 1][y + 1][6] != '1';

            case UNTEN:
                return spielfeld[x][y + 1][7] != '1';

            case UNTENLINKS:
                return spielfeld[x - 1][y + 1][8] != '1';

            case LINKS:
                return spielfeld[x - 1][y][9] != '1';

            case OBENLINKS:
                return spielfeld[x - 1][y - 1][10] != '1';
            default:
                return false;
        }
    }

    private boolean istSicher(short x, short y, MainPackage.Richtungen dir) {
        switch (dir) {
            case OBEN:
                return spielfeld[x][y][3] == '1';

            case OBENRECHTS:
                return spielfeld[x + 1][y - 1][4] == '1';

            case RECHTS:
                return spielfeld[x + 1][y][5] == '1';

            case UNTENRECHTS:
                return spielfeld[x + 1][y + 1][6] == '1';

            case UNTEN:
                return spielfeld[x][y + 1][7] == '1';

            case UNTENLINKS:
                return spielfeld[x - 1][y + 1][8] == '1';

            case LINKS:
                return spielfeld[x - 1][y][9] == '1';

            case OBENLINKS:
                return spielfeld[x - 1][y - 1][10] == '1';
            default:
                return false;
        }
    }

    */
/**
     * Gibt an ob das Feld in die Richtung die eigene Farbe hat. Kuemmert sich nicht darum ob das Feld Rand oder Aenliches ist!!!
     *
     * @param x
     * @param y
     * @param dir
     * @return
     *//*

    private boolean istEigeneFarbe(short x, short y, MainPackage.Richtungen dir) {
        switch (dir) {

            case OBEN:
                return spielfeld[x][y - 1][0] == '1';

            case OBENRECHTS:
                return spielfeld[x + 1][y - 1][0] == '1';

            case RECHTS:
                return spielfeld[x + 1][y][0] == '1';

            case UNTENRECHTS:
                return spielfeld[x + 1][y + 1][0] == '1';

            case UNTEN:
                return spielfeld[x][y + 1][0] == '1';

            case UNTENLINKS:
                return spielfeld[x - 1][y + 1][0] == '1';

            case LINKS:
                return spielfeld[x - 1][y][0] == '1';

            case OBENLINKS:
                return spielfeld[x - 1][y - 1][0] == '1';
            default:
                return false;
        }
    }

    short getSpielbewertung() {
        return brettsumme;
    }


    */
/* Ausgabe *//*


    public short getBrettsumme() {
        return this.brettsumme;
    }

    @Override
    public String toString() {
        return heuristikToString();
    }

    //gibt MainPackage.Heuristik als String zurueck
    private String heuristikToString() {
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < hoehe; i++) {
            for (int j = 0; j < breite; j++) {

                text.append(spielfeld[j][i][4] + " ");


            }
            text.append("\n");
        }
        return "MainPackage.Heuristik:\n" + text.toString() + "\n" + "Summe:\n" + brettsumme + "\n";
    }
}
*/
