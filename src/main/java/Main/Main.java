package Main;

import java.io.*;

public class Main {


    public static void main(String[] args) throws IOException {
         boolean a = true,w= true,s = true;


        int port = 7777;
         String ip = "127.0.0.1";


        //System.out.println("test");


        for (int i = 0; i < args.length; i++) {
           switch(args[i]){
            case "-i":
             ip  = args[i+1];
            break;
               case "-p":
              port  = Integer.parseInt(args[i+1]);
            break;
               case "-h":
                System.out.println("-i ip");
                System.out.println("-p port");
                System.out.println("-h help");
            break;
            case "-a":
                a = false;
                break;
               case "-w":
                   w = false;
                   break;
               case "-s":
                   s = false;


            }
        }
            Client client = new Client(a,w,s);
        //System.out.println("test");

        try {
            client.netzwerk(port,ip);
        } catch (IOException e) {
            System.out.println("no connection");
        }


        //System.out.println("test");
    }


}