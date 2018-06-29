package Main;

import java.io.*;
import java.util.*;

public class Spielbrett {
    private int Spieler,
            Ueberschreibsteine,
            Bomben,
            Staerke,
            Hoehe,
            Breite;
    private char Spielfeld[][][];
    private TransitionenListe[][] Transitionen;
    private GueltigerZugListe gueltigeZuege;
    private boolean dir[], faerben[][];
    private int aktX = 0, aktY = 0, aktDir = 0;
    private int count = 0, zustaende = 0, ersatzsteine;
    public long[] zustande = new long[500], zeit_zustand = new long[500];
    public boolean ustein = false;
    private static int j = 0;

    public Spielbrett(String name) throws IOException {
        Init(name);
    }

    //TODO enfernen nach Heuristik Test
    public Spielbrett(String mapName, String test) throws IOException {
        heuristikTestInit(mapName);
    }

    public Spielbrett(int s, int u, int b, int st, int h, int br, char[][][] sp, TransitionenListe[][] t, GueltigerZugListe gl) {
        Spieler = s;
        Ueberschreibsteine = 0;
        ersatzsteine = u;
        Bomben = b;
        Staerke = st;
        Hoehe = h;
        Breite = br;
        Spielfeld = sp;
        Transitionen = t;
        faerben = new boolean[Breite][Hoehe];
        gueltigeZuege = gl;
    }

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
        Spielfeld = new char[Breite][Hoehe][12];
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            text = br.readLine();
            array = text.split(" ");
            for (int spalte = 0; spalte < Breite; spalte++) {
                Spielfeld[spalte][zeile][0] = array[spalte].charAt(0);
                Spielfeld[spalte][zeile][2] = 0;
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


            /*if (Spielfeld[x1][y1][2] != 0 && Spielfeld[x2][y2][2] != 0) {
                char value1 = Spielfeld[x1][y1][2];
                char value2 = Spielfeld[x2][y2][2];
                if (value1 != value2) {
                    Transition trans = Transitionen[value2].getHead();
                    while (trans != null) {
                        Spielfeld[trans.x1][trans.y1][2] = value1;
                        Spielfeld[trans.x2][trans.y2][2] = value1;
                        Transitionen[value1].Insert(trans);
                        trans = trans.getNext();
                    }
                }
                Transitionen[value1].Insert(t);

            } else if (Spielfeld[x1][y1][2] != 0) {
                char value = Spielfeld[x1][y1][2];
                Transitionen[value].insert(t);
                Spielfeld[x2][y2][2] = value;
            } else if (Spielfeld[x2][y2][2] != 0) {
                char value = Spielfeld[x2][y2][2];
                Transitionen[value].Insert(t);
                Spielfeld[x1][y1][2] = value;
            } else {
                Transitionen[counter].insert(t);
                Spielfeld[x1][y1][2] = (char) counter;              //der Wert von counter wir zu char gecastet und ins Spielfeld geschrieben
                Spielfeld[x2][y2][2] = (char) counter++;            //da es mehr als 9 Transitionen geben kann und es bei chars nur bis Ziffer 9 geht
            }*/
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
        Spielfeld = new char[Breite][Hoehe][12];
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            text = br.readLine();
            array = text.split(" ");
            for (int spalte = 0; spalte < Breite; spalte++) {
                Spielfeld[spalte][zeile][0] = array[spalte].charAt(0);
                Spielfeld[spalte][zeile][2] = 0;
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

    public void leichtBombZug(int x, int y, int s) {
        Spielfeld[x][y][0] = '-';
    }

    private void transbombZug(int x, int y, int staerke, int i, int j) {
        i++;
        j++;
        TransitionenListe temp = Transitionen[x][y];
        if (temp != null) {
            for (short dir = 0; dir < 8; dir++) {

                Transition t = temp.search((short) x, (short) y, dir);

                if (t != null) {
                    int number = t.getNumber(x, y, dir);
                    if (i != staerke) {
                        int newoffset;
                        if (i < j) {
                            newoffset = j;
                        } else {
                            newoffset = i;
                        }
                        bombZug(t.getX(number), t.getY(number), newoffset, i, j);

                    }

                }

            }
        }
    }

    public void bombZug(int x, int y, int offset, int newi, int newj) {
        int staerke = Staerke - offset;
        Spielfeld[x][y][0] = '-';
        for (int i = newi; i <= staerke; i++) {
            for (int j = newj; j <= staerke; j++) {
                if (x - i >= 0) {
                    Spielfeld[x - i][y][0] = '-';
                    transbombZug(x - i, y, staerke, i, j);
                    if (y - j >= 0) {
                        Spielfeld[x - i][y - j][0] = '-';
                        transbombZug(x - i, y - j, staerke, i, j);
                    }
                }
                if (Breite > x + i && Hoehe > y + j) {
                    Spielfeld[x + i][y + j][0] = '-';
                    transbombZug(x + i, y + j, staerke, i, j);
                }
                if (Hoehe > y + j) {
                    Spielfeld[x][y + j][0] = '-';
                    transbombZug(x, y + j, staerke, i, j);
                }
                if (Breite > x + i) {
                    Spielfeld[x + i][y][0] = '-';
                    transbombZug(x + i, y, staerke, i, j);
                }
                if (y - j >= 0) {
                    Spielfeld[x][y - j][0] = '-';
                    transbombZug(x, y - j, staerke, i, j);
                }
                if (Breite > x + i && 0 <= y - j) {
                    Spielfeld[x + i][y - j][0] = '-';
                    transbombZug(x + i, y - j, staerke, i, j);
                }
                if (0 <= x - i && Hoehe > y + j) {
                    Spielfeld[x - i][y + j][0] = '-';
                    transbombZug(x - i, y + j, staerke, i, j);
                }
            }
        }
        PrintSpielfeld();
    }

    //  }
    private void firebomb() {
        for (int i = 0; i < Breite; i++) {
            for (int j = 0; j < Hoehe; j++) {
                if (Spielfeld[i][j][0] == 'B') {
                    Spielfeld[i][j][0] = '-';
                }
            }
        }
    }


    public void gueltigeBombZuege(int s) {
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            for (int spalte = 0; spalte < Breite; spalte++) {
                if (Spielfeld[spalte][zeile][0] != '-') {
                    Spielfeld[spalte][zeile][1] = 'B';
                } else {
                    Spielfeld[spalte][zeile][1] = '0';
                }

            }
        }

    }

    public void getBombZug() {


    }


    public void ganzerZug(int s, int x, int y, byte sonderfeld) {
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
            if (Spielfeld[x][y][0] == 'i') {
                inversion = true;
            } else if (Spielfeld[x][y][0] != '0' && Spielfeld[x][y][0] != 'b' && Spielfeld[x][y][0] != 'c') {
                setUeberschreibsteine(1);
                setErsatzsteine(getErsatzsteine() - 1);
                ustein = true;
            }

            Zug(s, x, y);
            Faerben(s, x, y);

            if (choice) {
                if (s != tausch) {
                    for (int spalte = 0; spalte < Breite; spalte++) {
                        for (int zeile = 0; zeile < Hoehe; zeile++) {
                            if (s == Character.getNumericValue(Spielfeld[spalte][zeile][0])) {
                                Spielfeld[spalte][zeile][0] = Integer.toString(tausch).charAt(0);
                            } else if (tausch == Character.getNumericValue(Spielfeld[spalte][zeile][0])) {
                                Spielfeld[spalte][zeile][0] = Integer.toString(s).charAt(0);
                            }
                        }
                    }
                }
            } else if (inversion) {
                for (int spalte = 0; spalte < Breite; spalte++) {
                    for (int zeile = 0; zeile < Hoehe; zeile++) {
                        switch (Spielfeld[spalte][zeile][0]) {
                            case '0':
                            case '-':
                            case 'b':
                            case 'c':
                            case 'i':
                            case 'x':
                                break;
                            default:
                                int i = Character.getNumericValue(Spielfeld[spalte][zeile][0]);
                                if (i > 0 && i <= Spieler) {
                                    Spielfeld[spalte][zeile][0] = Integer.toString(i % Spieler + 1).charAt(0);
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
     * Faerbt vom Punkt (X,Y) aus in die Richtungen die im Array auf true gesetzt sind
     * Faerbt bis gleicher Stein wie von Spieler erreicht ist
     *
     * @param s Spieler von 1-8
     * @param x X Koordinate
     * @param y Y Koordinate
     */

    public void Faerben(int s, int x, int y) {
        Spielfeld[x][y][0] = '0';

        Faerben(s, x, y, dir);
        for (int spalte = 0; spalte < Breite; spalte++) {
            for (int zeile = 0; zeile < Hoehe; zeile++) {
                if (faerben[spalte][zeile]) {
                    Spielfeld[spalte][zeile][0] = Integer.toString(s).charAt(0);
                }
            }
        }
        faerben = new boolean[Breite][Hoehe];
        Spielfeld[x][y][0] = Integer.toString(s).charAt(0);
    }

    private void Faerben(int s, int x, int y, boolean[] direction) {
        if (s == Character.getNumericValue(Spielfeld[x][y][0])) {
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
                            Faerben(s, t.getX(number), t.getY(number), d);
                            continue;
                        }
                    }

                    boolean[] newdir = new boolean[8];
                    newdir[i] = true;
                    newx = getNewX(x, i);
                    newy = getNewY(y, i);
                    Faerben(s, newx, newy, newdir);
                }
            }
        }
    }

    public void gueltigeZuege(int s) {
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            for (int spalte = 0; spalte < Breite; spalte++) {
                if (Zug(s, spalte, zeile)) {
                    char[][][] temp = kopiereSpielfeld(this.Spielfeld);
                    Faerben(s, spalte, zeile);
                    TrivialeHeuristik h = new TrivialeHeuristik(this, s);
                    Spielfeld = temp;
                    gueltigeZuege.hinzufuegen(spalte, zeile, h.getSpielbewertung());
                } else {
                    //Spielfeld[spalte][zeile][1] = '0';
                }
                //TODO vernuenftige Implementierung
                /*if (Spielfeld[spalte][zeile][0] == 'x' && Ueberschreibsteine > 0) {
                    Spielfeld[spalte][zeile][1] = 'X';
                }*/
            }
        }
    }

    private boolean pruefeZug(int s, int x, int y, int dir) {
        while (x < Breite && x >= 0 && y < Hoehe && y >= 0) {
            char value = Spielfeld[x][y][0];
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
                    if (s == Character.getNumericValue(value)) {
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
                                    return pruefeZug(s, t.getX(number), t.getY(number), t.getOppDir(t.dir2));
                                } else {
                                    count++;
                                    return pruefeZug(s, t.getX(number), t.getY(number), t.getOppDir(t.dir1));
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

    public boolean Zug(int s, int x, int y) {
        dir = new boolean[8];
        boolean faerben = false;
        aktX = x;
        aktY = y;
        if (x >= 0 && y >= 0 && x < Breite && y < Hoehe && s > 0 && s <= Spieler) {
            char a = Spielfeld[x][y][0];
            switch (a) {
                case '0':
                case 'b':
                case 'c':
                case 'i':
                    Spielfeld[x][y][0] = Integer.toString(s).charAt(0);
                    int newx, newy;

                    for (int i = 0; i < 8; i++) {
                        count = 0;
                        newx = getNewX(x, i);
                        newy = getNewY(y, i);
                        aktDir = i;
                        if (pruefeZug(s, newx, newy, aktDir)) {
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
                                if (pruefeZug(s, trans.getX(number), trans.getY(number), newdir)) {
                                    faerben = true;
                                }
                            }
                        }
                    }
                    Spielfeld[x][y][0] = a;
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
                        Spielfeld[x][y][0] = '0';
                        faerben = Zug(s, x, y);
                    } else {
                        faerben = false;
                    }
                    Spielfeld[x][y][0] = a;
                    break;
                case 'x':
                    if (hatUeberschreibsteine()) {
                        Spielfeld[x][y][0] = '0';
                        Zug(s, x, y);
                        faerben = true;
                    } else {
                        faerben = false;
                    }
                    Spielfeld[x][y][0] = a;
                    break;
                case '-':
                    break;
                default:
                    break;
            }
        }
        return faerben;
    }

    private char[][][] kopiereSpielfeld(char[][][] feld) {
        char[][][] clone = new char[feld.length][][];

        for (int i = 0; i < feld.length; i++) {
            char[][] innerClone = new char[feld[i].length][];

            for (int j = 0; j < feld[i].length; j++) {
                innerClone[j] = feld[i][j].clone();
            }

            clone[i] = innerClone;
        }

        return clone;
    }

    private GueltigerZugListe kopiereGZugListe() {
        return gueltigeZuege.clone();
    }

    public int[] alphaBeta(int tiefe, int s, Timer t, int alpha, int beta) {
        int x = -1, y = -1, anzahlsteine = this.getErsatzsteine(), anzahlbomben = this.getBomben();
        byte sonderfeld = 1;
        int[] zug = new int[3];
        GueltigerZug gzug;
        ABKnoten knoten = new ABKnoten(alpha, beta, Integer.MIN_VALUE);

        this.gueltigeZuege(s);
        this.gueltigeZuege.SortMaxFirst();

        if (gueltigeZuege.getSize() == 0) {
            if (ersatzsteine > 0) {
                setUeberschreibsteine(1);
                this.gueltigeZuege(s);
                this.gueltigeZuege.SortMaxFirst();
            }
            if (this.gueltigeZuege.getSize() == 0) {
                PrintSpielfeld();
                System.exit(-1);
            }
        }

        char[][][] feld = kopiereSpielfeld(this.Spielfeld);
        GueltigerZugListe liste = this.kopiereGZugListe();
        if (tiefe == 0) {
            zug[0] = gueltigeZuege.get(0).getX();
            zug[1] = gueltigeZuege.get(0).getY();
            if (this.Spielfeld[zug[0]][zug[1]][0] == 'c') {
                zug[2] = s;
            } else if (this.Spielfeld[zug[0]][zug[1]][0] == 'b') {
                zug[2] = 21;
                ustein = true;
            }
            return zug;
        }
        for (int j = 0; j < this.gueltigeZuege.getSize(); j++) {
            if (t != null && t.getStatus()) {
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

            if (this.Spielfeld[gx][gy][0] == 'c') {
                start = 1;
                ende = (byte) this.Spieler;
            } else if (this.Spielfeld[gx][gy][0] == 'b') {
                start = 21;
                ende = 21;
                ustein = true;
            }
            for (byte i = start; i <= ende; i++) {
                this.ganzerZug(s, gx, gy, i);
                zustaende++;
                if (tiefe > 0) {
                    temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, s % this.Spieler + 1, this, sonderfeld, t);
                    if (temp == null) {
                        //System.out.println("Abbruch Tiefe: "+tiefe);
                        this.setSpielfeld(feld);
                        this.setErsatzsteine(anzahlsteine);
                        this.setBomben(anzahlbomben);
                        this.setGueltigeZuege(liste);
                        return null;
                    } else {
                        wert = (int) temp;
                        if (ustein) {
                            if (wert < 0) {
                                wert = wert * (-100);
                            } else {
                                wert = wert * 100;
                            }
                        }
                    }
                } else {
                    DynamischeHeuristik h = new DynamischeHeuristik(this, s);
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
                zustaende = 0;
            }
        }
        this.setSpielfeld(feld);
        this.setErsatzsteine(anzahlsteine);
        this.setBomben(anzahlbomben);
        this.setGueltigeZuege(liste);

        if (x == -1 || y == -1) {
            PrintSpielfeld();
            System.exit(-1);
        } else {
            /*System.out.println("Zug: (" + x + "," + y + ")");
            System.out.println("Spieler: "+s+" Ustein: "+getUeberschreibsteine()+" Estein: "+getErsatzsteine());
            PrintSpielfeld();*/
            zug[0] = x;
            zug[1] = y;
            zug[2] = sonderfeld;
        }
        return zug;
    }

    private Object alphaBeta(int alpha, int beta, int tiefe, int s, int aktS, Spielbrett spiel, byte sonderfeld, Timer t) {
        if (t != null && t.getStatus()) {
            return null;
        }
        if (tiefe == 0) {
            DynamischeHeuristik h = new DynamischeHeuristik(spiel, s);
            int w = h.getSpielbewertung();
            return w;
        } else {
            ABKnoten knoten;
            int anzahlsteine = spiel.getErsatzsteine();
            boolean maxFirst;

            if (s == aktS) { // MAX
                knoten = new ABKnoten(alpha, beta, Integer.MIN_VALUE);
                maxFirst = true;
            } else { // MIN
                knoten = new ABKnoten(alpha, beta, Integer.MAX_VALUE);
                maxFirst = false;
            }
            GueltigerZug gzug;
            spiel.gueltigeZuege.listeLoeschen();
            spiel.gueltigeZuege(aktS);
            if (maxFirst) {
                spiel.gueltigeZuege.SortMaxFirst();
            } else {
                spiel.gueltigeZuege.SortMinFirst();
            }
            GueltigerZugListe gliste = spiel.kopiereGZugListe();
            char[][][] feld = kopiereSpielfeld(spiel.Spielfeld);
            int wert;
            Object temp;

            if (spiel.gueltigeZuege.getSize() > 0) {
                for (int i = 0; i < spiel.gueltigeZuege.getSize(); i++) {
                    gzug = spiel.gueltigeZuege.get(i);
                    spiel.ganzerZug(aktS, gzug.getX(), gzug.getY(), sonderfeld);
                    zustaende++;
                    if (s == aktS) { // MAX
                        temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, aktS % spiel.Spieler + 1, spiel, sonderfeld, t);
                        if (temp == null) {
                            //System.out.println("Abbruch Tiefe: "+tiefe);
                            spiel.setSpielfeld(kopiereSpielfeld(feld));
                            spiel.setErsatzsteine(anzahlsteine);
                            setErsatzsteine(anzahlsteine);
                            spiel.setGueltigeZuege(gliste);
                            return null;
                        } else {
                            wert = (int) temp;
                            if (spiel.Spielfeld[gzug.getX()][gzug.getY()][0] == 'b') {
                                wert = wert * 100;
                            }
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
                        temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, aktS % spiel.Spieler + 1, spiel, sonderfeld, t);
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
                //zustaende++;
                temp = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, aktS % spiel.Spieler + 1, spiel, sonderfeld, t);
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

    public int[] sucheZug(int tiefe, int s) {
        long zeitstart, zeitende;
        int max = Integer.MIN_VALUE, x = -1, y = -1, anzahlsteine = getUeberschreibsteine();
        byte sonderfeld = 1;
        int[] zug = new int[3];
        GueltigerZug gzug;
        Spielbrett spiel = new Spielbrett(this.getSpieler(), this.getUeberschreibsteine(), this.getBomben(), this.getStaerke(), this.getHoehe(), this.getBreite(), this.kopiereSpielfeld(this.Spielfeld), this.getTransitionen(), this.getGueltigeZuege());
        spiel.gueltigeZuege(s);
        gzug = spiel.getGueltigeZuege().getHead();
        //spiel.printGueltigeZuege();
        while (gzug != null) {
            zeitstart = System.nanoTime();
            byte start = 0, ende = 0;
            int gx = gzug.getX(), gy = gzug.getY();

            if (spiel.Spielfeld[gx][gy][0] == 'c') {
                start = 1;
                ende = (byte) spiel.Spieler;
            } else if (spiel.Spielfeld[gx][gy][0] == 'b') {
                start = 20;
                ende = 21;
            }
            for (byte i = start; i <= ende; i++) {
                char[][][] temp = spiel.kopiereSpielfeld(spiel.Spielfeld);
                int wert;
                spiel.ganzerZug(s, gx, gy, i);
                zustaende++;
                if (tiefe > 0) {
                    wert = sucheZug(tiefe - 1, s, s % spiel.Spieler + 1, spiel, i);
                } else {
                    DynamischeHeuristik h = new DynamischeHeuristik(spiel, s);
                    wert = h.getSpielbewertung();
                }
                if (max < wert) {
                    max = wert;
                    x = gx;
                    y = gy;
                    sonderfeld = i;
                }
                spiel.Spielfeld = temp;
                setUeberschreibsteine(anzahlsteine);
                spiel.setUeberschreibsteine(anzahlsteine);
                zeitende = System.nanoTime();
                //System.out.println("Zustaende pro Zug: " + zustaende);
                if (j < 500) {
                    zustande[j] = (long) zustaende;
                    zeit_zustand[j] = (zeitende - zeitstart) / zustaende;
                    j++;
                }
                zustaende = 0;
            }
            gzug = gzug.getNext();
        }
        spiel.getGueltigeZuege().listeLoeschen();

        if (x == -1 || y == -1) {
            //System.out.println("Kein Zug moeglich.");
            zug[0] = -1;
            zug[1] = -1;
        } else {
            //System.out.println("Zug: (" + x + "," + y + ")");
            //System.out.println("Spieler: "+s+" Ustein: "+getUeberschreibsteine());
            //PrintSpielfeld();
            zug[0] = x;
            zug[1] = y;
            zug[2] = sonderfeld;
        }
        return zug;
    }

    private int sucheZug(int tiefe, int s, int aktS, Spielbrett spiel, byte sonderfeld) {
        if (tiefe == 0) {
            DynamischeHeuristik h = new DynamischeHeuristik(spiel, s);
            int wert = h.getSpielbewertung();
            //int wert = h.trivialeHeuristik();
            System.out.println("Wert: " + wert);
            return wert;
        } else {
            int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, anzahlsteine = spiel.getUeberschreibsteine();
            GueltigerZug gzug;
            spiel.gueltigeZuege(aktS);
            gzug = spiel.getGueltigeZuege().getHead();
            Spielbrett temp = new Spielbrett(spiel.getSpieler(), spiel.getUeberschreibsteine(), spiel.getBomben(), spiel.getStaerke(), spiel.getHoehe(), spiel.getBreite(), spiel.kopiereSpielfeld(spiel.Spielfeld), spiel.getTransitionen(), spiel.getGueltigeZuege());
            while (gzug != null) {
                spiel.ganzerZug(aktS, gzug.getX(), gzug.getY(), sonderfeld);
                zustaende++;
                if (s == aktS) {
                    int wert = spiel.sucheZug(tiefe - 1, s, aktS % spiel.Spieler + 1, spiel, sonderfeld);
                    if (max < wert) {
                        max = wert;
                    }
                } else {
                    int wert = spiel.sucheZug(tiefe - 1, s, aktS % spiel.Spieler + 1, spiel, sonderfeld);
                    if (min > wert) {
                        min = wert;
                    }
                }
                spiel = temp;
                spiel.setUeberschreibsteine(anzahlsteine);
                setUeberschreibsteine(anzahlsteine);
                gzug = gzug.getNext();
            }
            spiel.getGueltigeZuege().listeLoeschen();

            if (min == Integer.MAX_VALUE && max == Integer.MIN_VALUE) {
                spiel = temp;
                spiel.setUeberschreibsteine(anzahlsteine);
                setUeberschreibsteine(anzahlsteine);
                //zustaende++;
                return spiel.sucheZug(tiefe - 1, s, (aktS) % spiel.Spieler + 1, spiel, sonderfeld);

            } else if (min != Integer.MAX_VALUE) {
                System.out.println();
                System.out.println("Spieler: " + s + " Akts: " + aktS + " Min: " + min);
                System.out.println();
                return min;
            } else {
                System.out.println();
                System.out.println("Spieler: " + s + " Akts: " + aktS + " Max: " + max);
                System.out.println();
                return max;
            }
        }
    }

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

    public void setSpielfeld(char[][][] spielfeld) {
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

    public char[][][] getSpielfeld() {
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

    //gibt Spielfeld als String zurueck
    private String spielfeldToString() {
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < Hoehe; i++) {
            for (int j = 0; j < Breite; j++) {
                text.append(Spielfeld[j][i][0] + " ");
            }
            text.append("\n");
        }
        return text.toString();
    }

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

    @Override
    public String toString() {
        return "Spieler: " + Spieler + " Steine: " + Ueberschreibsteine + " Bomben: " + Bomben + " Staerke: " + Staerke + " Hoehe: " + Hoehe + " Breite: " + Breite
                + "\n\n" + spielfeldToString() + "\n" + "Transitionen:\n" + transitionenToString();
    }
}
