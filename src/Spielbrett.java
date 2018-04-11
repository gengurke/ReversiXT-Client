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

    public void Init(String name) throws IOException {
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

    private boolean PruefeZug(int s, int x, int y) {
        boolean faerben = false;
        Spielfeld[x][y] = String.valueOf(s);
        return true;
    }

    public void Zug(int s, int x, int y) {
        if(x >= 0 && y >= 0 && x < Breite && y < Hoehe && s > 0 && s <= Spieler) {
                String a = Spielfeld[x][y];
                switch (a) {
                    case "0":
                        System.out.println("0");

                        break;
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                        if (s != (Integer.parseInt(a))) {
                            System.out.println("Anderer Spieler");
                        } else {
                            System.out.println("Gleicher Spieler");
                        }
                        break;
                    case "-":
                        System.out.println("Leeres Feld");
                        break;
                    default:
                        System.out.println("Anderes Feld");
                        break;
                }
                return;
        }
        System.out.println("Falsche Eingabe!");

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

    @Override
    public String toString() {
        return "Spieler: "+Spieler+" Steine: "+Ueberschreibsteine+" Bomben: "+Bomben+" Staerke: "+Staerke+" Hoehe: "+Hoehe+" Breite: "+Breite
                + "\n\n" + ArrayToString() + "\n" +"Transitionen:\n"+ArrayListToString();
    }
}
