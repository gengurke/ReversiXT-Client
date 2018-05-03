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
        int x = 0, y = 0;
        //char eintrag = temparray[x][y][0];


        for (y = 0; y <= this.hoehe; y++) {
            for (x = 0; x <= this.breite; x++) {
                spielwert += checkZelle(x, y);
            }
        }
        return spielwert;
    }

    //REFACTOR AB HIER _____________________________________________________________________________________________________
    {
        //Check oben
        /*        if (y == 0 && temparray[x][y][2 *//*TODO Level checken und zahl fuer richtung*//*] != '0') { // Oberste Zeile: wenn oberste Zeile und keine transi nach oben --> sicher.
            temparray[x][y][3] = 1;
        } else if (temparray[x][y][2] != '0' && temparray[x][y - 1][3] == '1') { //Nichtrandsteine: schaut ob das feld nach oben eine transition hat und ob das obere feld nach oben sicher ist.
            temparray[x][y][3] = 1;
        } else {
            temparray[x][y][3] = 0;
        }*/

        //Check obenrechts
/*        if (x == breite - 1 && y == 0 && temparray[x][y][2] != '1') { // Ecke oben rechts:
            temparray[x][y][4] = 1;
        } else if (y == 0 && temparray[x][y][2] != '1') { // Oberste Zeile: wenn oberste Zeile und keine transi nach obenrechts --> sicher.
            temparray[x][y][4] = 1;
        } else if (temparray[x][y][2] != '1' && temparray[x + 1][y - 1][4] == '1') { //Nichtrandsteine:
            temparray[x][y][4] = 1;
        } else {
            temparray[x][y][4] = 0;
        }*/

        //Check rechts
/*        if (x == breite - 1 && temparray[x][y][2] != '2') { // Rechteste Zeile:
            temparray[x][y][4] = 1;
        } else if (temparray[x][y][2] != '2' && temparray[x + 1][y][5] == '1') { // Nichtrandsteine:
            temparray[x][y][4] = 1;
        } else { // sonst:
            temparray[x][y][3] = 0;
        }

        //Check untenrechts
        if (x == breite - 1 && y == hoehe - 1 && temparray[x][y][2] != '3') { // Ecke unten rechts:
            temparray[x][y][4] = 1;
        }*/
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
/*        if (eintrag != 1 && eintrag != ' ' && eintrag != '-') {
            brettsumme -= 1;
        }*/
    }
//REFACTOR BIS HIER _____________________________________________________________________________________________________


    private int checkZelle(int x, int y) {
        int summe = 0;
        byte sicherheit = 0;

        /*Wert fuer Sicherheit fuer jedes Achtel*/
        short wertAchtel = 25;

        checkOben(x, y);
        checkObenRechts(x, y);
        checkRechts(x, y);
        checkUntenRechts(x, y);
        checkUnten(x, y);
        checkUntenLinks(x, y);
        checkLinks(x, y);
        checkObenLinks(x, y);

        //geht alle Richtungen durch und addiert fuer jede Sichere +1
        for (int i = 0; i < 8; i++) {
            if (temparray[x][y][i + 3] == '1') {
                sicherheit++;
            }
        }

        summe += sicherheit * wertAchtel;

        return summe;
    }

    private void checkOben(int x, int y) {
        if (y == 0 && temparray[x][y][2 /*TODO Level checken und zahl fuer richtung*/] != '0') { // Oberste Zeile: wenn oberste Zeile und keine transi nach oben --> sicher.
            temparray[x][y][3] = '1';
        } else if (temparray[x][y][2] != '0' && temparray[x][y - 1][3] == '1') { //Nichtrandsteine: schaut ob das feld nach oben eine transition hat und ob das obere feld nach oben sicher ist.
            temparray[x][y][3] = 1;
        } else {
            temparray[x][y][3] = 0;
        }
    }

    private void checkObenRechts(int x, int y) {
        if (x == breite - 1 && y == 0 && temparray[x][y][2] != '1') { // Ecke oben rechts:
            temparray[x][y][4] = 1;
        } else if (y == 0 && temparray[x][y][2] != '1') { // Oberste Zeile: wenn oberste Zeile und keine transi nach obenrechts --> sicher.
            temparray[x][y][4] = 1;
        } else if (temparray[x][y][2] != '1' && temparray[x + 1][y - 1][4] == '1') { //Nichtrandsteine:
            temparray[x][y][4] = 1;
        } else {
            temparray[x][y][4] = 0;
        }



    }

    private void checkRechts(int x, int y) {

    }

    private void checkUntenRechts(int x, int y) {

    }

    private void checkUnten(int x, int y) {

    }

    private void checkUntenLinks(int x, int y) {

    }

    private void checkLinks(int x, int y) {

    }

    private void checkObenLinks(int x, int y) {
        if (x == breite - 1 && y == 0 && temparray[x][y][2] != '1') { // Ecke oben rechts:
            temparray[x][y][4] = 1;
        } else if (y == 0 && temparray[x][y][2] != '1') { // Oberste Zeile: wenn oberste Zeile und keine transi nach obenrechts --> sicher.
            temparray[x][y][4] = 1;
        } else if (temparray[x][y][2] != '1' && temparray[x + 1][y - 1][4] == '1') { //Nichtrandsteine:
            temparray[x][y][4] = 1;
        } else {
            temparray[x][y][4] = 0;
        }
    }


    int getSpielbewertung() {
        return brettsumme;
    }
}
