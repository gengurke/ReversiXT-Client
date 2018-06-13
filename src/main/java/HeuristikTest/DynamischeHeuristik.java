package HeuristikTest;

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
 * 3 = Wert des Feldes (Heuristik_alt3)
 * <p>
 * <p>
 * Level Sicherheistarray
 * <p>
 * 0 = Oben
 * 1 = obenRechts
 * 2 = Rechts
 */

public class DynamischeHeuristik implements Heuristik {

    //Spielvariablen
    private int brettsumme;
    private int breite, hoehe, spieler;
    private char[][][] spielfeld;
    private TransitionenListe[] transitionen;

    private int[][][] sicherheit;
    private int[][] felderwerte;


    public DynamischeHeuristik(Spielbrett spiel, int spieler) {
        this.spieler = spieler;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();
        transitionen = spiel.getTransitionen();
        sicherheit = new int[breite][hoehe][7];
        felderwerte = new int[breite][hoehe];


        heuristikwertBerechnen();
    }

    private void heuristikwertBerechnen() {

        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                zelleUeberpruefen(x, y, spieler);
            }
        }
    }

    private void zelleUeberpruefen(int x, int y, int spieler) {
        for (Richtungen dir : Richtungen.values()) {
            switch (spielfeld[x][y][0]) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    richtungUeberpruefen(x, y, spieler, dir);
                    break;
                case '0':
                case 'x':
                    break;
                case 'b':
                    break;
                case 'c':
                    break;
                case 'i':
                    break;
                case '-':
                    break;
                default:
                    break;
            }
        }
    }

    private void richtungUeberpruefen(int x, int y, int spieler, Richtungen dir) {
        int intDir = dir.ordinal(); // Int Repraesentation der Richtung

        //Wenn Nummer des aktuellen Spielers
        if (Character.getNumericValue(spielfeld[x][y][0]) == spieler) {
            //Rand
            if (istRand(x, y, dir)) {
                Transition transi;

                //hat Transition
                if ((transi = getTransition(x, y, dir)) != null) {
                    switch (transi.getNumber(x, y, intDir)) {
                        case 1:
                            //TODO - in eine Richtung Ueberpruefen bis nicht mehr sicher
                            //TODO Ueberpruefungslogic einbauen (z.B. ob eigenes Feld USW)
                            richtungUeberpruefen(transi.x2, transi.y2, spieler, getOppDir(dir));
                            break;
                        case 2:
                            richtungUeberpruefen(transi.x1, transi.y1, spieler, getOppDir(dir));
                            break;

                    }
                    //hat keine Transition
                } else {

                }
                //kein Rand
            } else {

            }
            //Wenn nicht der aktuelle Spieler
            //TODO durch etwas sinnvolles ersetzten.
        } else {
            if (istRand(x, y, Richtungen.values()[intDir])) {
                felderwerte[x][y] = -1;
            } else {
                felderwerte[x][y] = -1;
            }
        }
    }

    private boolean hatTransition(int x, int y, Richtungen dir) {
        if ((transitionen[spielfeld[x][y][2]].search(x, y, dir.ordinal())) != null) {
            return true;
        } else {
            return false;
        }
    }

    private Transition getTransition(int x, int y, Richtungen dir) {

        if (hatTransition(x, y, dir)) {
            return transitionen[spielfeld[x][y][2]].search(x, y, dir.ordinal());
        } else {
            return null;
        }
    }

    private boolean istRand(int x, int y, Richtungen dir) {
        switch (dir) {
            case OBEN:
                if (y == 0) {
                    return true;
                } else return spielfeld[x][y - 1][0] == '-';


            case OBENRECHTS:
                if ((y == 0 && x == breite - 1) || y == 0 || x == breite - 1) {
                    return true;
                } else return spielfeld[x + 1][y - 1][0] == '-';

            case RECHTS:
                if (x == breite - 1) {
                    return true;
                } else return spielfeld[x + 1][y][0] == '-';

            case UNTENRECHTS:
                if ((y == hoehe - 1 && x == breite - 1) || y == hoehe - 1 || x == breite - 1) {
                    return true;
                } else return spielfeld[x + 1][y + 1][0] == '-';

            case UNTEN:
                if (y == hoehe - 1) {
                    return true;
                } else return spielfeld[x][y + 1][0] == '-';

            case UNTENLINKS:
                if ((y == hoehe - 1 && x == 0) || y == hoehe - 1 || x == 0) {
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

    private Richtungen getOppDir(Richtungen dir) {
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

    public int getSpielbewertung() {
        return brettsumme;
    }

    @Override
    public String toString() {
        return heuristikToString();
    }

    //gibt Heuristik als String zurueck
    private String heuristikToString() {
        StringBuilder text = new StringBuilder();
        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {

                //Formattierung
                if (felderwerte[x][y] <= -100) {
                    text.append(String.valueOf(" " + felderwerte[x][y]));
                } else if (felderwerte[x][y] <= -10) {
                    text.append(String.valueOf("  " + felderwerte[x][y]));
                } else if (felderwerte[x][y] <= -1) {
                    text.append(String.valueOf("   " + felderwerte[x][y]));
                } else if (felderwerte[x][y] >= 1000) {
                    text.append(String.valueOf(" " + felderwerte[x][y]));
                } else if (felderwerte[x][y] >= 100) {
                    text.append(String.valueOf("  " + felderwerte[x][y]));
                } else if (felderwerte[x][y] >= 10) {
                    text.append(String.valueOf("   " + felderwerte[x][y]));
                } else {
                    text.append(String.valueOf("    " + felderwerte[x][y]));
                }
            }
            text.append("\n");
        }
        return "Heuristik:\n" + text.toString() + "\n" + "Summe:\n" + brettsumme + "\n";

    }
}


