package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import commons.Constants;

public class Driver {
    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        
        BufferedInputStream bis = 
                new BufferedInputStream(System.in);
        //int nonce = -1;
        byte[] nonce = new byte[Constants.NONCE_SIZE];
        try {
            int nonce_size = bis.read(nonce);
            //We ensure we get 16 bytes from client!
            if (nonce_size != Constants.NONCE_SIZE) {
                System.out.println("0");
                System.exit(-1);
            }
            bis.close();

        } catch (Exception e) {
            System.out.println("0");
            System.exit(-1);
        }
        
        //open server socket
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
        } catch (IOException ioe) {
            System.out.println("0");
            System.exit(-1);
        }        
        
        System.out.println(socket.getLocalPort());
        //write(socket.getLocalPort());
        
        //start the monitoring thread!
        Monitor monitor = new Monitor(System.currentTimeMillis());
        Thread monitor_thread = new Thread(monitor);
        monitor_thread.start();
        
        //Starting select-accept loop!
        while (true) {
            try {
                //wait for request
                Socket connxn = socket.accept();
                
                //Only localhost requests are entertained!
                if (!connxn.getInetAddress().isLoopbackAddress()) {
                    continue;
                } else {
                    Monitor.setLastReqTime(System.currentTimeMillis());
                }
                
                RequestHandler reqHandler = new RequestHandler(connxn,
                        nonce);
                Thread t = new Thread(reqHandler);
                t.start();                                              
            }catch(Exception e) {
                
            }
        }
    }   
    
    public static void write(String s) throws Exception{

        BufferedOutputStream bos = new BufferedOutputStream(System.out);
        s = s+ "\n";
        byte [] b = s.getBytes();
        bos.write(b);
        bos.flush();
        
    }

}
