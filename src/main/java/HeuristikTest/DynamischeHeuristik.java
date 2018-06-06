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
    int brettsumme;
    int breite, hoehe, spieler;
    char[][][] spielfeld;
    TransitionenListe[] transitionen;

    int[][][] sicherheit;
    int[][] felderwerte;


    public DynamischeHeuristik(Spielbrett spiel, int spieler) {
        this.spieler = spieler;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();
        transitionen = spiel.getTransitionen();
        sicherheit = new int[breite][hoehe][7];
        felderwerte = new int[breite][hoehe];


        dynamischFeldwertBerechnen();
    }

    void dynamischFeldwertBerechnen() {

        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                switch (spielfeld[x][y][0]) {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        zelleUeberpruefen(x, y, spieler);
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
    }

    private void zelleUeberpruefen(int x, int y, int spieler) {
        for (int intDir = 0; intDir < 8; intDir++) {
            Richtungen dir = Richtungen.values()[intDir];

            //Wenn Nummer des aktuellen Spielers
            if (Character.getNumericValue(spielfeld[x][y][0]) == spieler) {
                if (istRand(x, y, dir)) {
                    Transition transi;
                    if ((transi = getTransition(x, y, dir)) != null) {
                        switch (transi.getNumber(x, y, intDir)) {
                            case 1:
                                //TODO Durch richtungUeberpruefen ersetzten
                                zelleUeberpruefen(transi.x2, transi.y2, spieler);
                                break;
                            case 2:
                                //TODO Durch richtungUeberpruefen ersetzten
                                zelleUeberpruefen(transi.x1, transi.y1, spieler);
                                break;

                        }
                    } else {

                    }
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
    }

    private void richtungUeberpruefen(int x, int y, int spieler) {
        //TODO - in eine Richtung Überprüfen bis nicht mehr sicher
    }

    private boolean hatTransition(int x, int y, Richtungen dir) {
        Transition transition;
        if ((transitionen[spielfeld[x][y][2]].search(x, y, (int) dir.ordinal())) != null) {
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

    private int getOppDir(int dir) {
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

    @Override
    public int getTrivialeHeuristik() {
        return 0;
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
        StringBuffer text = new StringBuffer();
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


