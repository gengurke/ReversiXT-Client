package HeuristikTest;

import static HeuristikTest.HeuristikHilfsFunktionen.*;

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
    private int brettsumme;
    private int breite, hoehe, spieler;
    private char[][][] spielfeld;
    private TransitionenListe[] transitionen;

    private int[][][][] sicherheit;
    private int[][] felderwerte;


    DynamischeHeuristik(Spielbrett spiel, int spieler) {
        this.spieler = spieler;
        hoehe = spiel.getHoehe();
        breite = spiel.getBreite();
        spielfeld = spiel.getSpielfeld();
        transitionen = spiel.getTransitionen();
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

    private void heuristikAufsummieren() {
        /*---------------------Sicherheit----------------------*/
        felderwerteAusSicherheiten();
        /*---------------------Mobilitaet----------------------*/

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
            } else if ((sicherheit[x][y][spielerInZelle][intDir] == -1) && (sicherheit[x][y][spielerInZelle][oppIntDir] == -1)) {
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
                                if (richtungUeberpruefenUndSicherheitenFestlegen(transi.x2, transi.y2, spielerInZelle, getOppDir(dir))) {
                                    sicherheit[x][y][spielerInZelle][intDir] = 1;
                                    sicherheit[x][y][spielerInZelle][oppIntDir] = 1;
                                    return true;
                                }
                                break;
                            case 2:
                                if (richtungUeberpruefenUndSicherheitenFestlegen(transi.x1, transi.y1, spielerInZelle, getOppDir(dir))) {
                                    sicherheit[x][y][spielerInZelle][intDir] = 1;
                                    sicherheit[x][y][spielerInZelle][oppIntDir] = 1;
                                    return true;
                                }
                                break;
                        }
                        sicherheit[x][y][spielerInZelle][intDir] = -1;
                        sicherheit[x][y][spielerInZelle][oppIntDir] = -1;
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
                        sicherheit[x][y][spielerInZelle][oppIntDir] = -1;
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    void felderwerteAusSicherheiten() {
        //Werte fuer Felder nach Unumfaerbbarkeit
        final int dreiSicher = 100;
        final int zweiSicher = 50;
        final int eineSicher = 20;
        final int ecke = 250;
        final int keineSicher = 10;


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
                        felderwerte[x][y] = -1 * eineSicher;
                        continue;
                    case -2:
                        felderwerte[x][y] = -1 * zweiSicher;
                        continue;
                    case -3:
                        felderwerte[x][y] = -1 * dreiSicher;
                        continue;
                    case -4:
                        felderwerte[x][y] = -1 * ecke;
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
                                    felderwerte[x][y] = keineSicher;
                                } else {
                                    felderwerte[x][y] = -1 * keineSicher;
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
                        felderwerte[x][y] = eineSicher;
                        break;
                    case 2:
                        felderwerte[x][y] = zweiSicher;
                        break;
                    case 3:
                        felderwerte[x][y] = dreiSicher;
                        break;
                    case 4:
                        felderwerte[x][y] = ecke;

                }
            }
        }
    }

    private boolean hatTransition(int x, int y, Richtungen dir) {
        if ((transitionen[spielfeld[x][y][2]].search(x, y, dir.ordinal())) != null) {
            return true;
        } else {
            return false;
        }
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


