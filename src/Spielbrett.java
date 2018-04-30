import java.io.*;
import java.util.*;

public class Spielbrett {
    private int Spieler,
                Ueberschreibsteine,
                Bomben,
                Staerke,
                Hoehe,
                Breite;
    private String Spielfeld[][][];
    private TransitionenListe[] Transitionen;
    private boolean dir[], faerben[][];
    private short aktX = 0, aktY = 0, aktDir = 0;
    private int count = 0;

    public Spielbrett(String name) throws IOException{
        Init(name);
    }

    private void Init(String name) throws IOException {
        FileReader fr = new FileReader(name);
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
        Spielfeld = new String[Breite][Hoehe][3];
        for(int zeile = 0; zeile < Hoehe; zeile++) {
            text = br.readLine();
            array = text.split(" ");
            for (int spalte = 0; spalte < Breite; spalte++) {
                Spielfeld[spalte][zeile][0] = array[spalte];
                Spielfeld[spalte][zeile][2] = "0";
            }
        }
        //Transitionen einlesen/speichern
        ArrayList<String> templiste = new ArrayList<>();
        while((text = br.readLine()) != null){
            templiste.add(text);
        }

        Transitionen = new TransitionenListe[templiste.size()+1];
        int counter = 1;
        for (int i = 0; i < templiste.size(); i++) {
            Transitionen[i+1] = new TransitionenListe();
            String[] textarray = templiste.get(i).split(" ");
            short x1,y1,r1,x2,y2,r2;
            Transition t;

            x1 = Short.parseShort(textarray[0]);
            y1 = Short.parseShort(textarray[1]);
            r1 = Short.parseShort(textarray[2]);
            x2 = Short.parseShort(textarray[4]);
            y2 = Short.parseShort(textarray[5]);
            r2 = Short.parseShort(textarray[6]);

            t = new Transition(x1, y1, r1, x2, y2, r2);
            if(Integer.parseInt(Spielfeld[x1][y1][2]) != 0){
                int value = Integer.valueOf(Spielfeld[x1][y1][2]);
                Transitionen[value].insert(t);
                Spielfeld[x2][y2][2] = String.valueOf(value);
            } else if (Integer.parseInt(Spielfeld[x2][y2][2]) != 0) {
                int value = Integer.valueOf(Spielfeld[x2][y2][2]);
                Transitionen[value].insert(t);
                Spielfeld[x1][y1][2] = String.valueOf(value);
            } else {
                Transitionen[counter].insert(t);
                Spielfeld[x1][y1][2] = String.valueOf(counter);
                Spielfeld[x2][y2][2] = String.valueOf(counter++);
            }
        }

        fr.close();
        dir = new boolean[8];
        faerben = new boolean[Breite][Hoehe];
    }

    /**
     * Färbt vom Punkt (X,Y) aus in die Richtungen die im Array auf true gesetzt sind
     * Färbt bis gleicher Stein wie von Spieler erreicht ist
     * @param s Spieler von 1-8
     * @param x X Koordinate
     * @param y Y Koordinate
     */

    public void Faerben(int s, int x, int y) {
        Faerben(s, x, y, dir);
        for (int spalte = 0; spalte < Breite; spalte++) {
            for (int zeile = 0; zeile < Hoehe; zeile++) {
                if(faerben[spalte][zeile]) {
                    Spielfeld[spalte][zeile][0] = Integer.toString(s);
                }
            }
        }
        faerben = new boolean[Breite][Hoehe];
    }

