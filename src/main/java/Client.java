import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Client {
    private Spielbrett Spiel;
    private byte Spielernummer;
    private boolean isRunning;
    private boolean bomben;
   // short[] zug= new short[3];


    public Client() throws IOException {

    }


    public void netzwerk(int p) throws IOException {
        String ip = "127.0.0.1"; // localhost
        int port = p;
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
        Heuristik heuristik = new Heuristik(Spiel, Spielernummer);
        System.out.println(heuristik);

        while(isRunning) {
            empfangeNachricht(socket);
        }


    }

    public void schreibeNachricht(java.net.Socket socket, byte[] nachricht) throws IOException {

        OutputStream socketOutputStream = socket.getOutputStream();
        socketOutputStream.write(nachricht);
    }

    public String empfangeNachricht(java.net.Socket socket) throws IOException {


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
                if(bomben){
                    Spiel.gueltigeBombZuege();
                    //Todo Sinnvolle Zugauswahl

                    for (short y = 0; y < Spiel.getHoehe(); y++) {
                        for (short x = 0; x < Spiel.getBreite(); x++) {
                            short[] zug = new short[3];
                            if (Spielfeld[x][y][1] == 'B') {
                                zug[0] = x;
                                zug[1] = y;
                                sendeZug(zug, socket);
                                return "";
                            }
                        }
                    }


                } else {
                    //Todo Sinnvolle Zugauswahl
                    short[] zug = new short[3];
                    zug = Spiel.sucheZug(2, Spielernummer);
                    //if(zug[0] != -1 && zug[1] != -1) {
                        sendeZug(zug, socket);
                    //}
                    //sendeZug(zug, socket);
                    // Spiel.ganzerZug(Spielernummer, zug[1], zug[0], false);
                    return "";
                    //Todo unterscheidung zwischen Ueberschreibsteinzuegen und normalen Zuegen wieder rueckgaengig mache
                }
                break;

            case 6:
                boolean ustein = false;
                short x = (short)((short)message[0]<<8);
                x += message[1];

                short y = (short)((short)message[2]<<8);
                y += message[3];
                byte sonderfeld = message[4];
                byte spieler = message[5];
                Spielfeld = Spiel.getSpielfeld();
               if(Spielfeld[x][y][0] != '0'){
                   ustein = true;
               }
               if(sonderfeld == spieler){
                   Spielernummer = sonderfeld;
            }
            if(bomben){
                   Spiel.bombZug(x,y);
            }else {

                Spiel.ganzerZug(spieler, x, y, ustein);
            }
               if (ustein && (spieler == Spielernummer)){

                   Spiel.setUeberschreibsteine(Spiel.getUeberschreibsteine()-1);

               }
               break;
            case 7:
                System.exit(-1);
            case 8:
                bomben = true;
                break;
            case 9:
                System.out.println("Ende");
                isRunning = false;
                break;








        }
        return "";

    }

    public void sendeZug(short[] zug,java.net.Socket socket)throws IOException{
        byte laenge = 5;
        byte nachricht[];
        byte zuSendendeNachricht[];
        nachricht = new byte[laenge];
        zuSendendeNachricht = new byte[laenge + 5];
        zuSendendeNachricht[0] = 5;
        zuSendendeNachricht[4] = laenge;
       // short[] zug = Spiel.getZug;
       // zug[] = Spiel.getZug();
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






