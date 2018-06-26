package Main;

public class TrivialeHeuristik implements Heuristik {

    //Spielvariablen
    private int brettsumme;
    private int breite, hoehe, spieler;
    private char[][][] spielfeld;


    public TrivialeHeuristik(Spielbrett spiel, int spieler) {
        this.spieler = spieler;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();


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
                        //Eingener Spieler
                        if (Character.getNumericValue(spielfeld[x][y][0]) == spieler) {
                            brettsumme += 10;
                            //Anderer Spieler
                        } else {
                            brettsumme -= 10;
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

    @Override
    public int getSpielbewertung() {
        return brettsumme;
    }
}

