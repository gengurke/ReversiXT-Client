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
    private TransitionenListe[] Transitionen;
    private GueltigerZugListe gueltigeZuege;
    private boolean dir[], faerben[][];
    private short aktX = 0, aktY = 0, aktDir = 0;
    private int count = 0, zustaende = 0;
    public long[] zustande = new long[500], zeit_zustand = new long[500];
    private static int j = 0;

    public Spielbrett(String name) throws IOException {
        Init(name);
    }

    public Spielbrett(int s, int u, int b, int st, int h, int br, char[][][] sp, TransitionenListe[] t, GueltigerZugListe gl) {
        Spieler = s;
        Ueberschreibsteine = u;
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

        Transitionen = new TransitionenListe[templiste.size() + 1];
        int counter = 1;
        Transitionen[0] = new TransitionenListe();
        for (int i = 0; i < templiste.size(); i++) {
            Transitionen[i + 1] = new TransitionenListe();
            String[] textarray = templiste.get(i).split(" ");
            short x1, y1, r1, x2, y2, r2;
            Transition t;

            x1 = Short.parseShort(textarray[0]);
            y1 = Short.parseShort(textarray[1]);
            r1 = Short.parseShort(textarray[2]);
            x2 = Short.parseShort(textarray[4]);
            y2 = Short.parseShort(textarray[5]);
            r2 = Short.parseShort(textarray[6]);

            t = new Transition(x1, y1, r1, x2, y2, r2);
            if(Spielfeld[x1][y1][2] != 0 && Spielfeld[x2][y2][2] != 0) {
                char value1 = Spielfeld[x1][y1][2];
                char value2 = Spielfeld[x2][y2][2];
                if(value1 != value2) {
                    Transition trans = Transitionen[value2].getHead();
                    while (trans != null) {
                        Spielfeld[trans.x1][trans.y1][2] = value1;
                        Spielfeld[trans.x2][trans.y2][2] = value1;
                        Transitionen[value1].insert(trans);
                        trans = trans.getNext();
                    }
                }
                Transitionen[value1].insert(t);

            } else if (Spielfeld[x1][y1][2] != 0) {
                char value = Spielfeld[x1][y1][2];
                Transitionen[value].insert(t);
                Spielfeld[x2][y2][2] = value;
            } else if (Spielfeld[x2][y2][2] != 0) {
                char value = Spielfeld[x2][y2][2];
                Transitionen[value].insert(t);
                Spielfeld[x1][y1][2] = value;
            } else {
                Transitionen[counter].insert(t);
                Spielfeld[x1][y1][2] = (char) counter;              //der Wert von counter wir zu char gecastet und ins Spielfeld geschrieben
                Spielfeld[x2][y2][2] = (char) counter++;            //da es mehr als 9 Transitionen geben kann und es bei chars nur bis Ziffer 9 geht
            }
        }


        dir = new boolean[8];
        faerben = new boolean[Breite][Hoehe];
        gueltigeZuege = new GueltigerZugListe();

    }

    public void leichtBombZug(int x, int y, int s) {
        Spielfeld[x][y][0] = '-';
    }
    private void transbombZug(int x, int y, int offset, short lastdir) {


            char temp = Spielfeld[x][y][2];
            if (temp != 0) {
                for (short dir = 0; dir < 8; dir++) {


                    Transition t = Transitionen[temp].search((short) x, (short) y, dir);

                    if (t != null) {
                        short number = t.getNumber((short) x, (short) y, dir);
                        if (lastdir != dir) {

                        bombZug(t.getX(number), t.getY(number), offset+1,dir);

                    }
                }
            }
        }
    }


    public void bombZug(int x, int y){
        bombZug(x,y,0,(short)-1);
        firebomb();
        PrintSpielfeld();
    }
            private void bombZug(int x, int y,int offset,short dir) {
                // if (x < Breite && y < Hoehe) {
                if (Spielfeld[x][y][0] != '-') {
                    int staerke = Staerke - offset;
                    Spielfeld[x][y][0] = 'B';
                    if (staerke > 0) {
                        transbombZug(x, y, offset, dir);
                        if (0 <= x - 1 && staerke > 0) {
                            bombZug(x - 1, y, offset + 1, (short) -1);
                        }
                        if (0 <= x - 1 && 0 <= y - 1 && staerke > 0) {
                            bombZug(x - 1, y - 1, offset + 1, (short) -1);
                        }
                        if (Breite > x + 1 && staerke > 0) {
                            bombZug(x + 1, y, offset + 1, (short) -1);
                        }
                        if (Breite > x + 1 && Hoehe > y + 1 && staerke > 0) {
                            bombZug(x + 1, y + 1, offset + 1, (short) -1);
                        }
                        if (0 <= x - 1 && Hoehe > y + 1 && staerke > 0) {
                            bombZug(x - 1, y + 1, offset + 1, (short) -1);
                        }
                        if (Breite > x + 1 && 0 <= y - 1 && staerke > 0) {
                            bombZug(x + 1, y - 1, offset + 1, (short) -1);
                        }
                        if (Hoehe > y + 1 && staerke > 0) {
                            bombZug(x, y + 1, offset + 1, (short) -1);
                        }
                        if (0 < y - 1 && staerke > 0) {
                            bombZug(x, y - 1, offset + 1, (short) -1);
                        }


                    }


                }
            }
          //  }
            private void firebomb(){
        for (int i =0;i<Breite;i++){
            for(int j = 0; j<Hoehe;j++){
                if(Spielfeld[i][j][0] == 'B'){
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


    public void ganzerZug(int s, int x, int y, byte sonderfeld) {
        if (x >= 0 && y >= 0 && x < Breite && y < Hoehe) {
            int tausch = 0, bonus = 0;
            boolean choice = false, inversion = false, ustein = false;
            if (Spielfeld[x][y][0] == 'c') {
                choice = true;
                tausch = (int) sonderfeld;
            } else if (Spielfeld[x][y][0] == 'b') {
                if (sonderfeld == 20) {
                    Bomben++;
                } else if (sonderfeld == 21) {
                    Ueberschreibsteine++;
                }
            } else if (Spielfeld[x][y][0] == 'i') {
                inversion = true;
            } else if (Spielfeld[x][y][0] != '0') {
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
                Ueberschreibsteine--;
            }
        } else {
            System.out.println("Ungueltiger Zug!3");
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
                    char temp = Spielfeld[newx][newy][2];
                    if (temp != 0) {
                        Transition t = Transitionen[temp].search((short) x, (short) y, (short) i);
                        if (t != null) {
                            short number = t.getNumber((short) x, (short) y, (short) i);
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
                    char[][][] temp = kopiereSpielfeld();
                    Faerben(s, spalte, zeile);
                    Heuristik h = new Heuristik(this, s);
                    Spielfeld = temp;
                    gueltigeZuege.hinzufuegen(spalte, zeile, h.trivialeHeuristik());
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
                        char temp = Spielfeld[x][y][2];
                        if (temp == 0) {
                            x = getNewX(x, dir);
                            y = getNewY(y, dir);
                        } else {
                            Transition t = Transitionen[temp].search((short) x, (short) y, (short) dir);
                            if (t != null) {
                                short number = t.getNumber((short) x, (short) y, (short) dir);
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
        aktX = (short) x;
        aktY = (short) y;
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
                        aktDir = (short) i;
                        if (pruefeZug(s, newx, newy, aktDir)) {
                            faerben = true;
                        }
                    }

                    char t = Spielfeld[x][y][2];

                    if (t != 0) {
                        for (int i = 0; i < 8; i++) {
                            Transition trans = Transitionen[t].search((short) x, (short) y, (short) i);
                            if (trans != null) {
                                short number = trans.getNumber((short) x, (short) y, (short) i);
                                short newdir;
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
                case '-':
                    break;
                default:
                    break;
            }
        }
        return faerben;
    }

    private char[][][] kopiereSpielfeld() {
        char[][][] temp = new char[Breite][Hoehe][3];

        for (int zeile = 0; zeile < Hoehe; zeile++) {

            for (int spalte = 0; spalte < Breite; spalte++) {

                temp[spalte][zeile][0] = Spielfeld[spalte][zeile][0];
                temp[spalte][zeile][1] = Spielfeld[spalte][zeile][1];
                temp[spalte][zeile][2] = Spielfeld[spalte][zeile][2];
            }
        }

        return temp;
    }

    private GueltigerZugListe kopiereGZugListe() {
        GueltigerZugListe neu = new GueltigerZugListe();
        GueltigerZug gzug = gueltigeZuege.getHead();
        while (gzug != null) {
            neu.hinzufuegen(gzug.getX(), gzug.getY(), gzug.getWert());
            gzug = gzug.getNext();
        }
        return neu;
    }

    public short[] alphaBeta(int tiefe, int s) {
        int x = -1, y = -1, anzahlsteine = getUeberschreibsteine();
        byte sonderfeld = 1;
        short[] zug = new short[3];
        GueltigerZug gzug;
        ABKnoten knoten = new ABKnoten(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);

        Spielbrett spiel = new Spielbrett(this.getSpieler(), this.getUeberschreibsteine(), this.getBomben(), this.getStaerke(), this.getHoehe(), this.getBreite(), this.kopiereSpielfeld(), this.getTransitionen(), new GueltigerZugListe());
        spiel.gueltigeZuege(s);
        spiel.gueltigeZuege.SortMaxFirst();
        spiel.printGueltigeZuege();
        for(int j = 0; j < spiel.gueltigeZuege.getSize(); j++) {
            gzug = spiel.gueltigeZuege.get(j);
            byte start = 0, ende = 0;
            int gx = gzug.getX(), gy = gzug.getY(), wert;
            char[][][] temp = spiel.kopiereSpielfeld();
            GueltigerZugListe gliste = spiel.kopiereGZugListe();

            if(spiel.Spielfeld[gx][gy][0] == 'c') {
                start = 1;
                ende = (byte) spiel.Spieler;
            } else if(spiel.Spielfeld[gx][gy][0] == 'b') {
                start = 20;
                ende = 21;
            }
            for(byte i = start; i <= ende; i++) {
                spiel.ganzerZug(s, gx, gy, i);
                zustaende++;
                if(tiefe > 0) {
                    wert = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, s % spiel.Spieler + 1, spiel, i);
                } else {
                    Heuristik h = new Heuristik(spiel, s);
                    wert = h.getSpielbewertung();
                }
                if (knoten.getAlpha() < wert) {
                    knoten.setAlpha(wert);
                    knoten.setWert(wert);

                    x = gx;
                    y = gy;
                    sonderfeld = i;
                }
                spiel.Spielfeld = temp;
                spiel.gueltigeZuege = gliste;
                setUeberschreibsteine(anzahlsteine);
                spiel.setUeberschreibsteine(anzahlsteine);
                /*System.out.println("Zustaende pro Zug: " + zustaende);
                if(j < 500) {
                    zustande[j] = (long) zustaende;
                    j++;
                }*/
                zustaende = 0;
            }
        }
        this.gueltigeZuege = new GueltigerZugListe();

        if (x == -1 || y == -1) {
            //System.out.println("Kein Zug moeglich.");
            zug[0] = -1;
            zug[1] = -1;
        } else {
            System.out.println("Zug: (" + x + "," + y + ")");
            System.out.println("Spieler: "+s+" Ustein: "+getUeberschreibsteine());
            PrintSpielfeld();
            zug[0] = (short) x;
            zug[1] = (short) y;
            zug[2] = (short) sonderfeld;
        }
        return zug;
    }

    private int alphaBeta(int a, int b, int tiefe, int s, int aktS, Spielbrett spiel, byte sonderfeld) {
        if (tiefe == 0) {
            Heuristik h = new Heuristik(spiel, s);
            int w = h.getSpielbewertung();
            //System.out.println(w);
            return w;
        } else {
            ABKnoten knoten;
            int anzahlsteine = spiel.getUeberschreibsteine();
            boolean maxFirst;

            if (s == aktS) { // MAX
                knoten = new ABKnoten(a, b, Integer.MIN_VALUE);
                maxFirst = true;
            } else { // MIN
                knoten = new ABKnoten(a, b, Integer.MAX_VALUE);
                maxFirst= false;
            }
            GueltigerZug gzug;
            spiel.gueltigeZuege.listeLoeschen();
            spiel.gueltigeZuege(aktS);
            if(maxFirst) {
                spiel.gueltigeZuege.SortMaxFirst();
            } else {
                spiel.gueltigeZuege.SortMinFirst();
            }
            GueltigerZugListe gliste = spiel.kopiereGZugListe();
            Spielbrett temp = new Spielbrett(spiel.getSpieler(), spiel.getUeberschreibsteine(), spiel.getBomben(), spiel.getStaerke(), spiel.getHoehe(), spiel.getBreite(), spiel.kopiereSpielfeld(), spiel.getTransitionen(), gliste);
            spiel.printGueltigeZuege();

            if(spiel.gueltigeZuege.getSize() > 0) {
                for(int i = 0; i < spiel.gueltigeZuege.getSize();i++) {
                    gzug = spiel.gueltigeZuege.get(i);
                    int wert;
                    spiel.ganzerZug(aktS, gzug.getX(), gzug.getY(), sonderfeld);
                    zustaende++;
                    if (s == aktS) { // MAX
                        wert = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, aktS % spiel.Spieler + 1, spiel, sonderfeld);
                        if (knoten.getWert() < wert) {
                            knoten.setWert(wert);
                        }
                        if (knoten.getAlpha() < wert) {
                            knoten.setAlpha(wert);
                        }
                        if (knoten.getBeta() < wert) {
                            gzug = null;
                            spiel = temp;
                            spiel.setUeberschreibsteine(anzahlsteine);
                            setUeberschreibsteine(anzahlsteine);
                            spiel.gueltigeZuege = gliste;
                            break;
                        }
                    } else { // MIN
                        wert = alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, aktS % spiel.Spieler + 1, spiel, sonderfeld);
                        if (knoten.getWert() > wert) {
                            knoten.setWert(wert);
                        }
                        if (knoten.getBeta() > wert) {
                            knoten.setBeta(wert);
                        }
                        if (knoten.getAlpha() > wert) {
                            gzug = null;
                            spiel = temp;
                            spiel.setUeberschreibsteine(anzahlsteine);
                            setUeberschreibsteine(anzahlsteine);
                            spiel.gueltigeZuege = gliste;
                            break;
                        }
                    }
                    spiel = temp;
                    spiel.setUeberschreibsteine(anzahlsteine);
                    setUeberschreibsteine(anzahlsteine);
                    spiel.gueltigeZuege = gliste;
                }
            } else {
                spiel = temp;
                spiel.setUeberschreibsteine(anzahlsteine);
                setUeberschreibsteine(anzahlsteine);
                spiel.gueltigeZuege = gliste;
                //zustaende++;
                int wert = spiel.alphaBeta(knoten.getAlpha(), knoten.getBeta(), tiefe - 1, s, (aktS) % spiel.Spieler + 1, spiel, sonderfeld);
                return wert;
            }
            spiel.getGueltigeZuege().listeLoeschen();
            return knoten.getWert();
        }
    }

    public short[] sucheZug(int tiefe, int s) {
        long zeitstart , zeitende;
        int max = Integer.MIN_VALUE, x = -1, y = -1, anzahlsteine = getUeberschreibsteine();
        byte sonderfeld = 1;
        short[] zug = new short[3];
        GueltigerZug gzug;
            Spielbrett spiel = new Spielbrett(this.getSpieler(), this.getUeberschreibsteine(), this.getBomben(), this.getStaerke(), this.getHoehe(), this.getBreite(), this.kopiereSpielfeld(), this.getTransitionen(), this.getGueltigeZuege());
            spiel.gueltigeZuege(s);
            gzug = spiel.getGueltigeZuege().getHead();
            //spiel.printGueltigeZuege();
            while(gzug != null) {
                zeitstart = System.nanoTime();
                byte start = 0, ende = 0;
                int gx = gzug.getX(), gy = gzug.getY();

                if(spiel.Spielfeld[gx][gy][0] == 'c') {
                    start = 1;
                    ende = (byte) spiel.Spieler;
                } else if(spiel.Spielfeld[gx][gy][0] == 'b') {
                    start = 20;
                    ende = 21;
                }
                for(byte i = start; i <= ende; i++) {
                    char[][][] temp = spiel.kopiereSpielfeld();
                    int wert;
                    spiel.ganzerZug(s, gx, gy, i);
                    zustaende++;
                    if(tiefe > 0) {
                        wert = sucheZug(tiefe - 1, s, s % spiel.Spieler + 1, spiel, i);
                    } else {
                        Heuristik h = new Heuristik(spiel, s);
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
                    if(j < 500) {
                        zustande[j] = (long) zustaende;
                        zeit_zustand[j] = (zeitende-zeitstart)/ zustaende;
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
            zug[0] = (short) x;
            zug[1] = (short) y;
            zug[2] = (short) sonderfeld;
        }
        return zug;
    }

    private int sucheZug(int tiefe, int s, int aktS, Spielbrett spiel, byte sonderfeld) {
        if (tiefe == 0) {
            Heuristik h = new Heuristik(spiel, s);
            int wert = h.getSpielbewertung();
            //int wert = h.trivialeHeuristik();
            System.out.println("Wert: "+wert);
            return wert;
        } else {
            int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, anzahlsteine = spiel.getUeberschreibsteine();
            GueltigerZug gzug;
            spiel.gueltigeZuege(aktS);
            gzug = spiel.getGueltigeZuege().getHead();
            Spielbrett temp = new Spielbrett(spiel.getSpieler(), spiel.getUeberschreibsteine(), spiel.getBomben(), spiel.getStaerke(), spiel.getHoehe(), spiel.getBreite(), spiel.kopiereSpielfeld(), spiel.getTransitionen(), spiel.getGueltigeZuege());
            while(gzug != null) {
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
                System.out.println("Spieler: "+s+" Akts: "+aktS+ " Min: "+min);
                System.out.println();
                return min;
            } else {
                System.out.println();
                System.out.println("Spieler: "+s+" Akts: "+aktS+ " Max: "+max);
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

    public void setTransitionen(TransitionenListe[] transitionen) {
        Transitionen = transitionen;
    }

    public int getSpieler() {
        return Spieler;
    }

    public int getUeberschreibsteine() {
        return Integer.valueOf(Ueberschreibsteine);
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
        return kopiereSpielfeld();
    }

    public TransitionenListe[] getTransitionen() {
        return Transitionen;
    }

    public GueltigerZugListe getGueltigeZuege() {
        return gueltigeZuege;
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
        for (int i = 1; i < Transitionen.length; i++) {
            sb.append(Transitionen[i].toString());
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
