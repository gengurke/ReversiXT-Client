import java.io.*;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {

         int port = 7777;
         String ip = "127.0.0.1";


        System.out.println("test");


        for (int i = 0; i < args.length; i++) {
           if(args[i].equals("-i")){
             ip  = args[i+1];
            }
            if(args[i].equals("-p")){
              port  = Integer.parseInt(args[i+1]);
            }
            if(args[i].equals("-h")){
                System.out.println("-i ip");
                System.out.println("-p port");
                System.out.println("-h help");


            }




        }
            Client client = new Client();
        //System.out.println("test");


        try {
            client.netzwerk(port,ip);
        } catch (IOException e) {
            System.out.println("no connection");
        }



        //System.out.println("test");
    }


}