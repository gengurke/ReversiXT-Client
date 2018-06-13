package Main;

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
    int[][][] sicherheit;

    public DynamischeHeuristik(Spielbrett spiel, int spieler) {
        this.spieler = spieler;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();
        sicherheit = new int[breite][hoehe][9];
    }

    void statischFeldwertBerechnen() {

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
                        //Wenn Nummer des aktuellen Spielers
                        if (Character.getNumericValue(spielfeld[x][y][0]) == spieler) {

                        } else {

                        }
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

    private void zelleUeberpruefen(int x, int y) {
        for (int dir = 0; dir < 8; dir++) {
            if (istRand(x, y, Richtungen.values()[dir])){

            } else {

            }
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


    public int getSpielbewertung() {
        return brettsumme;
    }

    @Override
    public String toString() {
        return heuristikToString();
    }

    //gibt Heuristik_alt3 als String zurueck
    private String heuristikToString() {
        StringBuffer text = new StringBuffer();
        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {

                //Formattierung
                if (sicherheit[x][y][8] <= -100) {
                    text.append(String.valueOf(" " + sicherheit[x][y][8]));
                } else if (sicherheit[x][y][8] <= -10) {
                    text.append(String.valueOf("  " + sicherheit[x][y][8]));
                } else if (sicherheit[x][y][8] >= 1000) {
                    text.append(String.valueOf(" " + sicherheit[x][y][8]));
                } else if (sicherheit[x][y][8] >= 100) {
                    text.append(String.valueOf("  " + sicherheit[x][y][8]));
                } else if (sicherheit[x][y][8] >= 10) {
                    text.append(String.valueOf("   " + sicherheit[x][y][8]));
                } else {
                    text.append(String.valueOf("    " + sicherheit[x][y][8]));
                }
            }
            text.append("\n");
        }
        return "Heuristik_alt3:\n" + text.toString() + "\n" + "Summe:\n" + brettsumme + "\n";

    }
}


