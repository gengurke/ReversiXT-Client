package Main;

import java.util.LinkedList;

import static Main.HeuristikHilfsFunktionen.*;

public class DynamischeHeuristik implements Heuristik {

    /*******************Einstellungen*******************/
    //// Sicherheiten
    private final int vierRichtungenSicher = 250;
    private final int dreiRichtungenSicher = 100;
    private final int zweiRichtungenSicher = 50;
    private final int eineRichtungenSicher = 20;
    private final int keineRichtungSicher = 10;

    //// Mobilitaet
    private final int wertGueltigerZug = 10;

    //// Ueberschreibsteine
    private final int wertUeberschreibstein = 300;

    //// Bomben
    private final int wertBomben = bombenStaerkeBewerten();
    /****************Einstellungen Ende*****************/

    //Spielvariablen
    private int brettsumme;
    private int breite, hoehe, spieler, anzahlGueltigeZuege, ueberschreibsteine, bomben, bombenStaerke;
    private char[][][] spielfeld;
    private TransitionenListe[] transitionen;

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
        bomben = spiel.getBomben();
        bombenStaerke = spiel.getStaerke();

        //gueltige Zuege
        LinkedList gueltigeZuege = spiel.gueltigeZuege(spieler);
        anzahlGueltigeZuege = gueltigeZuege.getSize();

        //Arrays fuer die Heuristikberechnung
        sicherheit = new int[breite][hoehe][spiel.getSpieler() + 1][8];
        felderwerte = new int[breite][hoehe];


        heuristikwertBerechnen();
        heuristikAufsummieren();
    }

    private void heuristikwertBerechnen() {

        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                zelleUeberpruefen(x, y);
            }
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
        /*-----------------------Bomben------------------------*/
        bombenBewerten();
        /*--------------------Aufsummieren---------------------*/
        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {
                brettsumme += felderwerte[x][y];
            }
        }
    }

    private void zelleUeberpruefen(int x, int y) {
        int spielerInZelle = Character.getNumericValue(spielfeld[x][y][0]);
        for (Richtungen dir : Richtungen.values()) {
            switch (spielfeld[x][y][0]) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    richtungUeberpruefenUndSicherheitenFestlegen(x, y, spielerInZelle, dir);
                    //TODO bool nutzen
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

    private boolean richtungUeberpruefenUndSicherheitenFestlegen(int x, int y, int spielerInZelle, Richtungen dir) {
        //Wenn ueberpruefender Spieler gleich Spieler auf spielfeld
        int intDir = dir.ordinal(); // Int Repraesentation der Richtung
        int oppIntDir = getOppDir(dir).ordinal();

        if (spielerInZelle == Character.getNumericValue(spielfeld[x][y][0])) {
            //Schon sicher
            if ((sicherheit[x][y][spielerInZelle][intDir] == 1) && (sicherheit[x][y][spielerInZelle][oppIntDir] == 1)) {
                return true;
                //nicht sicher
            } else if (sicherheit[x][y][spielerInZelle][intDir] == -1) {
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
                                //TODO loop fall implementieren
                                if (richtungUeberpruefenUndSicherheitenFestlegen(transi.x2, transi.y2, spielerInZelle, getOppDir(Richtungen.values()[transi.dir2]))) {
                                    sicherheit[x][y][spielerInZelle][intDir] = 1;
                                    sicherheit[x][y][spielerInZelle][oppIntDir] = 1;
                                    return true;
                                }
                                break;
                            case 2:
                                if (richtungUeberpruefenUndSicherheitenFestlegen(transi.x1, transi.y1, spielerInZelle, getOppDir(Richtungen.values()[transi.dir1]))) {
                                    sicherheit[x][y][spielerInZelle][intDir] = 1;
                                    sicherheit[x][y][spielerInZelle][oppIntDir] = 1;
                                    return true;
                                }
                                break;
                        }
                        sicherheit[x][y][spielerInZelle][intDir] = -1;
                        return false;
                        //hat keine Transition
                    } else {
                        sicherheit[x][y][spielerInZelle][intDir] = 1;
                        sicherheit[x][y][spielerInZelle][oppIntDir] = 1;
                        return true;
                    }
                    //kein Rand
                } else {
                    if (richtungUeberpruefenUndSicherheitenFestlegen(getNewX(x, dir), getNewY(y, dir), spielerInZelle, dir)) {
                        sicherheit[x][y][spielerInZelle][intDir] = 1;
                        sicherheit[x][y][spielerInZelle][oppIntDir] = 1;
                        return true;
                    } else {
                        sicherheit[x][y][spielerInZelle][intDir] = -1;
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Berechnet aus den den 4 Richtungen und deren Sicherheiten/Unsicherheiten den Wert fuer alle felder
     */
    private void felderwerteAusSicherheiten() {

        for (int y = 0; y < hoehe; y++) {
            for (int x = 0; x < breite; x++) {

                int spielerAufFeld = Character.getNumericValue(spielfeld[x][y][0]);

                int anzahlSichereRichtungen = 0; //Max 4
                switch (spielfeld[x][y][0]) {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        //Eigener Spieler
                        if (spieler == Character.getNumericValue(spielfeld[x][y][0])) {
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
                        switch (spielfeld[x][y][0]) {
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                                if (spieler == Character.getNumericValue(spielfeld[x][y][0])) {
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
        brettsumme += ueberschreibsteine * wertUeberschreibstein;
    }

    private void bombenBewerten() {
        brettsumme += bomben * wertBomben;
    }

    private int bombenStaerkeBewerten() {
        switch (bombenStaerke) {
            case 0:
                return 50;
            case 1:
                return 100;
            case 2:
            case 3:
            case 4:
            case 5:
                return 200;
            default:
                return 100;

        }
    }

    private boolean hatTransition(int x, int y, Richtungen dir) {
        return (transitionen[spielfeld[x][y][2]].search(x, y, dir.ordinal())) != null;
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


