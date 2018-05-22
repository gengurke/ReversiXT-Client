/*
*
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
public class Heuristik_1 {

    //Spielkonstanten
    final byte LEVEL = 12;
    final byte RICHTUNGSSHIFT = 4; // Da die Speicherung der Richtungen erst ab dem 4. Level des Arrays beginnt

    //Spielvariablen
    Spielbrett spiel;
    short brettsumme;
    int breite, hoehe;
    char[][][] spielfeld;
    short[][][] spielfeldSHORT;


    public Heuristik_1(Spielbrett spiel) {
        this.spiel = spiel;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();

        spielfeldSHORT = new short[breite][hoehe][LEVEL];
        spielfeldINIT();

        spielfeldBewerten();
        //TODO - Nur zum TESTEN -------------------------

        this.brettsumme = spielfeldWertBerechnenTest();
        //TODO ------------------------------------------
    }

    void spielfeldINIT() {

        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                spielfeldSHORT[x][y][0] = 0;
                spielfeldSHORT[x][y][7] = 0;
            }
        }
    }


*
     * Summiert alle Zellen auf. TODO nach test entfernen
     *
     * @return Gibt den durch die Heuristik berechneten Wert des Spielfeldes aus.


    short spielfeldWertBerechnenTest() {
        short spielwert = 0;
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                //spielwert += Character.getNumericValue(spielfeld[x][y][4]);
                spielwert += spielfeldSHORT[x][y][0];
            }
        }
        return spielwert;
    }

*
     * Summiert alle Zellen auf.
     *
     * @return Gibt den durch die Heuristik berechneten Wert des Spielfeldes aus.


    short spielfeldWertBerechnen() {
        short spielwert = 0;
        for (short x = 0; x < breite; x++) {
            for (short y = 0; y < hoehe; y++) {
                spielwert += spielfeldSHORT[x][y][0];
                //spielwert += Character.getNumericValue(spielfeld[x][y][4]);
            }
        }
        return spielwert;
    }

    private boolean istEcke(short x, short y) {
        if ((x == 0) && (y == 0)) {
            return true;
        } else if ((x == 0) && (y == hoehe - 1)) {
            return true;
        } else if ((y == 0) && (x == breite - 1)) {
            return true;
        } else if ((y == hoehe - 1) && (x == breite - 1)) {
            return true;
        } else {
            return false;
        }
    }

*
     * Geht jedes Feld durch und ueberprueft sie auf Kriterien wie Sicherheit oder Mobilitaet.
     * Die Ergebnisse der einzelnen Zellen werden zu Heuristik Wert (Array Level 4) hinzuaddiert.


    void spielfeldBewerten() {

 ================================ SICHERHEIT ================================

 ============================================================================



        TrivialeHeuristik();

 ============================================================================

 ============================================================================



 ================================ MOBILITAET ================================

 ============================================================================



 ============================================================================

 ============================================================================


 ================================ SICHERHEIT ================================

 ============================================================================



 ============================================================================

 ============================================================================


    }

*
     * Todo - Test


    void TrivialeHeuristik() {
        for (short y = 0; y < hoehe; y++) {
            for (short x = 0; x < breite; x++) {

                switch (spielfeld[x][y][0]) {
                    case '1':
                        if (istEcke(x, y)) {
                            spielfeldSHORT[x][y][0] = 100;

                        } else if ((istRand(x, y, Richtungen.OBEN)) || (istRand(x, y, Richtungen.OBENRECHTS)) || (istRand(x, y, Richtungen.RECHTS)) || (istRand(x, y, Richtungen.UNTENRECHTS)) || (istRand(x, y, Richtungen.UNTEN)) || (istRand(x, y, Richtungen.UNTENLINKS)) || (istRand(x, y, Richtungen.LINKS) || (istRand(x, y, Richtungen.OBENLINKS)))) {
                            spielfeldSHORT[x][y][0] = 9;
                        } else {
                            spielfeldSHORT[x][y][0] = 1;
                        }

                        break;
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        if ((istRand(x, y, Richtungen.OBEN)) || (istRand(x, y, Richtungen.OBENRECHTS)) || (istRand(x, y, Richtungen.RECHTS)) || (istRand(x, y, Richtungen.UNTENRECHTS)) || (istRand(x, y, Richtungen.UNTEN)) || (istRand(x, y, Richtungen.UNTENLINKS)) || (istRand(x, y, Richtungen.LINKS) || (istRand(x, y, Richtungen.OBENLINKS)))) {
                            spielfeldSHORT[x][y][0] = -5;
                        } else {
                            spielfeldSHORT[x][y][0] = -1;
                        }

                        break;
                    case '0':
                        spielfeldSHORT[x][y][0] = 0;
                        break;
                    case 'b':
                        spielfeldSHORT[x][y][0] = 2;
                        break;
                    case 'c':
                        spielfeldSHORT[x][y][0] = -5;
                        break;
                    case 'i':
                        spielfeldSHORT[x][y][0] = -8;
                        break;
                    default:
                        spielfeldSHORT[x][y][0] = 0;
                        break;

                }
            }
        }
    }


*
     * Schaut ob in die Richtung der Ran ist oder ein "Rand" Feld
     *
     * @param x
     * @param y
     * @param dir
     * @return Liefert True oder False ob rand da ist oder nicht


    private boolean istRand(short x, short y, Richtungen dir) {
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

    private boolean istNichtAngeschautOderUnsicher(short x, short y, Richtungen dir) {
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

    private boolean istSicher(short x, short y, Richtungen dir) {
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

*
     * Gibt an ob das Feld in die Richtung die eigene Farbe hat. Kuemmert sich nicht darum ob das Feld Rand oder Aenliches ist!!!
     *
     * @param x
     * @param y
     * @param dir
     * @return


    private boolean istEigeneFarbe(short x, short y, Richtungen dir) {
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


 Ausgabe


    public short getBrettsumme() {
        return this.brettsumme;
    }

    @Override
    public String toString() {
        return heuristikToString();
    }

    //gibt Heuristik als String zurueck
    private String heuristikToString() {
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < hoehe; i++) {
            for (int j = 0; j < breite; j++) {

                if (spielfeldSHORT[j][i][0] < 0) {
                    text.append(String.valueOf(" " + spielfeldSHORT[j][i][0]));
                } else {
                    text.append(String.valueOf("  " + spielfeldSHORT[j][i][0]));
                }
            }
            text.append("\n");
        }
        return "Heuristik:\n" + text.toString() + "\n" + "Summe:\n" + brettsumme + "\n" + "\n" + "Legende:\n" + "\n" + "Eigener Stein an Kante: 10\n" + "Fremder Stein an Kante: -10\n" + "Choise- und Inversionsstein: -8\n" + "Bonusstein: 2\n";

    }

    private void TrivialeHeuristik() {
        for (short y = 0; y < hoehe; y++) {
            for (short x = 0; x < breite; x++) {

                switch (spielfeld[x][y][0]) {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '0':
                    case 'x':
                        if (istEcke(x, y)) {
                            spielfeldSHORT[x][y][0] = 50;

                        } else if ((istRand(x, y, Richtungen.OBEN)) || (istRand(x, y, Richtungen.OBENRECHTS)) || (istRand(x, y, Richtungen.RECHTS)) || (istRand(x, y, Richtungen.UNTENRECHTS)) || (istRand(x, y, Richtungen.UNTEN)) || (istRand(x, y, Richtungen.UNTENLINKS)) || (istRand(x, y, Richtungen.LINKS) || (istRand(x, y, Richtungen.OBENLINKS)))) {
                            spielfeldSHORT[x][y][0] = 10;
                        } else {
                            spielfeldSHORT[x][y][0] = 1;
                        }
                        break;
                    case 'b':
                        spielfeldSHORT[x][y][0] = 5;
                        break;
                    case 'c':
                        spielfeldSHORT[x][y][0] = -5;
                        break;
                    case 'i':
                        spielfeldSHORT[x][y][0] = -8;
                        break;
                    default:
                        spielfeldSHORT[x][y][0] = 0;
                        break;
                }
            }
        }
    }
}
*/