    private void Faerben(int s, int x, int y, boolean[] direction) {
        if(Integer.toString(s).equals(Spielfeld[x][y][0])) {
            return;
        } else {
            int newx, newy;
            for (int i = 0; i < direction.length; i++) {
                if (direction[i]) {
                    newx = x;
                    newy = y;
                    faerben[x][y] = true;
                    String TEMP = Spielfeld[newx][newy][2];
                    if (Integer.parseInt(TEMP) != 0) {
                        Transition T = Transitionen[Integer.parseInt(TEMP)].search((short) x, (short) y, (short) i);
                        if (T != null) {
                            short number = T.getNumber((short) x, (short) y, (short) i);
                            boolean[] d = new boolean[8];
                            if (number == 1) {
                                d[T.getOppDir(T.dir2)] = true;
                            } else {
                                d[T.getOppDir(T.dir1)] = true;
                            }
                            Faerben(s, T.getX(number), T.getY(number), d);
                            continue;
                        }
                    }

                    boolean[] newdir = new boolean[8];
                    newdir[i] = true;
                    switch (i) {
                        case 0:
                            newx = x;
                            newy = y - 1;
                            break;
                        case 1:
                            newx = x + 1;
                            newy = y - 1;
                            break;
                        case 2:
                            newx = x + 1;
                            newy = y;
                            break;
                        case 3:
                            newx = x + 1;
                            newy = y + 1;
                            break;
                        case 4:
                            newx = x;
                            newy = y + 1;
                            break;
                        case 5:
                            newx = x - 1;
                            newy = y + 1;
                            break;
                        case 6:
                            newx = x - 1;
                            newy = y;
                            break;
                        case 7:
                            newx = x - 1;
                            newy = y - 1;
                            break;
                            default:
                                break;

                    }
                    Faerben(s, newx, newy, newdir);
                }
            }
        }
    }

    public void gueltigeZuege() {
        String uestein;

        for (int zeile = 0; zeile < Hoehe; zeile++) {

            for (int spalte = 0; spalte < Breite; spalte++) {

                if(Spielfeld[spalte][zeile][0] != "-" ||Spielfeld[spalte][zeile][0] !="0"||Ueberschreibsteine > 0||Spielfeld[spalte][zeile][0] !="c"){
                    uestein = "Nein";
                }else{
                    uestein = "Ja";
                }

                if (Zug(1, spalte, zeile, uestein)) {
                    Spielfeld[spalte][zeile][1] = "X";
                } else {
                    Spielfeld[spalte][zeile][1] = "0";
                }
            }
        }
        printGueltigeZuege();
    }


