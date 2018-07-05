package Main;

import java.io.*;
import java.util.ArrayList;

public class Client {
    private Spielbrett Spiel;
    private byte Spielernummer;
    private boolean isRunning;
    private boolean bomben;
    private long start, ende;
    private boolean window, sortierung, AB, info;
    private BufferedWriter bwZustaende, bwTiefe, bwZeitGesamt, bwZeitProTiefe, bwInfo;
    private int windowSize;


    public Client(boolean a, boolean w, boolean s, boolean i, int tiefe, int zeit) throws IOException {
        window = w;
        sortierung = s;
        AB = a;
        info = i;
        FileWriter fwInfo = new FileWriter("Info.txt");
        FileWriter fwZustaende = new FileWriter("Zustaende_pro_Tiefe.txt");
        FileWriter fwZeitGesamt = new FileWriter("Zeit_Gesamt_pro_Zug.txt");
        bwInfo = new BufferedWriter(fwInfo);
        bwZustaende = new BufferedWriter(fwZustaende);
        bwZeitGesamt = new BufferedWriter(fwZeitGesamt);
        if (zeit != 0) {
            FileWriter fwTiefe = new FileWriter("Erreichte_Tiefe.txt");
            FileWriter fwZeitProTiefe = new FileWriter("Zeit_pro_Tiefe.txt");
            bwTiefe = new BufferedWriter(fwTiefe);
            bwZeitProTiefe = new BufferedWriter(fwZeitProTiefe);
        }
        bwInfo.write("Tiefe: " + tiefe + " Zeit: " + zeit);
        bwInfo.newLine();
        bwInfo.write("AlphaBeta: " + a + " Zugsortierung: " + s + " Aspiration Windows: " + w);
        bwInfo.newLine();
        bwInfo.newLine();
    }


    public void netzwerk(int port, String ip) throws IOException {
        java.net.Socket socket = new java.net.Socket(ip, port); // verbindet sich mit Server
        byte Gruppennummer = 7;
        isRunning = true;
        int laenge = 1;
        byte nachricht[];
        byte zuSendendeNachricht[];
        nachricht = new byte[laenge];
        zuSendendeNachricht = new byte[laenge + 5];
        zuSendendeNachricht[0] = 1;
        zuSendendeNachricht[4] = 1;
        zuSendendeNachricht[5] = 7;


        schreibeNachricht(socket, zuSendendeNachricht);
        Spiel = new Spielbrett(empfangeNachricht(socket));
        if (window) {
            windowSize = windowSize(Spiel);
        }

        while (isRunning) {
            empfangeNachricht(socket);
        }
        bwInfo.close();
        bwZustaende.close();
        bwZeitGesamt.close();
        bwTiefe.close();
        bwZeitProTiefe.close();
        System.exit(0);

    }

    public void schreibeNachricht(java.net.Socket socket, byte[] nachricht) throws IOException {

        OutputStream socketOutputStream = socket.getOutputStream();
        socketOutputStream.write(nachricht);
    }

