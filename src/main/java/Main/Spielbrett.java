package Main;

import java.io.*;
import java.util.*;

/**
 *Die Klasse Spielbrett speichert und verwaltet die Daten fuer ein Spielfeld. Dazu gehoeren die Anzahl der Spieler, Ueberschreibsteine
 *und Bomebn sowie die Staerke der Bomben, die Breite und Hoehe des Spielfeldes und die Transitionen.
 */
public class Spielbrett {
    private int Spieler,
            Ueberschreibsteine,
            Bomben,
            Staerke,
            Hoehe,
            Breite;
    private char Spielfeld[][];
    private TransitionenListe[][] Transitionen;

    private GueltigerZugListe gueltigeZuege;
    private boolean dir[], faerben[][];
    private int aktX = 0, aktY = 0, aktDir = 0;
    private int count = 0, ersatzsteine;
    private int zustaende = 0;
    public boolean ustein = false;


    public Spielbrett(String name) throws IOException {
        Init(name);
    }

    //TODO enfernen nach Heuristik Test
    public Spielbrett(String mapName, String test) throws IOException {
        heuristikTestInit(mapName);
    }


    /**
     * Alternativer Konstuktor. Wird verwendet, wenn ein Spielbrett erstellt werden muss ohne nochmals die Datei auszulesen.
     * @param spieler Anzahl der Spieler
     * @param ueberschreibsteine Anzahl der Ueberschreibsteine fuer einen Spieler
     * @param bomben Anzahl der bomben fuer einen Spieler
     * @param staerke Staerke der Bomben, d.h. Radius der zerstoert wird
     * @param hoehe Hoehe des Spielfeldes
     * @param breite Breite des Spielfeldes
     * @param spielfeld 2-dimensionales Spielfeld Array (char)
     * @param transitionen Transitionen des Spielfeldes
     * @param gueltigeZuege Liste von gueltigen Zuegen
     */
    public Spielbrett(int spieler, int ueberschreibsteine, int bomben, int staerke, int hoehe, int breite, char[][] spielfeld, TransitionenListe[][] transitionen, GueltigerZugListe gueltigeZuege) {
        Spieler = spieler;
        Ueberschreibsteine = 0;
        ersatzsteine = ueberschreibsteine;
        Bomben = bomben;
        Staerke = staerke;
        Hoehe = hoehe;
        Breite = breite;
        Spielfeld = spielfeld;
        Transitionen = transitionen;
        faerben = new boolean[Breite][Hoehe];
        this.gueltigeZuege = gueltigeZuege;
    }

    /**
     * Anhand von der X Koordinate und der Richtung wird die neue X Koordinate berechnet und zurueckgegeben
     * @param x X Koordinate des Spielfeldes
     * @param dir Richtung in die gegangen wird
     * @return neue X Koordinate oder -1 bei falscher Eingabe
     */
    private int getNewX(int x, int dir) {
        switch (dir) {
            case 0:
            case 4:
                return x;
            case 1:
            case 2:
            case 3:
                return x + 1;
            case 5:
            case 6:
            case 7:
                return x - 1;
        }
        return -1;
    }

    /**
     * Anhand von der Y Koordinate und der Richtung wird die neue Y Koordinate berechnet und zurueckgegeben
     * @param y Y Koordinate des Spielfeldes
     * @param dir Richtung in die gegangen wird
     * @return neue Y Koordinate oder -1 bei falscher Eingabe
     */
    private int getNewY(int y, int dir) {
        switch (dir) {
            case 2:
            case 6:
                return y;
            case 3:
            case 4:
            case 5:
                return y + 1;
            case 7:
            case 0:
            case 1:
                return y - 1;
        }
        return -1;
    }


    /**
     * Initialisierung der Attribute
     * @param nachricht String mit Inhalt der Datei der Spielfeldspezifikation
     * @throws IOException
     */
    private void Init(String nachricht) throws IOException {
        Reader inputString = new StringReader(nachricht);
        BufferedReader br = new BufferedReader(inputString);
        String text;
        //Spieler und Ueberschreibsteine einlesen/speichern
        text = br.readLine();
        setSpieler(Integer.parseInt(text));
        text = br.readLine();
        setUeberschreibsteine(0);
        ersatzsteine = Integer.parseInt(text);
        //Bomben und Staerke einlesen/speichern
        text = br.readLine();
        String array[] = text.split(" ");
        setBomben(Integer.parseInt(array[0]));
        setStaerke(Integer.parseInt(array[1]));
        //Hoehe und Breite einlesen/speichern
        text = br.readLine();
        array = text.split(" ");
        setHoehe(Integer.parseInt(array[0]));
        setBreite(Integer.parseInt(array[1]));
        //Spielfeld einlesen/speichern
        Spielfeld = new char[Breite][Hoehe];
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            text = br.readLine();
            array = text.split(" ");
            for (int spalte = 0; spalte < Breite; spalte++) {
                Spielfeld[spalte][zeile] = array[spalte].charAt(0);
            }
        }
        //Transitionen einlesen/speichern
        ArrayList<String> templiste = new ArrayList<>();
        while ((text = br.readLine()) != null) {
            templiste.add(text);
        }

        Transitionen = new TransitionenListe[Breite][Hoehe];
        for (int i = 0; i < templiste.size(); i++) {
            String[] textarray = templiste.get(i).split(" ");
            int x1, y1, r1, x2, y2, r2;
            Transition t;

            x1 = Short.parseShort(textarray[0]);
            y1 = Short.parseShort(textarray[1]);
            r1 = Short.parseShort(textarray[2]);
            x2 = Short.parseShort(textarray[4]);
            y2 = Short.parseShort(textarray[5]);
            r2 = Short.parseShort(textarray[6]);

            if (x1 == 9 && y1 == 0) {
                System.out.println();
            }

            t = new Transition(x1, y1, r1, x2, y2, r2);

            if (Transitionen[x1][y1] == null) {
                Transitionen[x1][y1] = new TransitionenListe();
            }
            if (Transitionen[x2][y2] == null) {
                Transitionen[x2][y2] = new TransitionenListe();
            }
            Transitionen[x1][y1].Insert(t);
            Transitionen[x2][y2].Insert(t);

        }

