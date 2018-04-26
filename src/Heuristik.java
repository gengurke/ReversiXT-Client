public class Heuristik {
    /**
     * Punkteschlüssel:
     * <p>
     * Faerbbarkeit:
     * 0/4 Faerbbar: 100
     * 1/4 Faerbbar: 65
     * 2/4 Faerbbar: 40
     * 3/4 Faerbbar: 20
     * 4/4 Faerbbar: 0
     * <p>
     * Anzahl Zugmoeglichkeiten:
     * 0 Moeglichekeiten   -1000
     * 1 Moeglichekeiten   -200
     * 2 Moeglichekeiten   -100
     * 3 Moeglichekeiten   -50
     * 4 Moeglichekeiten   0
     * 5 Moeglichekeiten   10
     * 6 Moeglichekeiten   20
     * 7 Moeglichekeiten   30
     * 8 Moeglichekeiten   40
     * 9 Moeglichekeiten   50
     * 10 Moeglichekeiten  70
     * 11 Moeglichekeiten  90
     * 12 Moeglichekeiten  100
     * 13 Moeglichekeiten  120
     * 14 Moeglichekeiten  130
     * 15+ Moeglichekeiten  150
     */
    final int size = 50;
    int breite, hoehe, brettsumme;
    int level;
    Spielbrett spiel;
    char[][][] temparray = new char[breite][hoehe][3];


    public Heuristik(Spielbrett spiel) {
        this.spiel = spiel;
        spielbewerungBerechnen();
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();

    }

    /**
     * Level der 3.Dimension
     * 3 = oben
     * 4 = obenrechts
     * 5 = obenlinks
     * ...
     */
    void spielbewerungBerechnen() {
        //String[][][] spielfeld = spiel.getSpielfeld();
        int x, y;
        for (x = 0; x <= size; x++) {
            for (y = 0; y <= size; y++) {
                char eintrag = temparray[x][y][0];
                byte horizontal = 0;
                byte untenL_obenR = 0;
                byte vertikal = 0;
                byte untenR_obenL = 0;
                char transition0, transition1, transition2, transition3, transition4, transition5, transition6, transition7;


                //Check oben
                if (y == 0 && temparray[x][y][2 /*TODO Level checken und zahl für richtung*/] != '0') { // Oberste Zeile: wenn oberste Zeile und keine transi nach oben --> sicher.
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
                } else if () { // rechte Spalte:

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
                //rechnet für jeden Gegnerstein -1
                if (eintrag != 1 && eintrag != ' ' && eintrag != '-') {
                    brettsumme -= 1;
                }

                if ()


            }

        }

    }

    private boolean checkNachOben() {

    }

    int getSpielbewertung() {
        return brettsumme;
    }


}
