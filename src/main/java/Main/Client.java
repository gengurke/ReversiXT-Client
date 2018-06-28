package Main;

import java.io.*;

public class Client {
    private Spielbrett Spiel;
    private byte Spielernummer;
    private boolean isRunning;
    private boolean bomben;
    private long start, ende;

    public Client() throws IOException {

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

        //TODO - Heursitik ausgabe
        //Heuristik heuristik = new Heuristik(Spiel, Spielernummer);
        //System.out.println(heuristik);

        while (isRunning) {
            empfangeNachricht(socket);
        }


    }

    public void schreibeNachricht(java.net.Socket socket, byte[] nachricht) throws IOException {

        OutputStream socketOutputStream = socket.getOutputStream();
        socketOutputStream.write(nachricht);
    }

    public String empfangeNachricht(java.net.Socket socket) throws IOException {
        start = System.currentTimeMillis();

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
                char[][][] Spielfeld = Spiel.getSpielfeld();
                long zeit = 0;
                byte tiefe;
                for (int i = 0; i < 4; i++) {
                    zeit = zeit << 8;
                    zeit += Byte.toUnsignedInt(message[i]);
                }
                tiefe = (byte) nachricht[4];

                if(bomben){

                    Spiel.gueltigeBombZuege(Spielernummer);
                    //Todo Sinnvolle Zugauswahl
                    Spielfeld = Spiel.getSpielfeld();

                    for (int y = 0; y < Spiel.getHoehe(); y++) {
                        for (int x = 0; x < Spiel.getBreite(); x++) {
                            int[] zug = new int[3];
                            //todo entfernen
                            if(Spielfeld[3][3][1] == 'B'){
                                zug[0] = 3;
                                zug[1] = 1;
                                zug[2] = 0;
                                sendeZug(zug, socket);
                                return "";

                            }
                            else if(Spielfeld[x][y][1] == 'B') {
                                //TODO 3 durch X und Y ersetzen
                                zug[0] = x;
                                zug[1] = y;
                                zug[2] = 0;
                                sendeZug(zug, socket);
                                return "";
                            }
                        }
                    }

                } else {

                    int[] zug = new int[3], temp;
                    int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
                    if (zeit != 0) {
                        ende = System.currentTimeMillis();
                        Timer clock = new Timer(zeit-(ende-start));
                        long ges = 0;
                        int counter = 0, size;
                        while (counter < 30) {
                            temp = Spiel.alphaBeta(counter, Spielernummer, clock, alpha,beta);
                            if(temp == null) {
                                sendeZug(zug, socket);
                                return "";
                            } else {
                                zug = temp;
                            }
                            size = Spiel.getGueltigeZuege().getSize();
                            ende = System.currentTimeMillis();
                            ges = ges + (ende - start);
                            Spiel.getGueltigeZuege().listeLoeschen();
                            counter++;
                            if(zeit-ges < (ges*counter*size*Spiel.getHoehe()*Spiel.getBreite())/80000) {
                                break;
                            }
                        }

                    } else{
                        zug = Spiel.alphaBeta(tiefe, Spielernummer, null, alpha, beta);
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

                if (x == 16 && y == 2) {
                    int test = 1;
                }

                if(bomben){
                     Spiel.bombZug(x,y);
                    //Spiel.leichtBombZug(x,y);
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
                //System.out.println("Ende");
                isRunning = false;
                /*System.out.println("Zeit: ");
                durchschnitt(durchschnitt);
                System.out.println("Zustaende: ");
                durchschnitt(Spiel.zustande);
                System.out.println("Zeit/Zustand: ");
                durchschnitt(Spiel.zeit_zustand);*/
                break;
        }
        return "";

    }

    private void durchschnitt(long[] d) {
        long summe = 0, counter = 0;
        double erg;
        for (int j = 0; j < d.length; j++) {
            if (d[j] != 0) {
                summe = summe + d[j];
                counter++;
            }
        }
        erg = ((double) summe / (double) counter);
        System.out.println(erg);
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