    public String empfangeNachricht(java.net.Socket socket) throws IOException {
        InputStream socketInputStream = socket.getInputStream();

        byte stream[] = new byte[5];

        //ArrayList <Byte> nachricht = new ArrayList<>();

        socketInputStream.read(stream, 0, 5);


        byte art = stream[0];
        int laenge = getlaenge(stream);
        char nachricht[];
        nachricht = new char[laenge];
        byte message[] = new byte[laenge];
        socketInputStream.read(message, 0, laenge);

        for (int i = 0; i < laenge; i++) {
            nachricht[i] = (char) message[i];
        }
        // System.out.println(art);
        switch (art) {
            case 2:
                return String.valueOf(nachricht);

            case 3:
                // Spielernummer setzen
                Spielernummer = (byte) nachricht[0];
                break;
            case 4:
                start = System.currentTimeMillis();
                char[][] Spielfeld = Spiel.getSpielfeld();
                long zeit = 0;
                byte tiefe;
                for (int i = 0; i < 4; i++) {
                    zeit = zeit << 8;
                    zeit += Byte.toUnsignedInt(message[i]);
                }
                tiefe = (byte) nachricht[4];
                if (window) {
                    windowSize = windowSize(Spiel);
                }

                if (bomben) {
                    if (zeit != 0) {
                        ende = System.currentTimeMillis();
                        Timer clock = new Timer(zeit - (ende - start));
                        Spiel.gueltigeBombZuege(Spielernummer, clock);
                    } else {
                        Spiel.gueltigeBombZuege(Spielernummer, null);
                    }
                    GueltigerZug bombzug = Spiel.getGueltigeZuege().get(0);
                    int[] zug = new int[3];
                    zug[0] = bombzug.getX();
                    zug[1] = bombzug.getY();
                    sendeZug(zug, socket);

                } else {
                    int[] zug = new int[3], temp;
                    int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
                    if (zeit != 0) {
                        ende = System.currentTimeMillis();
                        Timer clock = new Timer(zeit - (ende - start));
                        long ges = 0;
                        int counter = 0, size;
                        while (counter < 30) {
                            if (AB) {
                                temp = Spiel.alphaBeta(counter, Spielernummer, clock, alpha, beta, sortierung);
                            } else {
                                temp = Spiel.sucheZug(counter, Spielernummer, clock, alpha, beta, sortierung);
                            }
                            if (temp == null) {
                                System.out.println("Abbruch Tiefe: "+counter);
                                if (info) {
                                    ende = System.currentTimeMillis();
                                    ges = (ende - start);
                                    bwZeitGesamt.write("" + ges);
                                    bwZeitGesamt.newLine();
                                    bwTiefe.write("" + (counter - 1));
                                    bwTiefe.newLine();
                                    bwZustaende.newLine();
                                    bwZeitProTiefe.newLine();
                                }
                                Spiel.getGueltigeZuege().listeLoeschen();
                                sendeZug(zug, socket);
                                return "";
                            } else {
                                if (window) {
                                    System.out.println("Tiefe" + counter);
                                    //System.out.println("Alpha: " + alpha + " Beta: " + beta + " Wert: " + temp[3]);
                                    if (temp[3] <= alpha) {
                                        alpha = Integer.MIN_VALUE;
                                        Spiel.setZustaende(0);
                                        continue;
                                    } else if (temp[3] >= beta) {
                                        beta = Integer.MAX_VALUE;
                                        Spiel.setZustaende(0);
                                        continue;
                                    } else {
                                        zug[0] = temp[0];
                                        zug[1] = temp[1];
                                        zug[2] = temp[2];
                                        counter++;
                                        alpha = temp[3] - windowSize;
                                        beta = temp[3] + windowSize;
                                    }
                                } else {
                                    zug[0] = temp[0];
                                    zug[1] = temp[1];
                                    zug[2] = temp[2];
                                    counter++;
                                }
                                if (clock.getStatus()) {
                                    if (info) {
                                        bwZustaende.write("" + Spiel.getZustaende());
                                        bwZustaende.newLine();
                                        ende = System.currentTimeMillis();
                                        bwZeitProTiefe.write("" + ((ende - start)-ges));
                                        bwZeitProTiefe.newLine();
                                        bwZeitGesamt.write("" + (ende-start));
                                        bwZeitGesamt.newLine();
                                        bwTiefe.write("" + (counter));
                                        bwTiefe.newLine();
                                        bwZustaende.newLine();
                                        bwZeitProTiefe.newLine();
                                    }
                                    Spiel.getGueltigeZuege().listeLoeschen();
                                    sendeZug(zug, socket);
                                    return "";
                                }
                            }
                            size = Spiel.getGueltigeZuege().getSize();
                            ende = System.currentTimeMillis();
                            if (info) {
                                bwZeitProTiefe.write("" + ((ende - start)-ges));
                                bwZeitProTiefe.newLine();
                                bwZustaende.write("" + Spiel.getZustaende());
                                bwZustaende.newLine();
                                //System.out.println(Spiel.getZustaende());
                            }
                            ges = (ende - start);
                            Spiel.getGueltigeZuege().listeLoeschen();
                            if (zeit - ges < (ges * counter * (size + 1) * Spiel.getHoehe() * Spiel.getBreite()) / 1000) {
                                break;
                            }
                        }
                        if (info) {
                            bwZeitGesamt.write("" + ges);
                            bwZeitGesamt.newLine();
                            bwTiefe.write("" + (counter - 1));
                            bwTiefe.newLine();
                            bwZustaende.newLine();
                            bwZeitProTiefe.newLine();
                        }
                    } else {
                        if (AB) {
                            zug = Spiel.alphaBeta(tiefe, Spielernummer, null, alpha, beta, sortierung);
                        } else {
                            zug = Spiel.sucheZug(tiefe, Spielernummer, null, alpha, beta, sortierung);
                        }
                        if (info) {
                            bwZustaende.write("" + Spiel.getZustaende());
                            bwZustaende.newLine();
                            //System.out.println(Spiel.getZustaende());
                            ende = System.currentTimeMillis();
                            bwZeitGesamt.write("" + (ende - start));
                            bwZeitGesamt.newLine();
                        }
                        Spiel.getGueltigeZuege().listeLoeschen();
                    }

                    sendeZug(zug, socket);
                    return "";
                    //Todo unterscheidung zwischen Ueberschreibsteinzuegen und normalen Zuegen wieder rueckgaengig mache
                }
                break;

            case 6:
                int x = message[0] << 8;
                x += message[1];

                int y = message[2] << 8;
                y += message[3];
                byte sonderfeld = message[4];
                byte spieler = message[5];
                Spielfeld = Spiel.getSpielfeld();

                if (bomben) {
                    Spiel.bombZug(x, y);
                    Spiel.PrintSpielfeld();
                } else if (spieler == Spielernummer) {
                    Spiel.ganzerZug(spieler, x, y, sonderfeld);
                } else {
                    int anzahlsteine = Spiel.getErsatzsteine(), anzahlbomben = Spiel.getBomben();
                    Spiel.setErsatzsteine(1);
                    Spiel.ganzerZug(spieler, x, y, sonderfeld);
                    Spiel.setErsatzsteine(anzahlsteine);
                    Spiel.setBomben(anzahlbomben);
                }
                break;
            case 7:
                if ((byte) nachricht[0] == Spielernummer) {
                    Spiel.PrintSpielfeld();
                    System.out.println(Spielernummer);
                    System.exit(-1);
                }
                break;
            case 8:
                bomben = true;
                break;
            case 9:
                isRunning = false;
                //bw.close();
                break;
        }
        return "";

    }

