import java.io.*;
import java.util.*;

public class Spielbrett {
    private int Spieler,
                Ueberschreibsteine,
                Bomben,
                Staerke,
                Hoehe,
                Breite;
    private String Spielfeld[][];
    private ArrayList<Transition> Transitionen;

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
        Spielfeld = new String[Breite][Hoehe];
        for(int zeile = 0; zeile < Hoehe; zeile++) {
            text = br.readLine();
            array = text.split(" ");
            for (int spalte = 0; spalte < Breite; spalte++) {
                Spielfeld[spalte][zeile] = array[spalte];
            }
        }
        //Transitionen einlesen/speichern
        Transitionen = new ArrayList<>();
        while((text = br.readLine()) != null) {
            Transitionen.add(new Transition(text));
        }

        fr.close();
    }

    private void Faerben(int s, int x, int y, boolean[] dir) {
        for(int i = 0; i < dir.length; i++) {
            int newx, newy;
            if(dir[i]) {
                switch (i) {
                    case 0:
                        newx = x; newy = y-1;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx; newy = newy-1;
                        }
                        break;
                    case 1:
                        newx = x+1; newy = y-1;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx+1; newy = newy-1;
                        }
                        break;
                    case 2:
                        newx = x+1; newy = y;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx+1; newy = newy;
                        }
                        break;
                    case 3:
                        newx = x+1; newy = y+1;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx+1; newy = newy+1;
                        }
                        break;
                    case 4:
                        newx = x; newy = y+1;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx; newy = newy+1;
                        }
                        break;
                    case 5:
                        newx = x-1; newy = y+1;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx-1; newy = newy+1;
                        }
                        break;
                    case 6:
                        newx = x-1; newy = y;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx-1; newy = newy;
                        }
                        break;
                    case 7:
                        newx = x-1; newy = y-1;
                        while(!Integer.toString(s).equals(Spielfeld[newx][newy])) {
                            Spielfeld[newx][newy] = Integer.toString(s);
                            newx = newx-1; newy = newy-1;
                        }
                        break;
                }
            }
        }
    }

    public boolean Zug(int s, int x, int y, String ustein) {
        boolean faerben = false;
        boolean[] dir = new boolean[8];
        if(x >= 0 && y >= 0 && x < Breite && y < Hoehe && s > 0 && s <= Spieler) {
                String a = Spielfeld[x][y];
                switch (a) {
                    case "0":
                        Spielfeld[x][y] = Integer.toString(s);
                        int newx = x, newy = y-1, count = 0;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 0
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[0] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx; newy = newy-1;
                                    }
                                    break;
                                    default:
                                        newx = Breite;
                                        newy = Hoehe;
                                        break;
                            }
                            count++;
                        }

                        count = 0;
                        newx = x+1; newy = y-1;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 1
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[1] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx+1; newy = newy-1;
                                    }
                                    break;
                                default:
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                            }
                            count++;
                        }

                        count = 0;
                        newx = x+1; newy = y;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 2
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[2] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx+1; newy = newy;
                                    }
                                    break;
                                default:
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                            }
                            count++;
                        }

                        count = 0;
                        newx = x+1; newy = y+1;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 3
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[3] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx+1; newy = newy+1;
                                    }
                                    break;
                                default:
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                            }
                            count++;
                        }

                        count = 0;
                        newx = x; newy = y+1;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 4
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[4] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx; newy = newy+1;
                                    }
                                    break;
                                default:
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                            }
                            count++;
                        }

                        count = 0;
                        newx = x-1; newy = y+1;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 5
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[5] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx-1; newy = newy+1;
                                    }
                                    break;
                                default:
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                            }
                            count++;
                        }

                        count = 0;
                        newx = x-1; newy = y;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 6
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[6] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx-1; newy = newy;
                                    }
                                    break;
                                default:
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                            }
                            count++;
                        }

                        count = 0;
                        newx = x-1; newy = y-1;
                        while(newx < Breite && newx >= 0 && newy < Hoehe && newy >= 0) { //Richtung 7
                            String value = Spielfeld[newx][newy];
                            switch (value) {
                                case "0":
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                case "5":
                                case "6":
                                case "7":
                                case "8":
                                    if(s == (Integer.parseInt(value))) {
                                        if (newx != x || newy != y) {
                                            if(count > 0) {
                                                faerben = true;
                                                dir[7] = true;
                                            }
                                        }
                                        newx = Breite;
                                        newy = Hoehe;
                                    } else {
                                        newx = newx-1; newy = newy-1;
                                    }
                                    break;
                                default:
                                    newx = Breite;
                                    newy = Hoehe;
                                    break;
                            }
                            count++;
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
                            Spielfeld[x][y] = "0";
                            faerben = Zug(s,x,y,"Ja");
                        }
                        break;
                    case "-":
                        break;
                    default:
                        break;
                }
        }
        Faerben(s,x,y,dir);
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

    public void setSpielfeld(String[][] spielfeld) {
        Spielfeld = spielfeld;
    }

    public void setTransitionen(ArrayList<Transition> transitionen) {
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

    public String[][] getSpielfeld() {
        return Spielfeld;
    }

    public ArrayList<Transition> getTransitionen() {
        return Transitionen;
    }
    //gibt Spielfeld als String zurück
    private String ArrayToString() {
        StringBuffer text = new StringBuffer();
        for(int i = 0; i < Hoehe; i++) {
            for(int j = 0; j < Breite; j++) {
                text.append(Spielfeld[j][i] + " ");
            }
            text.append("\n");
        }
        return text.toString();
    }
    //gibt
    private String ArrayListToString() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < Transitionen.size(); i++) {
            sb.append(Transitionen.get(i));
        }
        return sb.toString();
    }

    public void PrintSpielfeld() {
       System.out.println(ArrayToString());
    }

    @Override
    public String toString() {
        return "Spieler: "+Spieler+" Steine: "+Ueberschreibsteine+" Bomben: "+Bomben+" Staerke: "+Staerke+" Hoehe: "+Hoehe+" Breite: "+Breite
                + "\n\n" + ArrayToString() + "\n" +"Transitionen:\n"+ArrayListToString();
    }
}
