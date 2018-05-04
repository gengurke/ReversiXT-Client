import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Client {


    public Client()throws  IOException{

    }


    public void netzwerk(int p)throws IOException {
        String ip = "127.0.0.1"; // localhost
        int port = p;
        java.net.Socket socket = new java.net.Socket(ip,port); // verbindet sich mit Server
        byte Gruppennummer = 7;
        int laenge = 0;
        byte nachricht[];
        byte zuSendendeNachricht[];
        nachricht = new byte[laenge];
        zuSendendeNachricht = new byte[laenge+5];
        zuSendendeNachricht[0] = Gruppennummer;

        schreibeNachricht(socket,zuSendendeNachricht);


    }
    public void schreibeNachricht(java.net.Socket socket, byte[] nachricht) throws IOException {
        PrintWriter printWriter =
                new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));
        printWriter.print(nachricht);
        printWriter.flush();
    }





}