        dir = new boolean[8];
        faerben = new boolean[Breite][Hoehe];
        gueltigeZuege = new GueltigerZugListe();

    }

    //TODO enfernen nach Heuristik Test
    private void heuristikTestInit(String mapName) throws IOException {
        FileReader fr = new FileReader(mapName);
        BufferedReader br = new BufferedReader(fr);
        String text;
        //Spieler und Ueberschreibsteine einlesen/speichern
        text = br.readLine();
        setSpieler(Integer.parseInt(text));
        text = br.readLine();
        setUeberschreibsteine(Integer.parseInt(text));
        //Bomben und Staerke einlesen/speichern
        text = br.readLine();
        String array[] = text.split(" ");
        setBomben(Integer.parseInt(array[0]));
        setStaerke(Integer.parseInt(array[1]));
        //Hoehe und Breite einlesen/speichern
        text = br.readLine();
        array = text.split(" ");
        setHoehe(Integer.parseInt(array[0]));
        setBreite(Integer.parseInt(array[1]));
        //Spielfeld einlesen/speichern
        Spielfeld = new char[Breite][Hoehe];
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            text = br.readLine();
            array = text.split(" ");
            for (int spalte = 0; spalte < Breite; spalte++) {
                Spielfeld[spalte][zeile] = array[spalte].charAt(0);
            }
        }
        //Transitionen einlesen/speichern
        ArrayList<String> templiste = new ArrayList<>();
        while ((text = br.readLine()) != null) {
            templiste.add(text);
        }

        Transitionen = new TransitionenListe[Breite][Hoehe];
        for (int i = 0; i < templiste.size(); i++) {
            String[] textarray = templiste.get(i).split(" ");
            int x1, y1, r1, x2, y2, r2;
            Transition t;

            x1 = Short.parseShort(textarray[0]);
            y1 = Short.parseShort(textarray[1]);
            r1 = Short.parseShort(textarray[2]);
            x2 = Short.parseShort(textarray[4]);
            y2 = Short.parseShort(textarray[5]);
            r2 = Short.parseShort(textarray[6]);

            t = new Transition(x1, y1, r1, x2, y2, r2);

            if (Transitionen[x1][y1] == null) {
                Transitionen[x1][y1] = new TransitionenListe();
            }
            if (Transitionen[x2][y2] == null) {
                Transitionen[x2][y2] = new TransitionenListe();
            }
            Transitionen[x1][y1].Insert(t);
            Transitionen[x2][y2].Insert(t);
        }


        dir = new boolean[8];
        faerben = new boolean[Breite][Hoehe];
        gueltigeZuege = new GueltigerZugListe();

    }

    /**
     * Faerbt das Spielfeld um nachdem mit Methode private void bombZug(int x, int y, int staerke)
     * berechnet wurde, welche Felder umgefaerbt werden sollen
     * @param x X Koordinate an die Bombe gesetzt wird
     * @param y Y Koordinate an die Bombe gesetzt wird
     */
    public void bombZug(int x, int y) {
        gueltigeZuege.listeLoeschen();
        bombZug(x, y, Staerke);
        for (int i = 0; i < gueltigeZuege.getSize(); i++) {
            GueltigerZug gzug = gueltigeZuege.get(i);
            Spielfeld[gzug.getX()][gzug.getY()] = '-';
        }
        gueltigeZuege.listeLoeschen();
    }

    /**
     * Berechnet welche Felder vom Punkt (x,y) aus mit gegebender Staerke erreicht werden
     * @param x X Koordinate des Startpunktes
     * @param y Y Koordinate des Startpunktes
     * @param staerke Bombenstaerke, d.h. Anzahl an Schritten die man vom Startpunkt weg gehen kann
     */
    private void bombZug(int x, int y, int staerke) {
        if (0 <= x && 0 <= y && x < Breite && y < Hoehe) {
            if (Spielfeld[x][y] == '-') {
                return;
            }
            gueltigeZuege.hinzufuegen(x, y, 0);
            if (staerke > 0) {
                for (int dir = 0; dir < 8; dir++) {
                    TransitionenListe temp = Transitionen[x][y];
                    if (temp != null) {
                        Transition t = temp.search((short) x, (short) y, dir);
                        if (t != null) {
                            int number = t.getNumber(x, y, dir);
                            bombZug(t.getX(number), t.getY(number), staerke - 1);
                            continue;
                        }
                    }
                    bombZug(getNewX(x, dir), getNewY(y, dir), staerke - 1);
                }
            } else {
                return;
            }
        }
    }


    /**
     * Berechnet alle moeglichen Bombenzuege
     * @param spieler Nummer des Spielers der den Bombenzug macht
     * @param timer Timer gibt an, ob noch genug Zeit fuer den Zug vorhanden ist
     */
    public void gueltigeBombZuege(int spieler, Timer timer) {
        gueltigeZuege.listeLoeschen();
        count = 0;
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            for (int spalte = 0; spalte < Breite; spalte++) {
                if (timer != null) {
                    if (!timer.getStatus()) {
                        if (pruefeBombZug(spalte, zeile, Staerke, spieler)) {
                            gueltigeZuege.hinzufuegen(spalte, zeile, count);
                            count = 0;
                        }
                    } else {
                        gueltigeZuege.SortMaxFirst();
                        return;
                    }
                } else {
                    if (pruefeBombZug(spalte, zeile, Staerke, spieler)) {
                        gueltigeZuege.hinzufuegen(spalte, zeile, count);
                        count = 0;
                    }
                }
            }
        }
        gueltigeZuege.SortMaxFirst();
        printGueltigeZuege();
    }

    /**
     * Prueft ob der Zug mit Startpunkt (x,y) ein gueltiger Bombenzug ist.
     * @param x X Koordinate des Startpunktes
     * @param y Y Koordinate des Startpunktes
     * @param staerke Bombenstaerke, d.h. Anzahl an Schritten die man vom Startpunkt weg gehen kann
     * @param spieler Nummer des Spielers der den Bombenzug macht
     * @return true falls Zug gueltig, false falls Zug ungueltig
     */
    public boolean pruefeBombZug(int x, int y, int staerke, int spieler) {
        if (0 <= x && 0 <= y && x < Breite && y < Hoehe) {
            if (staerke > 0) {
                if (Spielfeld[x][y] == '-') {
                    return false;
                } else {
                    switch (Spielfeld[x][y]) {
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                            if (Spielfeld[x][y] != Integer.toString(spieler).charAt(0)) {
                                count++;
                            }
                            break;
                        default:
                            break;
                    }
                }
                for (int dir = 0; dir < 8; dir++) {
                    TransitionenListe temp = Transitionen[x][y];
                    if (temp != null) {
                        Transition t = temp.search((short) x, (short) y, dir);
                        if (t != null) {
                            int number = t.getNumber(x, y, dir);
                            pruefeBombZug(t.getX(number), t.getY(number), staerke - 1, spieler);
                            continue;
                        }
                    }
                    pruefeBombZug(getNewX(x, dir), getNewY(y, dir), staerke - 1, spieler);
                }
            } else {
                switch (Spielfeld[x][y]) {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        if (Spielfeld[x][y] != Integer.toString(spieler).charAt(0)) {
                            count++;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    /**
     * Fuert ganzen Zug des Spielers an dem Punkt (x,y) aus unter Beruecksichtigung von Sonderfeldern
     * D.h. es wird der gegebene Zug gemacht, das Spielfeld umgefaerbt und bei Sonderfelden
     * Ueberschreibsteine bzw. Bomben angerechnet oder Spielernummern getauscht
     * @param spieler Nummer des Spielers der den Zug macht
     * @param x X Koordinate des Zuges
     * @param y Y Koordinate des Zuges
     * @param sonderfeld gibt an, welche Option bei sonderfeldern gewaehlt werden soll
     */
    public void ganzerZug(int spieler, int x, int y, byte sonderfeld) {
        if (x >= 0 && y >= 0 && x < Breite && y < Hoehe) {
            int tausch = 0, bonus = 0;
            boolean choice = false, inversion = false, ustein = false;
            switch (sonderfeld) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    choice = true;
                    tausch = sonderfeld;
                    break;
                case 20:
                    Bomben++;
                    break;
                case 21:
                    ersatzsteine++;
                    break;
                default:
                    break;
            }
            if (Spielfeld[x][y] == 'i') {
                inversion = true;
            } else if (Spielfeld[x][y] != '0' && Spielfeld[x][y] != 'b' && Spielfeld[x][y] != 'c') {
                setUeberschreibsteine(1);
                setErsatzsteine(getErsatzsteine() - 1);
                ustein = true;
            }

            Zug(spieler, x, y);
            Faerben(spieler, x, y);

            if (choice) {
                if (spieler != tausch) {
                    for (int spalte = 0; spalte < Breite; spalte++) {
                        for (int zeile = 0; zeile < Hoehe; zeile++) {
                            if (spieler == Character.getNumericValue(Spielfeld[spalte][zeile])) {
                                Spielfeld[spalte][zeile] = Integer.toString(tausch).charAt(0);
                            } else if (tausch == Character.getNumericValue(Spielfeld[spalte][zeile])) {
                                Spielfeld[spalte][zeile] = Integer.toString(spieler).charAt(0);
                            }
                        }
                    }
                }
            } else if (inversion) {
                for (int spalte = 0; spalte < Breite; spalte++) {
                    for (int zeile = 0; zeile < Hoehe; zeile++) {
                        switch (Spielfeld[spalte][zeile]) {
                            case '0':
                            case '-':
                            case 'b':
                            case 'c':
                            case 'i':
                            case 'x':
                                break;
                            default:
                                int i = Character.getNumericValue(Spielfeld[spalte][zeile]);
                                if (i > 0 && i <= Spieler) {
                                    Spielfeld[spalte][zeile] = Integer.toString(i % Spieler + 1).charAt(0);
                                }
                                break;
                        }
                    }
                }
            } else if (ustein) {
                setUeberschreibsteine(0);
            }
        } else {
            System.out.println("Ungueltiger Zug!");
        }
    }

    /**
     * Faerbt alle Felder um, die bei dem gueltigen Zug (x,y) geaendert werden (in die Nummer des Spielers)
     *
     * @param spieler Spieler der den Zug gemacht hat
     * @param x X Koordinate des Zuges
     * @param y Y Koordinate des Zuges
     */
    public void Faerben(int spieler, int x, int y) {
        Spielfeld[x][y] = '0';

        Faerben(spieler, x, y, dir);
        for (int spalte = 0; spalte < Breite; spalte++) {
            for (int zeile = 0; zeile < Hoehe; zeile++) {
                if (faerben[spalte][zeile]) {
                    Spielfeld[spalte][zeile] = Integer.toString(spieler).charAt(0);
                }
            }
        }
        faerben = new boolean[Breite][Hoehe];
        Spielfeld[x][y] = Integer.toString(spieler).charAt(0);
    }

    /**
     * Berechnet alle Felder, die bei dem gueltigen Zug (x,y) des Spielers gefaerbt werden
     * @param spieler Spieler der den Zug gemacht hat
     * @param x X Koordinate des Zuges
     * @param y Y Koordinate des Zuges
     * @param direction Array das angibt in Welche Richtungen gefaerbt werden kann
     */
    private void Faerben(int spieler, int x, int y, boolean[] direction) {
        if (spieler == Character.getNumericValue(Spielfeld[x][y])) {
            return;
        } else {
            int newx, newy;
            for (int i = 0; i < direction.length; i++) {
                if (direction[i]) {
                    newx = x;
                    newy = y;
                    faerben[x][y] = true;
                    TransitionenListe temp = Transitionen[newx][newy];
                    if (temp != null) {
                        Transition t = temp.search(x, y, i);
                        if (t != null) {
                            int number = t.getNumber(x, y, i);
                            boolean[] d = new boolean[8];
                            if (number == 1) {
                                d[t.getOppDir(t.dir2)] = true;
                            } else {
                                d[t.getOppDir(t.dir1)] = true;
                            }
                            Faerben(spieler, t.getX(number), t.getY(number), d);
                            continue;
                        }
                    }

                    boolean[] newdir = new boolean[8];
                    newdir[i] = true;
                    newx = getNewX(x, i);
                    newy = getNewY(y, i);
                    Faerben(spieler, newx, newy, newdir);
                }
            }
        }
    }

    /**
     * Berechnet und speichert alle gueltigen Zuege fuer das aktuelle Spielfeld und den gegebenen Spieler
     * @param spieler Spieler fuer den gueltige Zuege berechnet werden sollen
     * @param sort gibt an, ob die Zuege sortiert werden sollen (true) oder nicht (false)
     */
    public void gueltigeZuege(int spieler, boolean sort) {
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            for (int spalte = 0; spalte < Breite; spalte++) {
                if (Zug(spieler, spalte, zeile)) {
                    if(sort) {
                        char[][] temp = kopiereSpielfeld(this.Spielfeld);
                        Faerben(spieler, spalte, zeile);
                        TrivialeHeuristik h = new TrivialeHeuristik(this, spieler);
                        Spielfeld = temp;
                        gueltigeZuege.hinzufuegen(spalte, zeile, h.getSpielbewertung());
                    } else {
                        gueltigeZuege.hinzufuegen(spalte, zeile, 0);
                    }
                }
            }
        }
    }

    /**
     * Prueft ob der Zug (x,y) ein gueltiger Zug fuer den gegebenen Spieler ist
     * @param spieler Spieler, der den Zug macht
     * @param x X Koordinate des Zuges
     * @param y Y Koordinate des Zuges
     * @return true, falls Zug gueltig und false, falls Zug ungueltig
     */
    public boolean Zug(int spieler, int x, int y) {
        dir = new boolean[8];
        boolean faerben = false;
        aktX = x;
        aktY = y;
        if (x >= 0 && y >= 0 && x < Breite && y < Hoehe && spieler > 0 && spieler <= Spieler) {
            char a = Spielfeld[x][y];
            switch (a) {
                case '0':
                case 'b':
                case 'c':
                case 'i':
                    Spielfeld[x][y] = Integer.toString(spieler).charAt(0);
                    int newx, newy;

                    for (int i = 0; i < 8; i++) {
                        count = 0;
                        newx = getNewX(x, i);
                        newy = getNewY(y, i);
                        aktDir = i;
                        if (pruefeRichtung(spieler, newx, newy, aktDir)) {
                            faerben = true;
                        }
                    }

                    TransitionenListe t = Transitionen[x][y];

                    if (t != null) {
                        for (int i = 0; i < 8; i++) {
                            Transition trans = t.search(x, y, i);
                            if (trans != null) {
                                int number = trans.getNumber(x, y, i);
                                int newdir;
                                if (number == 1) {
                                    newdir = trans.getOppDir(trans.dir2);
                                } else {
                                    newdir = trans.getOppDir(trans.dir1);
                                }
                                if (number == 1) {
                                    aktDir = trans.dir1;
                                } else {
                                    aktDir = trans.dir2;
                                }
                                count = 0;
                                if (pruefeRichtung(spieler, trans.getX(number), trans.getY(number), newdir)) {
                                    faerben = true;
                                }
                            }
                        }
                    }
                    Spielfeld[x][y] = a;
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    if (hatUeberschreibsteine()) {
                        Spielfeld[x][y] = '0';
                        faerben = Zug(spieler, x, y);
                    } else {
                        faerben = false;
                    }
                    Spielfeld[x][y] = a;
                    break;
                case 'x':
                    if (hatUeberschreibsteine()) {
                        Spielfeld[x][y] = '0';
                        Zug(spieler, x, y);
                        faerben = true;
                    } else {
                        faerben = false;
                    }
                    Spielfeld[x][y] = a;
                    break;
                case '-':
                    break;
                default:
                    break;
            }
        }
        return faerben;
    }

    /**
     * Prueft ob gegebender Spieler vom Startpunkt (x,y) in gegebener Richtung "dir" faerben kann
     * @param spieler Spieler, der den Zug macht
     * @param x X Koordinate des Zuges
     * @param y Y Koordinate des Zuges
     * @param dir direction gibt die Richtung an, die gerade geprueft wird
     * @return true, falls in dieser Richtung Faerbung moeglich ist, sonst false
     */
    private boolean pruefeRichtung(int spieler, int x, int y, int dir) {
        while (x < Breite && x >= 0 && y < Hoehe && y >= 0) {
            char value = Spielfeld[x][y];
            switch (value) {
                case '0':
                case 'b':
                case 'c':
                case 'i':
                case '-':
                    x = Breite;
                    y = Hoehe;
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case 'x':
                    if (spieler == Character.getNumericValue(value)) {
                        if (aktX != x || aktY != y) {
                            if (count > 0) {
                                this.dir[aktDir] = true;
                                return true;
                            }
                        }
                        x = Breite;
                        y = Hoehe;
                    } else {
                        TransitionenListe temp = Transitionen[x][y];
                        if (temp == null) {
                            x = getNewX(x, dir);
                            y = getNewY(y, dir);
                        } else {
                            Transition t = temp.search(x, y, dir);
                            if (t != null) {
                                int number = t.getNumber(x, y, dir);
                                if (number == 1) {
                                    count++;
                                    return pruefeRichtung(spieler, t.getX(number), t.getY(number), t.getOppDir(t.dir2));
                                } else {
                                    count++;
                                    return pruefeRichtung(spieler, t.getX(number), t.getY(number), t.getOppDir(t.dir1));
                                }
                            } else {
                                x = getNewX(x, dir);
                                y = getNewY(y, dir);
                            }
                        }
                    }
                    break;
                default:
                    x = Breite;
                    y = Hoehe;
                    break;
            }
            count++;
        }
        return false;
    }

    /**
     * Berechnet mit Hilfe des Alpha-Beta-Algorithmus den besten Zug fuer den gegebenen Spiler mit gegebener Tiefe
     * @param tiefe Tiefe bzw. Anzahl der Zuege, die im Voraus berechnet werden
     * @param spieler Spieler, der den Zug macht
     * @param timer Timer gibt an, ob noch genug Zeit fuer den Zug vorhanden ist
     * @param alpha ist der bisher beste Zug fuer den Spieler
     * @param beta ist der bisher schlechteste Zug fuer den Spieler
     * @param sort gibt an, ob die Zuege sortiert werden sollen (true) oder nicht (false)
     * @return int Array mit den x und y Koordinaten des besten Zuges, Wahl bei Sonderfeldern und Wert der Heuristik fuer den Zug
     */
    public int[] alphaBeta(int tiefe, int spieler, Timer timer, int alpha, int beta, boolean sort) {
        int x = -1, y = -1, anzahlsteine = this.getErsatzsteine(), anzahlbomben = this.getBomben();
        byte sonderfeld = 1;
        int[] zug = new int[4];
        GueltigerZug gzug;
        ABKnoten knoten = new ABKnoten(alpha, beta, Integer.MIN_VALUE);
        zustaende = 0;

        this.gueltigeZuege(spieler,sort);
        if (sort) {
            this.gueltigeZuege.SortMaxFirst();
        }

        if (gueltigeZuege.getSize() == 0) {
            if (ersatzsteine > 0) {
                setUeberschreibsteine(1);
                this.gueltigeZuege(spieler,sort);
                this.gueltigeZuege.SortMaxFirst();
            }
            if (this.gueltigeZuege.getSize() == 0) {
                PrintSpielfeld();
                System.exit(-1);
            }
            zug[0] = this.gueltigeZuege.getHead().getX();
            zug[1] = this.gueltigeZuege.getHead().getY();
            if (timer != null) {
                timer.setIstFertig(true);
            }
            zustaende = 1;
            return zug;
        }

        char[][] feld = kopiereSpielfeld(this.Spielfeld);
        GueltigerZugListe liste = this.kopiereGZugListe();
        if (tiefe == 0) {
            int wert = 0;
            TrivialeHeuristik h = new TrivialeHeuristik(this, spieler);
            wert = h.getSpielbewertung();
            zug[0] = gueltigeZuege.get(0).getX();
            zug[1] = gueltigeZuege.get(0).getY();
            zug[3] = wert;
            if (this.Spielfeld[zug[0]][zug[1]] == 'c') {
                for (int i = 1; i <= Spieler; i++) {
                    h = new TrivialeHeuristik(this, i);
                    if (wert < h.getSpielbewertung()) {
                        wert = h.getSpielbewertung();
                        zug[2] = i;
                        zug[3] = wert;
                    }
                }
            } else if (this.Spielfeld[zug[0]][zug[1]] == 'b') {
                zug[2] = 21;
                ustein = true;
            }
            zustaende = 1;
            return zug;
        }

        for (int j = 0; j < this.gueltigeZuege.getSize(); j++) {
            if (timer != null && timer.getStatus()) {
                this.setSpielfeld(feld);
                this.setErsatzsteine(anzahlsteine);
                this.setBomben(anzahlbomben);
                this.setGueltigeZuege(liste);
                return null;
            }
            gzug = this.gueltigeZuege.get(j);
            byte start = 0, ende = 0;
            int gx = gzug.getX(), gy = gzug.getY(), wert;
            Object temp;

            if (this.Spielfeld[gx][gy] == 'c') {
                start = 1;
                ende = (byte) this.Spieler;
                ustein = true;
            } else if (this.Spielfeld[gx][gy] == 'b') {
                start = 21;
                ende = 21;
                ustein = true;
            }
            for (byte i = start; i <= ende; i++) {
                this.ganzerZug(spieler, gx, gy, i);
                zustaende++;
                if (tiefe > 0) {
                    temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, spieler % this.Spieler + 1, this, sonderfeld, timer, sort);
                    if (temp == null) {
                        //System.out.println("Abbruch Tiefe: "+tiefe);
                        this.setSpielfeld(feld);
                        this.setErsatzsteine(anzahlsteine);
                        this.setBomben(anzahlbomben);
                        this.setGueltigeZuege(liste);
                        return null;
                    } else {
                        wert = (int) temp;
                    }
                } else {
                    DynamischeHeuristik h = new DynamischeHeuristik(this, spieler);
                    wert = h.getSpielbewertung();
                }
                if (knoten.getAlpha() < wert) {
                    knoten.setAlpha(wert);
                    knoten.setWert(wert);

                    x = gx;
                    y = gy;
                    sonderfeld = i;

                }
                this.setSpielfeld(kopiereSpielfeld(feld));
                this.setGueltigeZuege(liste.clone());
                this.setErsatzsteine(anzahlsteine);
                this.setBomben(anzahlbomben);
            }
        }
        this.setSpielfeld(feld);
        this.setErsatzsteine(anzahlsteine);
        this.setBomben(anzahlbomben);
        this.setGueltigeZuege(liste);

        if (x == -1 || y == -1) {
            zug[3] = Integer.MIN_VALUE;
        } else {
            zug[0] = x;
            zug[1] = y;
            zug[2] = sonderfeld;
            zug[3] = knoten.getWert();
        }
        return zug;
    }

    /**
     * Wird von oberer Methode aufgerufen (Rekursion)
     * @param alpha ist der bisher beste Zug fuer den Spieler
     * @param beta ist der bisher schlechteste Zug fuer den Spieler
     * @param tiefe Tiefe bzw. Anzahl der Zuege, die im Voraus berechnet werden
     * @param spieler Spieler, fuer den der beste Zug berechnet wird
     * @param aktuellerSpieler Spieler, der bei der Berechnung im Voraus gerade den Zug macht
     * @param spiel aktuelles Spielfeld
     * @param sonderfeld Wahl bei Sonderfeldern
     * @param timer Timer gibt an, ob noch genug Zeit fuer den Zug vorhanden ist
     * @param sort gibt an, ob die Zuege sortiert werden sollen (true) oder nicht (false)
     * @return null falls keine Zeit mehr vorhanden ist, int Wert der Heuristik fuer den Zug sonst
     */
    private Object alphaBeta(int alpha, int beta, int tiefe, int spieler, int aktuellerSpieler, Spielbrett spiel, byte sonderfeld, Timer timer, boolean sort) {
        if (timer != null && timer.getStatus()) {
            return null;
        }
        if (tiefe == 0) {
            DynamischeHeuristik h = new DynamischeHeuristik(spiel, spieler);
            int w = h.getSpielbewertung();
            return w;
        } else {
            ABKnoten knoten;
            int anzahlsteine = spiel.getErsatzsteine();
            boolean maxFirst;

            if (spieler == aktuellerSpieler) { // MAX
                knoten = new ABKnoten(alpha, beta, Integer.MIN_VALUE);
                maxFirst = true;
            } else { // MIN
                knoten = new ABKnoten(alpha, beta, Integer.MAX_VALUE);
                maxFirst = false;
            }
            GueltigerZug gzug;
            spiel.gueltigeZuege.listeLoeschen();
            spiel.gueltigeZuege(aktuellerSpieler,sort);
            if (sort) {
                if (maxFirst) {
                    spiel.gueltigeZuege.SortMaxFirst();
                } else {
                    spiel.gueltigeZuege.SortMinFirst();
                }
            }
            GueltigerZugListe gliste = spiel.kopiereGZugListe();
            char[][] feld = kopiereSpielfeld(spiel.Spielfeld);
            int wert;
            Object temp;

            if (spiel.gueltigeZuege.getSize() > 0) {
                for (int i = 0; i < spiel.gueltigeZuege.getSize(); i++) {
                    gzug = spiel.gueltigeZuege.get(i);
                    spiel.ganzerZug(aktuellerSpieler, gzug.getX(), gzug.getY(), sonderfeld);
                    zustaende++;
                    if (spieler == aktuellerSpieler) { // MAX
                        temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, aktuellerSpieler % spiel.Spieler + 1, spiel, sonderfeld, timer, sort);
                        if (temp == null) {
                            //System.out.println("Abbruch Tiefe: "+tiefe);
                            spiel.setSpielfeld(kopiereSpielfeld(feld));
                            spiel.setErsatzsteine(anzahlsteine);
                            setErsatzsteine(anzahlsteine);
                            spiel.setGueltigeZuege(gliste);
                            return null;
                        } else {
                            wert = (int) temp;
                        }

                        if (knoten.getWert() < wert) {
                            knoten.setWert(wert);
                        }
                        if (knoten.getAlpha() < wert) {
                            knoten.setAlpha(wert);
                        }
                        if (knoten.getBeta() < wert) {
                            i = gliste.getSize();
                        }
                    } else { // MIN
                        temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, aktuellerSpieler % spiel.Spieler + 1, spiel, sonderfeld, timer, sort);
                        if (temp == null) {
                            //System.out.println("Abbruch Tiefe: "+tiefe);
                            spiel.setSpielfeld(kopiereSpielfeld(feld));
                            spiel.setErsatzsteine(anzahlsteine);
                            setErsatzsteine(anzahlsteine);
                            spiel.setGueltigeZuege(gliste);
                            return null;
                        } else {
                            wert = (int) temp;
                        }
                        if (knoten.getWert() > wert) {
                            knoten.setWert(wert);
                        }
                        if (knoten.getBeta() > wert) {
                            knoten.setBeta(wert);
                        }
                        if (knoten.getAlpha() > wert) {
                            i = gliste.getSize();
                        }
                    }
                    spiel.setSpielfeld(kopiereSpielfeld(feld));
                    spiel.setErsatzsteine(anzahlsteine);
                    spiel.setGueltigeZuege(gliste.clone());
                }
            } else {
                zustaende++;
                temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, aktuellerSpieler % spiel.Spieler + 1, spiel, sonderfeld, timer, sort);
                if (temp == null) {
                    //System.out.println("Abbruch Tiefe: "+tiefe);
                    spiel.setSpielfeld(kopiereSpielfeld(feld));
                    spiel.setErsatzsteine(anzahlsteine);
                    setErsatzsteine(anzahlsteine);
                    spiel.setGueltigeZuege(gliste);
                    return null;
                } else {
                    wert = (int) temp;
                }
                spiel.setSpielfeld(kopiereSpielfeld(feld));
                spiel.setErsatzsteine(anzahlsteine);
                setErsatzsteine(anzahlsteine);
                spiel.setGueltigeZuege(gliste);
                return wert;
            }
            spiel.getGueltigeZuege().listeLoeschen();
            return knoten.getWert();
        }
    }

    /**
     * Berechnet mit Hilfe des Paranoid-Algorithmus den besten Zug fuer den gegebenen Spiler mit gegebener Tiefe
     * @param tiefe Tiefe bzw. Anzahl der Zuege, die im Voraus berechnet werden
     * @param spieler Spieler, der den Zug macht
     * @param timer Timer gibt an, ob noch genug Zeit fuer den Zug vorhanden ist
     * @param alpha ist der bisher beste Zug fuer den Spieler
     * @param beta ist der bisher schlechteste Zug fuer den Spieler
     * @param sort gibt an, ob die Zuege sortiert werden sollen (true) oder nicht (false)
     * @return int Array mit den x und y Koordinaten des besten Zuges, Wahl bei Sonderfeldern und Wert der Heuristik fuer den Zug
     */
    public int[] Paranoid(int tiefe, int spieler, Timer timer, int alpha, int beta, boolean sort) {
        int x = -1, y = -1, anzahlsteine = this.getErsatzsteine(), anzahlbomben = this.getBomben();
        byte sonderfeld = 1;
        int[] zug = new int[4];
        GueltigerZug gzug;
        ABKnoten knoten = new ABKnoten(alpha, beta, Integer.MIN_VALUE);
        zustaende = 0;

        this.gueltigeZuege(spieler,sort);
        if (sort) {
            this.gueltigeZuege.SortMaxFirst();
        }

        if (gueltigeZuege.getSize() == 0) {
            if (ersatzsteine > 0) {
                setUeberschreibsteine(1);
                this.gueltigeZuege(spieler,sort);
                this.gueltigeZuege.SortMaxFirst();
            }
            if (this.gueltigeZuege.getSize() == 0) {
                PrintSpielfeld();
                System.exit(-1);
            }
            zug[0] = this.gueltigeZuege.getHead().getX();
            zug[1] = this.gueltigeZuege.getHead().getY();
            if (timer != null) {
                timer.setIstFertig(true);
            }
            zustaende = 1;
            return zug;
        }

        char[][] feld = kopiereSpielfeld(this.Spielfeld);
        GueltigerZugListe liste = this.kopiereGZugListe();
        if (tiefe == 0) {
            int wert = 0;
            TrivialeHeuristik h = new TrivialeHeuristik(this, spieler);
            wert = h.getSpielbewertung();
            zug[0] = gueltigeZuege.get(0).getX();
            zug[1] = gueltigeZuege.get(0).getY();
            zug[3] = wert;
            if (this.Spielfeld[zug[0]][zug[1]] == 'c') {
                for (int i = 1; i <= Spieler; i++) {
                    h = new TrivialeHeuristik(this, i);
                    if (wert < h.getSpielbewertung()) {
                        wert = h.getSpielbewertung();
                        zug[2] = i;
                        zug[3] = wert;
                    }
                }
            } else if (this.Spielfeld[zug[0]][zug[1]] == 'b') {
                zug[2] = 21;
                ustein = true;
            }
            zustaende = 1;
            return zug;
        }
        for (int j = 0; j < this.gueltigeZuege.getSize(); j++) {
            if (timer != null && timer.getStatus()) {
                this.setSpielfeld(feld);
                this.setErsatzsteine(anzahlsteine);
                this.setBomben(anzahlbomben);
                this.setGueltigeZuege(liste);
                return null;
            }
            gzug = this.gueltigeZuege.get(j);
            byte start = 0, ende = 0;
            int gx = gzug.getX(), gy = gzug.getY(), wert;
            Object temp;

            if (this.Spielfeld[gx][gy] == 'c') {
                start = 1;
                ende = (byte) this.Spieler;
                ustein = true;
            } else if (this.Spielfeld[gx][gy] == 'b') {
                start = 21;
                ende = 21;
                ustein = true;
            }
            for (byte i = start; i <= ende; i++) {
                this.ganzerZug(spieler, gx, gy, i);
                zustaende++;
                temp = Paranoid(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, spieler % this.Spieler + 1, this, sonderfeld, timer, sort);
                if (temp == null) {
                    //System.out.println("Abbruch Tiefe: "+tiefe);
                    this.setSpielfeld(feld);
                    this.setErsatzsteine(anzahlsteine);
                    this.setBomben(anzahlbomben);
                    this.setGueltigeZuege(liste);
                    return null;
                } else {
                    wert = (int) temp;
                }
                if (knoten.getAlpha() < wert) {
                    knoten.setAlpha(wert);
                    knoten.setWert(wert);

                    x = gx;
                    y = gy;
                    sonderfeld = i;

                }
                this.setSpielfeld(kopiereSpielfeld(feld));
                this.setGueltigeZuege(liste.clone());
                this.setErsatzsteine(anzahlsteine);
                this.setBomben(anzahlbomben);
            }
        }
        this.setSpielfeld(feld);
        this.setErsatzsteine(anzahlsteine);
        this.setBomben(anzahlbomben);
        this.setGueltigeZuege(liste);

        if (x == -1 || y == -1) {
            zug[3] = Integer.MIN_VALUE;
        } else {
            zug[0] = x;
            zug[1] = y;
            zug[2] = sonderfeld;
            zug[3] = knoten.getWert();
        }
        return zug;
    }

    /**
     * Wird von oberer Methode aufgerufen (Rekursion)
     * @param alpha ist der bisher beste Zug fuer den Spieler
     * @param beta ist der bisher schlechteste Zug fuer den Spieler
     * @param tiefe Tiefe bzw. Anzahl der Zuege, die im Voraus berechnet werden
     * @param spieler Spieler, fuer den der beste Zug berechnet wird
     * @param aktuellerSpieler Spieler, der bei der Berechnung im Voraus gerade den Zug macht
     * @param spiel aktuelles Spielfeld
     * @param sonderfeld Wahl bei Sonderfeldern
     * @param timer Timer gibt an, ob noch genug Zeit fuer den Zug vorhanden ist
     * @param sort gibt an, ob die Zuege sortiert werden sollen (true) oder nicht (false)
     * @return null falls keine Zeit mehr vorhanden ist, int Wert der Heuristik fuer den Zug sonst
     */
    private Object Paranoid(int alpha, int beta, int tiefe, int spieler, int aktuellerSpieler , Spielbrett spiel, byte sonderfeld, Timer timer, boolean sort) {
        if (timer != null && timer.getStatus()) {
            return null;
        }
        if (tiefe == 0) {
            DynamischeHeuristik h = new DynamischeHeuristik(spiel, spieler);
            int w = h.getSpielbewertung();
            return w;
        } else {
            ABKnoten knoten;
            int anzahlsteine = spiel.getErsatzsteine();
            boolean maxFirst;

            if (spieler == aktuellerSpieler ) { // MAX
                knoten = new ABKnoten(alpha, beta, Integer.MIN_VALUE);
                maxFirst = true;
            } else { // MIN
                knoten = new ABKnoten(alpha, beta, Integer.MAX_VALUE);
                maxFirst = false;
            }
            GueltigerZug gzug;
            spiel.gueltigeZuege.listeLoeschen();
            spiel.gueltigeZuege(aktuellerSpieler ,sort);
            if (sort) {
                if (maxFirst) {
                    spiel.gueltigeZuege.SortMaxFirst();
                } else {
                    spiel.gueltigeZuege.SortMinFirst();
                }
            }
            GueltigerZugListe gliste = spiel.kopiereGZugListe();
            char[][] feld = kopiereSpielfeld(spiel.Spielfeld);
            int wert;
            Object temp;

            if (spiel.gueltigeZuege.getSize() > 0) {
                for (int i = 0; i < spiel.gueltigeZuege.getSize(); i++) {
                    gzug = spiel.gueltigeZuege.get(i);
                    spiel.ganzerZug(aktuellerSpieler , gzug.getX(), gzug.getY(), sonderfeld);
                    zustaende++;
                    if (spieler == aktuellerSpieler ) { // MAX
                        temp = Paranoid(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, aktuellerSpieler  % spiel.Spieler + 1, spiel, sonderfeld, timer, sort);
                        if (temp == null) {
                            spiel.setSpielfeld(kopiereSpielfeld(feld));
                            spiel.setErsatzsteine(anzahlsteine);
                            setErsatzsteine(anzahlsteine);
                            spiel.setGueltigeZuege(gliste);
                            return null;
                        } else {
                            wert = (int) temp;
                        }

                        if (knoten.getWert() < wert) {
                            knoten.setWert(wert);
                        }
                        if (knoten.getAlpha() < wert) {
                            knoten.setAlpha(wert);
                        }
                    } else { // MIN
                        temp = Paranoid(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, aktuellerSpieler  % spiel.Spieler + 1, spiel, sonderfeld, timer, sort);
                        if (temp == null) {
                            spiel.setSpielfeld(kopiereSpielfeld(feld));
                            spiel.setErsatzsteine(anzahlsteine);
                            setErsatzsteine(anzahlsteine);
                            spiel.setGueltigeZuege(gliste);
                            return null;
                        } else {
                            wert = (int) temp;
                        }
                        if (knoten.getWert() > wert) {
                            knoten.setWert(wert);
                        }
                        if (knoten.getBeta() > wert) {
                            knoten.setBeta(wert);
                        }
                    }
                    spiel.setSpielfeld(kopiereSpielfeld(feld));
                    spiel.setErsatzsteine(anzahlsteine);
                    spiel.setGueltigeZuege(gliste.clone());
                }
            } else {
                zustaende++;
                temp = Paranoid(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, spieler, aktuellerSpieler  % spiel.Spieler + 1, spiel, sonderfeld, timer, sort);
                if (temp == null) {
                    spiel.setSpielfeld(kopiereSpielfeld(feld));
                    spiel.setErsatzsteine(anzahlsteine);
                    setErsatzsteine(anzahlsteine);
                    spiel.setGueltigeZuege(gliste);
                    return null;
                } else {
                    wert = (int) temp;
                }
                spiel.setSpielfeld(kopiereSpielfeld(feld));
                spiel.setErsatzsteine(anzahlsteine);
                setErsatzsteine(anzahlsteine);
                spiel.setGueltigeZuege(gliste);
                return wert;
            }
            spiel.getGueltigeZuege().listeLoeschen();
            return knoten.getWert();
        }
    }

    /**
     * Get und Set Methoden fuer die Attribute
     */

    public void setSpieler(int spieler) {
        Spieler = spieler;
    }

    public void setUeberschreibsteine(int ueberschreibsteine) {
        Ueberschreibsteine = ueberschreibsteine;
    }

    public void setErsatzsteine(int ersatzsteine) {
        this.ersatzsteine = ersatzsteine;
    }

    public void setBomben(int bomben) {
        Bomben = bomben;
    }

    public void setStaerke(int staerke) {
        Staerke = staerke;
    }

    public void setHoehe(int hoehe) {
        Hoehe = hoehe;
    }

    public void setBreite(int breite) {
        Breite = breite;
    }

    public void setSpielfeld(char[][] spielfeld) {
        Spielfeld = spielfeld;
    }

    public void setTransitionen(TransitionenListe[][] transitionen) {
        Transitionen = transitionen;
    }

    public int getSpieler() {
        return Spieler;
    }

    public int getUeberschreibsteine() {
        return Ueberschreibsteine;
    }

    public int getErsatzsteine() {
        return ersatzsteine;
    }

    public boolean hatUeberschreibsteine() {
        if (Ueberschreibsteine > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setZustaende(int z) {
        zustaende = z;
    }

    public int getZustaende() {
        return zustaende;
    }

    public int getBomben() {
        return Bomben;
    }

    public int getStaerke() {
        return Staerke;
    }

    public int getHoehe() {
        return Hoehe;
    }

    public int getBreite() {
        return Breite;
    }

    public char[][] getSpielfeld() {
        return Spielfeld;
    }

    public TransitionenListe[][] getTransitionen() {
        return Transitionen;
    }

    public GueltigerZugListe getGueltigeZuege() {
        return gueltigeZuege;
    }

    public void setGueltigeZuege(GueltigerZugListe liste) {
        gueltigeZuege.set(liste);
    }

    /**
     * Gibt echte Kopie des Spielfeldes "feld" zurueck
     * @param feld Spielfeld, das kopiert werden soll
     * @return kopiertes Feld
     */
    private char[][] kopiereSpielfeld(char[][] feld) {
        char[][] clone = new char[feld.length][];

        for (int i = 0; i < feld.length; i++) {
            clone[i] = feld[i].clone();
        }
        return clone;
    }

    /**
     * Gibt echte Kopie der gueltigeZuege Liste zurueck
     * @return kopierte gueltige Zug Liste
     */
    private GueltigerZugListe kopiereGZugListe() {
        return gueltigeZuege.clone();
    }


    /**
     * Formatierter String des Spielfeldes zur Ausgabe
     * @return Spielfeld als String
     */
    private String spielfeldToString() {
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < Hoehe; i++) {
            for (int j = 0; j < Breite; j++) {
                text.append(Spielfeld[j][i] + " ");
            }
            text.append("\n");
        }
        return text.toString();
    }

    /**
     * Formatierter String der Transitionen zur Ausgabe
     * @return Transitionen als String
     */
    private String transitionenToString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < Breite; i++) {
            for (int j = 0; j < Hoehe; j++) {
                if (Transitionen[i][j] != null) {
                    sb.append(Transitionen[i][j].toString());
                }
            }
        }
        return sb.toString();
    }

    public void PrintSpielfeld() {
        System.out.println(spielfeldToString());
    }

    public void printGueltigeZuege() {
        System.out.println("Moegliche Zuege:");
        gueltigeZuege.Print();
    }

    /**
     * Formatierter String des Spielbretts zur Ausgabe
     * @return Spielbrett als String
     */
    @Override
    public String toString() {
        return "Spieler: " + Spieler + " Steine: " + Ueberschreibsteine + " Bomben: " + Bomben + " Staerke: " + Staerke + " Hoehe: " + Hoehe + " Breite: " + Breite
                + "\n\n" + spielfeldToString() + "\n" + "Transitionen:\n" + transitionenToString();
    }
}