    private int windowSize(Spielbrett spiel) {
        int size = (spiel.getBreite() * spiel.getHoehe()*100 + spiel.getErsatzsteine() * 1000);
        System.out.println("Windowsize: " + size);
        return size;
    }

    private double durchschnitt(ArrayList<Long> d) {
        long summe = 0, counter = 0;
        double erg;
        for (int j = 0; j < d.size(); j++) {
            if (d.get(j) != 0) {
                summe = summe + d.get(j);
                counter++;
            }
        }
        erg = ((double) summe / (double) counter);
        return erg;
    }

    public void sendeZug(int[] zug, java.net.Socket socket) throws IOException {
        byte laenge = 5;
        byte nachricht[];
        byte zuSendendeNachricht[];
        nachricht = new byte[laenge];
        zuSendendeNachricht = new byte[laenge + 5];
        zuSendendeNachricht[0] = 5;
        zuSendendeNachricht[4] = laenge;
        int x = zug[0];
        int y = zug[1];
        byte sonderfeld = (byte) zug[2];

        zuSendendeNachricht[6] = (byte) (x);
        zuSendendeNachricht[5] = (byte) ((x >> 8));

        zuSendendeNachricht[8] = (byte) (y);
        zuSendendeNachricht[7] = (byte) (y >> 8);
        zuSendendeNachricht[9] = sonderfeld;

        schreibeNachricht(socket, zuSendendeNachricht);

    }


    private int getlaenge(byte[] bytes) {

        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i + 1] & 0x000000FF) << shift;
        }
        return value;

    }

}






