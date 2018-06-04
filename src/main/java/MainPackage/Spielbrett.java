package MainPackage;

import HeuristikTest.Heuristik_alt3;

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
    private boolean dir[], faerben[][];
    private short aktX = 0, aktY = 0, aktDir = 0;
    private int count = 0, zustaende = 0;
    private byte sonderstein = 0;

    public Spielbrett(String name) throws IOException {
        Init(name);
    }

    public Spielbrett(int s, int u, int b, int st, int h, int br, char[][][] sp, TransitionenListe[] t) {
        Spieler = s;
        Ueberschreibsteine = u;
        Bomben = b;
        Staerke = st;
        Hoehe = h;
        Breite = br;
        Spielfeld = sp;
        Transitionen = t;
        faerben = new boolean[Breite][Hoehe];
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
            if (Spielfeld[x1][y1][2] != 0) {
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

    }

    public void bombZug(int x, int y) {
        Spielfeld[x][y][0] = '-';
        PrintSpielfeld();

    }

    public void gueltigeBombZuege() {
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


    public void ganzerZug(int s, int x, int y) {
        if (x >= 0 && y >= 0 && x < Breite && y < Hoehe) {
            int tausch = 0, bonus = 0;
            boolean choice = false, inversion = false, ustein = false;
            if (Spielfeld[x][y][0] == 'c') {
                choice = true;
                tausch = 1;
            } else if (Spielfeld[x][y][0] == 'b') {
                bonus = 20;
                //sonderstein = 20;
                if (bonus == 20) {
                    Bomben++;
                } else if (bonus == 21) {
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
                    Spielfeld[spalte][zeile][1] = 'X';
                } else {
                    Spielfeld[spalte][zeile][1] = '0';
                }
                //TODO vernuenftige Implementierung
                if (Spielfeld[spalte][zeile][0] == 'x' && Ueberschreibsteine > 0) {
                    Spielfeld[spalte][zeile][1] = 'X';
                }
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
                                    return pruefeZug(s, t.getX(number), t.getY(number), t.getOppDir(t.dir2));
                                } else {
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
                    Spielfeld[x][y][0] = '0';
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
                    if (hatUeberschreibsteine()) {
                        Spielfeld[x][y][0] = '0';
                        faerben = Zug(s, x, y);
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

    public short[] sucheZug(int tiefe, int s) {
        int max = Integer.MIN_VALUE, x = -1, y = -1, anzahlsteine = getUeberschreibsteine();
        short[] zug = new short[3];
        if(tiefe > 0) {
            Spielbrett spiel = new Spielbrett(this.getSpieler(), this.getUeberschreibsteine(), this.getBomben(), this.getStaerke(), this.getHoehe(), this.getBreite(), this.kopiereSpielfeld(), this.getTransitionen());
            spiel.gueltigeZuege(s);
            //spiel.printGueltigeZuege();
            for (int spalte = 0; spalte < spiel.Breite; spalte++) {
                for (int zeile = 0; zeile < spiel.Hoehe; zeile++) {
                    if (spiel.Spielfeld[spalte][zeile][1] == 'X') {
                        char[][][] temp = spiel.kopiereSpielfeld();
                        spiel.ganzerZug(s, spalte, zeile);
                        zustaende++;
                        int wert = sucheZug(tiefe - 1, s, s % spiel.Spieler + 1, spiel);
                        if (max < wert) {
                            max = wert;
                            x = spalte;
                            y = zeile;
                        }
                        spiel.Spielfeld = temp;
                        setUeberschreibsteine(anzahlsteine);
                        spiel.setUeberschreibsteine(anzahlsteine);
                        System.out.println("Zustände pro Zug: " + zustaende);
                        zustaende = 0;
                    }
                }
            }
        }
        if (x == -1 || y == -1) {
            //System.out.println("Kein Zug möglich.");
            zug[0] = -1;
            zug[1] = -1;
        } else {
            //System.out.println("Zug: (" + x + "," + y + ")");
            //System.out.println("Spieler: "+s+" Ustein: "+getUeberschreibsteine());
            //PrintSpielfeld();
            zug[0] = (short) x;
            zug[1] = (short) y;
            zug[2] = (short) sonderstein;
            switch (Spielfeld[x][y][0]) {
                case 'b':
                    zug[2] = 20;
                    break;
                case 'c':
                    zug[2] = 1;
                    break;
                    default:
                        zug[2] = 0;
                        break;
            }
        }
        return zug;
    }

    private int sucheZug(int tiefe, int s, int aktS, Spielbrett spiel) {
        if (tiefe == 0) {
            Heuristik_alt3 h = new Heuristik_alt3(spiel, s);
            return h.getSpielbewertung();

        } else {
            int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, anzahlsteine = spiel.getUeberschreibsteine();
            spiel.gueltigeZuege(aktS);
            Spielbrett temp = new Spielbrett(spiel.getSpieler(), spiel.getUeberschreibsteine(), spiel.getBomben(), spiel.getStaerke(), spiel.getHoehe(), spiel.getBreite(), spiel.kopiereSpielfeld(), spiel.getTransitionen());
            for (int spalte = 0; spalte < spiel.Breite; spalte++) {
                for (int zeile = 0; zeile < spiel.Hoehe; zeile++) {
                    if (spiel.Spielfeld[spalte][zeile][1] == 'X') {
                        spiel.ganzerZug(aktS, spalte, zeile);
                        zustaende++;
                        if (s == aktS) {
                            int wert = spiel.sucheZug(tiefe - 1, s, aktS % spiel.Spieler + 1, spiel);
                            if (max < wert) {
                                max = wert;
                            }
                            spiel = temp;
                        } else {
                            int wert = spiel.sucheZug(tiefe - 1, s, aktS % spiel.Spieler + 1, spiel);
                            if (min > wert) {
                                min = wert;
                            }
                            spiel = temp;
                            spiel.setUeberschreibsteine(anzahlsteine);
                            setUeberschreibsteine(anzahlsteine);
                        }
                    }
                }
            }
            if (min == Integer.MAX_VALUE && max == Integer.MIN_VALUE) {
                spiel = temp;
                //zustaende++;
                return spiel.sucheZug(tiefe - 1, s, (aktS) % spiel.Spieler + 1, spiel);

            } else if (min != Integer.MAX_VALUE) {
                return min;
            } else {
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
        return Spielfeld;
    }

    public TransitionenListe[] getTransitionen() {
        return Transitionen;
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
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            for (int spalte = 0; spalte < Breite; spalte++) {
                if (Spielfeld[spalte][zeile][1] == 'X') {
                    System.out.println("(" + spalte + "," + zeile + ")");

                }
            }
        }
    }

    @Override
    public String toString() {
        return "Spieler: " + Spieler + " Steine: " + Ueberschreibsteine + " Bomben: " + Bomben + " Staerke: " + Staerke + " Hoehe: " + Hoehe + " Breite: " + Breite
                + "\n\n" + spielfeldToString() + "\n" + "Transitionen:\n" + transitionenToString();
    }
}
