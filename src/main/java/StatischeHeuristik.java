
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
 * <p>
 * Level Shicherheistarray
 * <p>
 * 0 = Oben
 * 1 = obenRechts
 * 2 = Rechts
 */

public class StatischeHeuristik {

    //Spielkonstanten
    final byte LEVEL = 12;
    final byte RICHTUNGSSHIFT = 4; // Da die Speicherung der Richtungen erst ab dem 4. Level des Arrays beginnt

    //Spielvariablen
    Spielbrett spiel;
    int brettsumme;
    int breite, hoehe;
    char[][][] spielfeld;
    int[][][] sicherheit;

    public StatischeHeuristik(Spielbrett spiel) {
        this.spiel = spiel;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();

        sicherheit = new int[breite][hoehe][9];

        statischFeldwertBerechnen();
        mobilitaetBerechnen();
        spielbrettSummeBerechnen();

    }

    void spielbrettSummeBerechnen() {
        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                brettsumme += sicherheit[x][y][8];
            }
        }
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
                    case '0':
                    case 'x':
                        for (Richtungen dir : Richtungen.values()) {
                            if (istRand(x, y, dir)) {
                                sicherheit[x][y][dir.ordinal()] = 1;
                                sicherheit[x][y][getOppDir(dir.ordinal())] = 1;
                                sicherheit[x][y][8] += sicherheit[x][y][dir.ordinal()] * 10;
                                sicherheit[x][y][8] += sicherheit[x][y][getOppDir(dir.ordinal())] * 10;
                            }
                        }
                        break;
                    case 'b':
                        sicherheit[x][y][8] += 20;
                        break;
                    case 'c':
                        sicherheit[x][y][8] += 20;
                        break;
                    case 'i':
                        sicherheit[x][y][8] += -100;
                        break;
                    default:
                        sicherheit[x][y][8] += 0;
                        break;

                }
            }
        }
    }

    void mobilitaetBerechnen() {
        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                if (spielfeld[x][y][1] == 'X') {
                    sicherheit[x][y][8] += 20;
                }
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
                if (sicherheit[x][y][8] < 0) {
                    text.append(String.valueOf(" " + sicherheit[x][y][8]));
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
        return "Heuristik:\n" + text.toString() + "\n" + "Summe:\n" + brettsumme + "\n";

    }
}


