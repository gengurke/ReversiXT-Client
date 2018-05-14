public class Heuristik {

    short breite, hoehe, brettsumme;
    short level;
    Spielbrett spiel;
    char[][][] array = new char[breite][hoehe][3];


    public Heuristik(Spielbrett spiel) {
        this.spiel = spiel;
        hoehe = (short) spiel.getHoehe();
        breite = (short) spiel.getBreite();

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
     * 5 = rechts
     * 6 = untenrechts
     * 7 = unten
     * 8 = untenlinks
     * 9 = links
     * 10 = obenlinks
     * ...
     */
    short spielbewerungBerechnen() {
        short spielwert = 0;
        short x = 0, y = 0;
        //char eshortrag = array[x][y][0];


        for (y = 0; y <= this.hoehe; y++) {
            for (x = 0; x <= this.breite; x++) {
                spielwert += checkZelle(x, y);
            }
        }
        return spielwert;
    }

    private short checkZelle(short x, short y) {
        short summe = 0;
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


        //geht alle Richtungen durch und addiert fuer jede Sichere Richtung +1
        for (short i = 0; i < 8; i++) {
            if (array[x][y][i + 3] == '1') {
                sicherheit++;
            }
        }

        summe += sicherheit * wertAchtel;

        return summe;

    }

    private void checkOben(short x, short y) {
        // Transition nach oben:
        if (array[x][y][2] != 0) {
            // TransitionenListe[] transitionen = spiel.getTransitionen();
            // transitionen[1].search((short) x, (short) y, (short) 1);

            // Oberste Zeile: wenn oberste Zeile und keine transi nach oben --> sicher.
        } else if (y == 0 && array[x][y][2] == '0') {
            array[x][y][3] = '1';
            //Nichtrandsteine: schaut ob das feld nach oben eine transition hat und ob das obere feld nach oben sicher ist.
        } else if (array[x][y][2] == '0' && array[x][y - 1][3] == '1') {
            array[x][y][3] = 1;
        } else {
            array[x][y][3] = 0;
        }
    }

    private void checkObenRechts(short x, short y) {
        if (x == breite - 1 && y == 0 && array[x][y][2] != '1') { // Ecke oben rechts:
            array[x][y][4] = 1;
        } else if (y == 0 && array[x][y][2] != '1') { // Oberste Zeile: wenn oberste Zeile und keine transi nach obenrechts --> sicher.
            array[x][y][4] = 1;
        } else if (array[x][y][2] != '1' && array[x + 1][y - 1][4] == '1') { //Nichtrandsteine:
            array[x][y][4] = 1;
        } else {
            array[x][y][4] = 0;
        }


    }

    private void checkRechts(short x, short y) {
        if (istRand(x, y, Richtungen.RECHTS) == 1) {
            Transition transition = getTransition(x,y,Richtungen.RECHTS);
        }
    }

    private void checkUntenRechts(short x, short y) {

    }

    private void checkUnten(short x, short y) {

        if ((y == spiel.getHoehe() - 1 && array[x][y][2] == 0) || (array[x][y + 1][1] == '-' && array[x][y][2] == 0)) {
            array[x][y][7] = 1;
            //Wenn das untere Feld nach unten sicher ist ist auch dieses Feld sicher
        } else if (array[x][y + 1][1] == '-') {
            array[x][y][7] = 1;

            /*Wenn das untere Feld nach unten nicht sicher ist, ist auch dieses Feld nicht sicher*/
        } else if (array[x][y + 1][7] == '0') {
            array[x][y][7] = 0;

            /*Wenn nach unten noch nicht berechnet wurde*/
        } else if (array[x][y][7 + 1] == '-') {

        } else {
            /*ERROR Feld wurde nicht rchtig initialisiert*/
        }
    }

    private void checkUntenLinks(short x, short y) {

    }

    private void checkLinks(short x, short y) {

    }

    private void checkObenLinks(short x, short y) {
        if (x == breite - 1 && y == 0 && array[x][y][2] != '1') { // Ecke oben rechts:
            array[x][y][4] = 1;
        } else if (y == 0 && array[x][y][2] != '1') { // Oberste Zeile: wenn oberste Zeile und keine transi nach obenrechts --> sicher.
            array[x][y][4] = 1;
        } else if (array[x][y][2] != '1' && array[x + 1][y - 1][4] == '1') { //Nichtrandsteine:
            array[x][y][4] = 1;
        } else {
            array[x][y][4] = 0;
        }
    }

    /**
     * Schaut ob in die Richtung der Ran ist oder ein "Rand" Feld
     *
     * @param x
     * @param y
     * @param dir
     * @return Liefert True oder False ob rand da ist oder nicht
     */
    private short istRand(short x, short y, Richtungen dir) {
        switch (dir) {
            case OBEN:
                if (y == 0) {
                    return 1;
                } else if (array[x][y - 1][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }


            case OBENRECHTS:
                if (y == 0 && x == spiel.getBreite() - 1) {
                    return 1;
                } else if (array[x + 1][y - 1][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }

            case RECHTS:
                if (x == spiel.getBreite() - 1) {
                    return 1;
                } else if (array[x + 1][y][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }

            case UNTENRECHTS:
                if (y == spiel.getHoehe() - 1 && x == spiel.getBreite() - 1) {
                    return 1;
                } else if (array[x + 1][y + 1][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }

            case UNTEN:
                if (y == spiel.getHoehe() - 1) {
                    return 1;
                } else if (array[x][y + 1][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }

            case UNTENLINKS:
                if (y == spiel.getHoehe() - 1 && x == 0) {
                    return 1;
                } else if (array[x - 1][y + 1][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }

            case LINKS:
                if (x == 0) {
                    return 1;
                } else if (array[x - 1][y][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }

            case OBENLINKS:
                if (y == 0 && x == 0) {
                    return 1;
                } else if (array[x - 1][y - 1][0] == '-') {
                    return 1;
                } else {
                    return 0;
                }

            default:
                return 0;
        }
    }

    /**
     * Die Funktion liefert Boolwerte jenachdem ob eine Transition in diese Richtung vorhanden ist
     *
     * @param x   X Koordinate
     * @param y   Y Koordinate
     * @param dir Richtung der potentiellen Transition
     * @return Liefert True oder False wenn Transition da ist oder nicht
     */
    private boolean hatTransition(short x, short y, Richtungen dir) {
        TransitionenListe[] transitionen = spiel.getTransitionen();
        Transition transition = new Transition((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);//TODO - Default Construktor erstellen?

        if (transitionen[array[x][y][2]].isEmpty()) {
            return false;
        } else if ((transition = transitionen[array[x][y][2]].search(x, y, (short) dir.ordinal())) != null) {
            return true;
        } else {
            return false;
        }
    }

    private Transition getTransition(short x, short y, Richtungen dir) {
        TransitionenListe[] transitionen = spiel.getTransitionen();
        Transition transition = transition = transitionen[array[x][y][2]].search(x, y, (short) dir.ordinal());

        if (hatTransition(x, y, dir)) {
            return transition;
        } else {
            return null;
        }
    }

    private boolean istEigeneZelle(){

        return false;
    }

    private boolean istSchonAngeschaut(){

        return  false;
    }

    short getSpielbewertung() {
        return brettsumme;
    }
}
