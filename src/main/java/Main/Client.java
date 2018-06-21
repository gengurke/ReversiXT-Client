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

        while(isRunning) {
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

        socketInputStream.read(stream,0,5);


        byte art = stream[0];
        int laenge = getlaenge(stream);
        char nachricht[] ;
        nachricht = new char[laenge];
        byte message[] = new byte[laenge];
        socketInputStream.read(message,0,laenge);

        for(int i = 0; i <laenge;i++){
            nachricht[i] = (char)message[i];
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
                long zeit =0;
                byte tiefe;
                for(int i = 0; i<4;i++){
                    zeit = zeit <<8;
                    zeit += Byte.toUnsignedInt(message[i]);
                }
                tiefe = (byte)nachricht[4];

                if(bomben){
                    Spiel.gueltigeBombZuege(Spielernummer);
                    //Todo Sinnvolle Zugauswahl
                    Spielfeld = Spiel.getSpielfeld();

                    for (short y = 0; y < Spiel.getHoehe(); y++) {
                        for (short x = 0; x < Spiel.getBreite(); x++) {
                            short[] zug = new short[3];
                            if (Spielfeld[x][y][1] == 'B') {
                                zug[0] = x;
                                zug[1] = y;
                                zug[2] = 0;
                                sendeZug(zug, socket);
                                return "";
                            }
                        }
                    }

                } else {
                    //Todo Sinnvolle Zugauswahl
                    short[] zug = new short[3];
                    if(zeit!=0) {
                        long ges = 0;
                        int i = 0, size, feldgroesse = 1;
                        if(Spiel.getBreite() < 10 && Spiel.getHoehe() < 10) {
                            feldgroesse = 1;
                        } else if(Spiel.getBreite() < 20 && Spiel.getHoehe() < 20) {
                            feldgroesse = 2;
                        } else if(Spiel.getBreite() < 30 && Spiel.getHoehe() < 30) {
                            feldgroesse = 3;
                        } else if (Spiel.getBreite() < 40 && Spiel.getHoehe() < 40) {
                            feldgroesse = 4;
                        } else {
                            feldgroesse = 5;
                        }
                        while(i < 30) {
                            System.out.println(i);
                            zug = Spiel.alphaBeta(i, Spielernummer);
                            size = Spiel.getGueltigeZuege().getSize();
                            if(Spiel.hatUeberschreibsteine()) {
                                feldgroesse = feldgroesse*2;
                            }
                            if(Spiel.ustein) {
                                feldgroesse = feldgroesse*4;
                                Spiel.ustein = false;
                            }
                            ende = System.currentTimeMillis();
                            ges = ges+ (ende - start);
                            if(size <= 10) {
                                if((zeit-ges) < ges*feldgroesse){
                                    i = 100;
                                }
                                System.out.println(size+" "+ zeit+ " "+ges+ " "+Spiel.lastSize+" "+ges*feldgroesse);
                            } else if(size > 10 && size < 50) {
                                if(zeit-ges < ges*3*feldgroesse) {
                                    i = 100;
                                }
                                System.out.println(size+" "+ zeit+ " "+ges+ " "+Spiel.lastSize+" "+ges*3*feldgroesse);
                            } else if(size >= 50 && size < 100) {
                                if(zeit-ges < ges*4*feldgroesse) {
                                    i = 100;
                                }
                                System.out.println(size+" "+ zeit+ " "+ges+ " "+Spiel.lastSize+" "+ges*4*feldgroesse);
                            } else {
                                if(zeit-ges < ges*8*feldgroesse) {
                                    i = 100;
                                }
                                System.out.println(size+" "+ zeit+ " "+ges+ " "+Spiel.lastSize+" "+ges*8*feldgroesse);
                            }
                            i++;
                            Spiel.getGueltigeZuege().listeLoeschen();
                        }

                    } else if (tiefe != 0){
                        zug = Spiel.alphaBeta(tiefe, Spielernummer);
                    }

                    sendeZug(zug, socket);

                    return "";
                    //Todo unterscheidung zwischen Ueberschreibsteinzuegen und normalen Zuegen wieder rueckgaengig mache
                }
                break;

            case 6:
                short x = (short)((short)message[0]<<8);
                x += message[1];

                short y = (short)((short)message[2]<<8);
                y += message[3];
                byte sonderfeld = message[4];
                byte spieler = message[5];
                Spielfeld = Spiel.getSpielfeld();

                if(bomben){
                     Spiel.bombZug(x,y,0,0,0);
                    //Spiel.leichtBombZug(x,y);
                } else if (spieler == Spielernummer){
                    Spiel.ganzerZug(spieler, x, y, sonderfeld);
                } else {
                    int anzahlsteine = Spiel.getUeberschreibsteine(), anzahlbomben = Spiel.getBomben();
                    Spiel.setUeberschreibsteine(1);
                    Spiel.ganzerZug(spieler, x, y, sonderfeld);
                    Spiel.setUeberschreibsteine(anzahlsteine);
                    Spiel.setBomben(anzahlbomben);
                }
               break;
            case 7:
                if((byte) nachricht[0] == Spielernummer) {
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
        for(int j = 0; j < d.length; j++) {
            if(d[j] != 0) {
                summe = summe + d[j];
                counter++;
            }
        }
        erg = ((double) summe/ (double) counter);
        System.out.println(erg);
    }

    public void sendeZug(short[] zug,java.net.Socket socket)throws IOException{
        byte laenge = 5;
        byte nachricht[];
        byte zuSendendeNachricht[];
        nachricht = new byte[laenge];
        zuSendendeNachricht = new byte[laenge + 5];
        zuSendendeNachricht[0] = 5;
        zuSendendeNachricht[4] = laenge;
        short x = zug[0];
        short y = zug[1];
        byte sonderfeld = (byte)zug[2];

        zuSendendeNachricht[6] = (byte)(x);
        zuSendendeNachricht[5] = (byte)((x >> 8));

        zuSendendeNachricht[8] = (byte)(y);
        zuSendendeNachricht[7] = (byte)(y >> 8);
        zuSendendeNachricht[9] = sonderfeld;

        schreibeNachricht(socket,zuSendendeNachricht);

    }


    private int getlaenge(byte[] bytes) {

            int value = 0;
            for (int i = 0; i < 4; i++) {
                int shift = (4 - 1 - i) * 8;
                value += (bytes[i+1] & 0x000000FF) << shift;
            }
            return value;

    }
}






