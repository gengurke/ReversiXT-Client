package Main;

import static Main.HeuristikHilfsFunktionen.*;

public class DynamischeHeuristik implements Heuristik {

    /*******************Einstellungen*******************/
    //// Sicherheiten
    private final int vierRichtungenSicher = 2000;
    private final int dreiRichtungenSicher = 300;
    private final int zweiRichtungenSicher = 50;
    private final int eineRichtungenSicher = 20;
    private final int keineRichtungSicher = 10;

    //// Mobilitaet
    private final int wertGueltigerZug = 20;

    //// Ueberschreibsteine
    private final int wertUeberschreibstein = 10000;


    /****************Einstellungen Ende*****************/

    //Spielvariablen
    private int brettsumme;
    private int breite, hoehe, spieler, anzahlGueltigeZuege, ueberschreibsteine, ersatzsteine;
    private char[][] spielfeld;
    private TransitionenListe[][] transitionen;

    //Arrays fuer die Heuristikberechnung
    private int[][][][] sicherheit;
    private int[][] felderwerte;


    DynamischeHeuristik(Spielbrett spiel, int spieler) {
        //Spieler und Spielfeld
        this.spieler = spieler;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();
        transitionen = spiel.getTransitionen();
        ueberschreibsteine = spiel.getUeberschreibsteine();
        ersatzsteine = spiel.getErsatzsteine();

        //gueltige Zuege TODO gueltige zuege fuer den gegener mit aufnehmen
        GueltigerZugListe gueltigeZuege = spiel.getGueltigeZuege();
        anzahlGueltigeZuege = gueltigeZuege.getSize();

        //Arrays fuer die Heuristikberechnung
        sicherheit = new int[breite][hoehe][spiel.getSpieler() + 1][8];
        felderwerte = new int[breite][hoehe];


        heuristikwertBerechnen();
        heuristikAufsummieren();
    }

    //TODO - Statische Wellenbewertung um bonussteine, eventuell ecken.

