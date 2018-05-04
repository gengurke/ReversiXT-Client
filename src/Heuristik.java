public class Heuristik {

    int breite, hoehe, brettsumme;
    int level;
    Spielbrett spiel;
    char[][][] temparray = new char[breite][hoehe][3];


    public Heuristik(Spielbrett spiel) {
        this.spiel = spiel;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();

        spielbewerungBerechnen();
    }

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
     * 3 = oben
     * 4 = obenrechts
     * 5 = obenlinks
     * ...
     */
    int spielbewerungBerechnen() {
        int spielwert = 0;
        int x = 1, y = 1;
        char eintrag = temparray[x][y][0];


        for (y = 0; y <= this.hoehe; y++) {
            for (x = 0; x <= this.breite; x++) {
                //spielwert += checkZelle();
            }
        }

        //REFACTOR AB HIER _____________________________________________________________________________________________________

        //Check oben
        if (y == 0 && temparray[x][y][2 /*TODO Level checken und zahl fuer richtung*/] != '0') { // Oberste Zeile: wenn oberste Zeile und keine transi nach oben --> sicher.
            temparray[x][y][3] = 1;
        } else if (temparray[x][y][2] != '0' && temparray[x][y - 1][3] == '1') { //Nichtrandsteine: schaut ob das feld nach oben eine transition hat und ob das obere feld nach oben sicher ist.
            temparray[x][y][3] = 1;
        } else {
            temparray[x][y][3] = 0;
        }

        //Check obenrechts
        if (x == breite - 1 && y == 0 && temparray[x][y][2] != '1') { // Ecke oben rechts:
            temparray[x][y][4] = 1;
        } else if (y == 0 && temparray[x][y][2] != '1') { // Oberste Zeile: wenn oberste Zeile und keine transi nach obenrechts --> sicher.
            temparray[x][y][4] = 1;
        } else if (temparray[x][y][2] != '1' && temparray[x + 1][y - 1][4] == '1') { //Nichtrandsteine:
            temparray[x][y][4] = 1;
        } else {
            temparray[x][y][4] = 0;
        }

        //Check rechts
        if (x == breite - 1 && temparray[x][y][2] != '2') { // Rechteste Zeile:
            temparray[x][y][4] = 1;
        } else if (temparray[x][y][2] != '2' && temparray[x + 1][y][5] == '1') { // Nichtrandsteine:
            temparray[x][y][4] = 1;
        } else { // sonst:
            temparray[x][y][3] = 0;
        }

        //Check untenrechts
        if (x == breite - 1 && y == hoehe - 1 && temparray[x][y][2] != '3') { // Ecke unten rechts:
            temparray[x][y][4] = 1;
        }
        // rechte Spalte:
        // untere Zeile:
        // Nichtrand Steine:
        // sonst:
        //Check unten
        // wie oben...
        //Check untenlinks
        //Check links
        //Check obenlinks
        //rechnet fuer jeden Gegnerstein -1
        if (eintrag != 1 && eintrag != ' ' && eintrag != '-') {
            brettsumme -= 1;
        }

    return 2; //TODO platzhalter aendern
//REFACTOR BIS HIER _____________________________________________________________________________________________________
    }

    private int checkZelle(int x, int y) {

        checkOben(x, y);
        checkObenRechts(x, y);
        checkRechts(x, y);
        checkUntenRechts(x, y);
        checkUnten(x, y);
        checkUntenLinks(x, y);
        checkLinks(x, y);
        checkObenLinks(x, y);
        return 1 /*todo - magic number aendern*/;
    }

    private boolean checkOben(int x, int y) {
        return true;
    }

    private boolean checkObenRechts(int x, int y) {
        return true;
    }

    private boolean checkRechts(int x, int y) {
        return true;
    }

    private boolean checkUntenRechts(int x, int y) {
        return true;
    }

    private boolean checkUnten(int x, int y) {
        return true;
    }

    private boolean checkUntenLinks(int x, int y) {
        return true;
    }

    private boolean checkLinks(int x, int y) {
        return true;
    }

    private boolean checkObenLinks(int x, int y) {
        return true;
    }


    int getSpielbewertung() {
        return brettsumme;
    }
}
