package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Driver {
    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        
        /*TODO: Recv nonce from stdin*/
        BufferedReader br = 
                new BufferedReader(new InputStreamReader(System.in));
        int nonce = -1;
        try {
            nonce = Integer.parseInt(br.readLine());                        
            br.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.err.println("Error obtaining nonce! Committing harakiri!");
            System.exit(-1);
            e.printStackTrace();
        }
        
        System.out.println("Nonce = " + nonce);
        
        //open server socket
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(Constants.port);
        } catch (IOException ioe) {
            System.err.println("Could not start server");
            System.exit(-1);
        }        
        
        System.out.println("Started server at port: " + Constants.port);
        
        //Starting select-accept loop!
        while (true) {
            try {
                //wait for request
                Socket connxn = socket.accept();
                
                RequestHandler reqHandler = new RequestHandler(connxn,
                        nonce);
                Thread t = new Thread(reqHandler);
                t.start();                                              
            }catch(Exception e) {
                
            }
        }
    }   

}