    private void heuristikwertBerechnen() {
        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                for (Richtungen dir : Richtungen.values()) {
                    switch (spielfeld[x][y]) {
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                            ueberpruefenAufSicherheit(x, y, dir);
                            continue;
                        case '0':
                            continue;
                        case 'x':
                            continue;
                        case 'b':
                            continue;
                        case 'c':
                            continue;
                        case 'i':
                            continue;
                        case '-':
                            continue;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void ueberpruefenAufSicherheit(int x, int y, Richtungen dir) {
        int schrittCounter = 0;
        int urSpieler = Character.getNumericValue(spielfeld[x][y]);

        richtungUeberpruefenUndSicherheitenFestlegen(x, y, x, y, urSpieler, schrittCounter, dir, dir);
    }

    /**
     * Kernfunktion in dieser Klasse. Sie ueberpfrueft die gegebene zelle strahlenmaessig in alle Richtungen auf
     * Faerbbarkeit und legt dann die Sicherheit im array sicherheit fest.
     *
     * @param x         x der zu ueberpruefenden Zelle
     * @param y         y der zu ueberpruefenden Zelle
     * @param urX       unrspuengliches x von dem die ueberpruefung gestartet ist (um loops zu finden)
     * @param urY       unrspuengliches y von dem die ueberpruefung gestartet ist (um loops zu finden)
     * @param urDir     unrspuengliche richtung von dem die ueberpruefung gestartet ist (um loops zu finden)
     * @param urSpieler urspruenglicher spieler vom dem die ueberpruefung gestartet wurde
     * @param dir       richtung der aktuellen ueberpruefung
     * @return true wenn die Richtung der Zelle Sicher ist
     */
    private boolean richtungUeberpruefenUndSicherheitenFestlegen(int x, int y, int urX, int urY, int urSpieler, int schrittCounter, Richtungen urDir, Richtungen dir) {

        //Wenn ueberpruefender Spieler gleich Spieler auf spielfeld
        int intDir = dir.ordinal(); // Int Repraesentation der Richtung
        int oppIntDir = getOppDir(dir).ordinal();

        //loop abfangen
        if (x == urX && y == urY && dir == urDir && schrittCounter != 0) {
            return true;

            //kein loop
        } else {
            schrittCounter++;
            if (urSpieler == Character.getNumericValue(spielfeld[x][y])) {
                //Schon sicher
                if ((sicherheit[x][y][urSpieler][intDir] == 1) && (sicherheit[x][y][urSpieler][oppIntDir] == 1)) {
                    return true;
                    //nicht sicher
                } else if (sicherheit[x][y][urSpieler][intDir] == -1) {
                    return false;
                    //noch nicht ueberprueft
                } else {
                    //Rand
                    if (istRand(x, y, dir)) {
                        //hat Transition
                        Transition transi;
                        if ((transi = getTransition(x, y, dir)) != null) {
                            switch (transi.getNumber(x, y, intDir)) {
                                case 1:
                                    if (richtungUeberpruefenUndSicherheitenFestlegen(transi.x2, transi.y2, urX, urY, urSpieler, schrittCounter, urDir, getOppDir(Richtungen.values()[transi.dir2]))) {
                                        sicherheit[x][y][urSpieler][intDir] = 1;
                                        sicherheit[x][y][urSpieler][oppIntDir] = 1;
                                        return true;
                                    }
                                    break;
                                case 2:
                                    if (richtungUeberpruefenUndSicherheitenFestlegen(transi.x1, transi.y1, urX, urY, urSpieler, schrittCounter, urDir, getOppDir(Richtungen.values()[transi.dir1]))) {
                                        sicherheit[x][y][urSpieler][intDir] = 1;
                                        sicherheit[x][y][urSpieler][oppIntDir] = 1;
                                        return true;
                                    }
                                    break;
                            }
                            sicherheit[x][y][urSpieler][intDir] = -1;
                            return false;
                            //hat keine Transition
                        } else {
                            sicherheit[x][y][urSpieler][intDir] = 1;
                            sicherheit[x][y][urSpieler][oppIntDir] = 1;
                            return true;
                        }
                        //kein Rand
                    } else {
                        if (richtungUeberpruefenUndSicherheitenFestlegen(getNewX(x, dir), getNewY(y, dir), urX, urY, urSpieler, schrittCounter, urDir, dir)) {
                            sicherheit[x][y][urSpieler][intDir] = 1;
                            sicherheit[x][y][urSpieler][oppIntDir] = 1;
                            return true;
                        } else {
                            sicherheit[x][y][urSpieler][intDir] = -1;
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Berechnet aus den den 4 Richtungen und deren Sicherheiten/Unsicherheiten den Wert fuer alle felder
     */
    private void felderwerteAusSicherheiten() {

        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {

                int spielerAufFeld = Character.getNumericValue(spielfeld[x][y]);

                int anzahlSichereRichtungen = 0; //Max 4
                switch (spielfeld[x][y]) {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        //Eigener Spieler
                        if (spieler == Character.getNumericValue(spielfeld[x][y])) {
                            if (sicherheit[x][y][spieler][0] == 1) {
                                anzahlSichereRichtungen += 1;
                            }
                            if (sicherheit[x][y][spieler][1] == 1) {
                                anzahlSichereRichtungen += 1;
                            }
                            if (sicherheit[x][y][spieler][2] == 1) {
                                anzahlSichereRichtungen += 1;
                            }
                            if (sicherheit[x][y][spieler][3] == 1) {
                                anzahlSichereRichtungen += 1;
                            }
                            //Alle anderen Spieler
                        } else {
                            if (sicherheit[x][y][spielerAufFeld][0] == 1) {
                                anzahlSichereRichtungen += -1;
                            }
                            if (sicherheit[x][y][spielerAufFeld][1] == 1) {
                                anzahlSichereRichtungen += -1;
                            }
                            if (sicherheit[x][y][spielerAufFeld][2] == 1) {
                                anzahlSichereRichtungen += -1;
                            }
                            if (sicherheit[x][y][spielerAufFeld][3] == 1) {
                                anzahlSichereRichtungen += -1;
                            }
                        }
                        break;
                    case '0':
                        break;
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

                switch (anzahlSichereRichtungen) {

                    case -1:
                        felderwerte[x][y] = -1 * eineRichtungenSicher;
                        continue;
                    case -2:
                        felderwerte[x][y] = -1 * zweiRichtungenSicher;
                        continue;
                    case -3:
                        felderwerte[x][y] = -1 * dreiRichtungenSicher;
                        continue;
                    case -4:
                        felderwerte[x][y] = -1 * vierRichtungenSicher;
                        continue;
                    case 0:
                        switch (spielfeld[x][y]) {
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                                if (spieler == Character.getNumericValue(spielfeld[x][y])) {
                                    felderwerte[x][y] = keineRichtungSicher;
                                } else {
                                    felderwerte[x][y] = -1 * keineRichtungSicher;
                                }
                                continue;
                            case '0':
                                felderwerte[x][y] += 0;
                                continue;
                            case 'x':
                                continue;
                            case 'b':
                                continue;
                            case 'c':
                                continue;
                            case 'i':
                                continue;
                            case '-':
                                continue;
                            default:
                                break;

                        }
                        break;
                    case 1:
                        felderwerte[x][y] = eineRichtungenSicher;
                        break;
                    case 2:
                        felderwerte[x][y] = zweiRichtungenSicher;
                        break;
                    case 3:
                        felderwerte[x][y] = dreiRichtungenSicher;
                        break;
                    case 4:
                        felderwerte[x][y] = vierRichtungenSicher;

                }
            }
        }
    }

    private void mobilitaet() {
        brettsumme += anzahlGueltigeZuege * wertGueltigerZug;
    }

    private void ueberschreibsteine() {
        brettsumme += (ueberschreibsteine + ersatzsteine) * wertUeberschreibstein;
    }

    private boolean hatTransition(int x, int y, Richtungen dir) {
        return (transitionen[x][y] != null) && ((transitionen[x][y].search(x, y, dir.ordinal())) != null);
    }

    private Transition getTransition(int x, int y, Richtungen dir) {
        if (hatTransition(x, y, dir)) {
            return transitionen[x][y].search(x, y, dir.ordinal());
        } else {
            return null;
        }
    }

    private boolean istRand(int x, int y, Richtungen dir) {
        switch (dir) {
            case OBEN:
                if (y == 0) {
                    return true;
                } else return spielfeld[x][y - 1] == '-';


            case OBENRECHTS:
                if ((y == 0 && x == breite - 1) || y == 0 || x == breite - 1) {
                    return true;
                } else return spielfeld[x + 1][y - 1] == '-';

            case RECHTS:
                if (x == breite - 1) {
                    return true;
                } else return spielfeld[x + 1][y] == '-';

            case UNTENRECHTS:
                if ((y == hoehe - 1 && x == breite - 1) || y == hoehe - 1 || x == breite - 1) {
                    return true;
                } else return spielfeld[x + 1][y + 1] == '-';

            case UNTEN:
                if (y == hoehe - 1) {
                    return true;
                } else return spielfeld[x][y + 1] == '-';

            case UNTENLINKS:
                if ((y == hoehe - 1 && x == 0) || y == hoehe - 1 || x == 0) {
                    return true;
                } else return spielfeld[x - 1][y + 1] == '-';

            case LINKS:
                if (x == 0) {
                    return true;
                } else return spielfeld[x - 1][y] == '-';

            case OBENLINKS:
                if ((y == 0 && x == 0) || y == 0 || x == 0) {
                    return true;
                } else return spielfeld[x - 1][y - 1] == '-';

            default:
                return false;
        }
    }

    /**
     * Addiert die Verschiedenen Aspekte der Heuristik auf. Z.B. die Faerbbarkeit, Mobilitaet...
     */
    private void heuristikAufsummieren() {
        /*---------------------Sicherheit----------------------*/
        felderwerteAusSicherheiten();
        /*---------------------Mobilitaet----------------------*/
        mobilitaet();
        /*-----------------Ueberschreibsteine------------------*/
        ueberschreibsteine();
        /*--------------------Aufsummieren---------------------*/
        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                brettsumme += felderwerte[x][y];
            }
        }
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


