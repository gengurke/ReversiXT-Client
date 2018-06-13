package HeuristikTest;

public class TrivialeHeuristikMitAusgabe implements Heuristik {

    //Spielvariablen
    private int brettsumme;
    private int breite, hoehe, spieler, ueberschreibsteine;
    private char[][][] spielfeld;

    private int[][] felderwerte;


    public TrivialeHeuristikMitAusgabe(Spielbrett spiel, int spieler) {
        this.spieler = spieler;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();
        ueberschreibsteine = spiel.getUeberschreibsteine();
        felderwerte = new int[breite][hoehe];


        heuristikwertBerechnen();
    }

    private void heuristikwertBerechnen() {
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {

                switch (spielfeld[x][y][0]) {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        zelleBewerten(x, y, spieler);
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
        summieren();
    }

    private void zelleBewerten(int x, int y, int spieler) {
        //Eingener Spieler
        if (Character.getNumericValue(spielfeld[x][y][0]) == spieler) {
            felderwerte[x][y] += 10;
            //Anderer Spieler
        } else {
            felderwerte[x][y] -= 10;
        }
    }

    private void summieren() {

        //Feld aufsummieren
        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                brettsumme += felderwerte[x][y];
            }
        }

        //Sondersteine aufsummieren
        //Ueberschreibsteine
        brettsumme += ueberschreibsteine * 100;
    }

    @Override
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
