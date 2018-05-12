import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Client {


    public Client() throws IOException {

    }


    public void netzwerk(int p) throws IOException {
        String ip = "127.0.0.1"; // localhost
        int port = p;
        java.net.Socket socket = new java.net.Socket(ip, port); // verbindet sich mit Server
        byte Gruppennummer = 7;
        int laenge = 1;
        byte nachricht[];
        byte zuSendendeNachricht[];
        nachricht = new byte[laenge];
        zuSendendeNachricht = new byte[laenge + 5];
        zuSendendeNachricht[0] = 1;
        zuSendendeNachricht[4] = 1;
        zuSendendeNachricht[5] = 7;

        schreibeNachricht(socket, zuSendendeNachricht);
        empfangeNachricht(socket);


    }

    public void schreibeNachricht(java.net.Socket socket, byte[] nachricht) throws IOException {

        OutputStream socketOutputStream = socket.getOutputStream();
        socketOutputStream.write(nachricht);
    }

    public void empfangeNachricht(java.net.Socket socket) throws IOException {

        InputStream socketInputStream = socket.getInputStream();

        byte stream[];

        //ArrayList <Byte> nachricht = new ArrayList<>();
        stream = socketInputStream.readAllBytes();

        byte art =stream [0];
        int laenge = getlaenge(stream);
        char nachricht[] ;
        nachricht = new char[laenge];
        for(int i = 4; i <laenge+4;i++){
            nachricht[i-4] = (char)stream[i];
        }

        switch (art) {
            case 2:


            }
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