    private boolean pruefeZug(int s, int x, int y, String ustein, int dir) {
        switch (dir) {
            case 0:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 0
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x; y = y-1;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 0);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 0);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x; y = y-1;
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
                break;
            case 1:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 1
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x+1; y = y-1;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 1);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 1);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x+1; y = y-1;
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
                break;
            case 2:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 2
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x+1; y = y;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 2);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 2);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x+1; y = y;
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
                break;
            case 3:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 3
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x+1; y = y+1;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 3);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 3);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x+1; y = y+1;
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
                break;
            case 4:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 4
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x; y = y+1;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 4);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 4);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x; y = y+1;
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
                break;
            case 5:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 5
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x-1; y = y+1;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 5);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 5);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x-1; y = y+1;
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
                break;
            case 6:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 6
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x-1; y = y;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 6);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 6);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x-1; y = y;
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
                break;
            case 7:
                while(x < Breite && x >= 0 && y < Hoehe && y >= 0) { //Richtung 7
                    String value = Spielfeld[x][y][0];
                    switch (value) {
                        case "0":
                        case "b":
                        case "c":
                            x = Breite;
                            y = Hoehe;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "x":
                            if(s == (Integer.parseInt(value))) {
                                if (aktX != x || aktY != y) {
                                    if(count > 0) {
                                        this.dir[aktDir] = true;
                                        return true;
                                    }
                                }
                                x = Breite;
                                y = Hoehe;
                            } else {
                                String temp = Spielfeld[x][y][2];
                                if(Integer.parseInt(temp) == 0) {
                                    x = x-1; y = y-1;
                                } else {
                                    Transition t = Transitionen[Integer.parseInt(temp)].search((short) x, (short) y,(short) 7);
                                    if(t != null) {
                                        short number = t.getNumber((short) x, (short) y, (short) 7);
                                        if(number == 1) {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir2));
                                        } else {
                                            return pruefeZug(s, t.getX(number), t.getY(number), ustein, t.getOppDir(t.dir1));
                                        }
                                    } else {
                                        x = x-1; y = y-1;
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
                break;
                default:
                    break;

        }
        return false;
    }


    public boolean Zug(int s, int x, int y, String ustein)  {
        dir = new boolean[8];
        boolean faerben = false;
        aktX = (short) x;
        aktY = (short) y;
        if(x >= 0 && y >= 0 && x < Breite && y < Hoehe && s > 0 && s <= Spieler) {
                String a = Spielfeld[x][y][0];
                String t = Spielfeld[x][y][2];
                switch (a) {
                    case "0":
                    case "b":
                    case "c":
                        Spielfeld[x][y][0] = Integer.toString(s);

                        int newx = x, newy = y-1;
                        aktDir = 0;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        count = 0;
                        newx = x+1; newy = y-1;
                        aktDir = 1;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        count = 0;
                        newx = x+1; newy = y;
                        aktDir = 2;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        count = 0;
                        newx = x+1; newy = y+1;
                        aktDir = 3;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        count = 0;
                        newx = x; newy = y+1;
                        aktDir = 4;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        count = 0;
                        newx = x-1; newy = y+1;
                        aktDir = 5;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        count = 0;
                        newx = x-1; newy = y;
                        aktDir = 6;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        count = 0;
                        newx = x-1; newy = y-1;
                        aktDir = 7;
                        if(pruefeZug(s, newx, newy, ustein, aktDir)) {
                            faerben = true;
                        }

                        if(Integer.parseInt(t) != 0) {
                            for(int i = 0; i < 8; i++) {
                                Transition trans = Transitionen[Integer.parseInt(t)].search((short) x, (short) y, (short) i);
                                if(trans != null) {
                                    short number = trans.getNumber((short) x, (short) y, (short) i);
                                    short newdir;
                                    if(number == 1) {
                                        newdir = trans.getOppDir(trans.dir2);
                                    } else {
                                        newdir = trans.getOppDir(trans.dir1);
                                    }
                                    if(number == 1) {
                                        aktDir = trans.dir1;
                                    } else {
                                        aktDir = trans.dir2;
                                    }
                                    if (pruefeZug(s, trans.getX(number), trans.getY(number), ustein, newdir)) {
                                        faerben = true;
                                    }
                                }
                            }
                        }

                        break;
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                    case "x":
                        if(ustein.equals("Ja") || ustein.equals("ja")) {
                            Spielfeld[x][y][0] = "0";
                            faerben = Zug(s,x,y,"Ja");
                        }
                        break;
                    case "-":
                        break;
                    default:
                        break;
                }
                Spielfeld[x][y][0] = a;
        }
        return faerben;
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

    public void setSpielfeld(String[][][] spielfeld) {
        Spielfeld = spielfeld;
    }

    public void setTransitionen(TransitionenListe[] transitionen) {
        Transitionen = transitionen;
    }

    public int getSpieler() {
        return Spieler;
    }

    public int getUeberschreibsteine() {
        return Ueberschreibsteine;
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

    public String[][][] getSpielfeld() {
        return Spielfeld;
    }

    public TransitionenListe[] getTransitionen() {
        return Transitionen;
    }
    //gibt Spielfeld als String zurück
    private String spielfeldToString() {
        StringBuffer text = new StringBuffer();
        for(int i = 0; i < Hoehe; i++) {
            for(int j = 0; j < Breite; j++) {

                    text.append(Spielfeld[j][i][0] + " ");


            }
            text.append("\n");
        }
        return text.toString();
    }
    //gibt
    private String transitionenToString() {
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i < Transitionen.length; i++) {
            sb.append(Transitionen[i].toString());
        }
        return sb.toString();
    }

    public void PrintSpielfeld() {
       System.out.println(spielfeldToString());
    }

    public void printGueltigeZuege() {
        System.out.println("Mögliche Züge:");
        for (int zeile = 0; zeile < Hoehe; zeile++) {
            for (int spalte = 0; spalte < Breite; spalte++) {
                if(Spielfeld[spalte][zeile][1] == "X") {
                    System.out.println("("+spalte + "," + zeile+")");

                }
            }
        }
    }

    @Override
    public String toString() {
        return "Spieler: "+Spieler+" Steine: "+Ueberschreibsteine+" Bomben: "+Bomben+" Staerke: "+Staerke+" Hoehe: "+Hoehe+" Breite: "+Breite
                + "\n\n" + spielfeldToString() + "\n" +"Transitionen:\n"+transitionenToString();
    }
}
