package client.request;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.Conversions;

public class DefaultRequest {

    public static void send(
            Socket clientSocket, byte pathtype, byte cmd, String path, 
            byte[] nonce) throws UnknownHostException, IOException{
        
        BufferedOutputStream bos = null;
        
        bos = new BufferedOutputStream(
                clientSocket.getOutputStream());

        byte[] request = new byte[1024];
        int w_offset = 0 ;

        w_offset = Commons.formPrefix(request, pathtype, cmd
                , path, nonce);

        bos.write(request, 0, w_offset);
        bos.flush();
         
    }
    
    public static int recv(Socket clientSocket) throws IOException{
        BufferedInputStream bin = null;
        bin = new BufferedInputStream(clientSocket.getInputStream());
        
        byte[] response = new byte[1024];
        
        int bytes_read = bin.read(response, 0, response.length);
        if (bytes_read == -1 ) {
            return -1;
        }
        
        int retVal = Conversions.getIntFromBytes( 
                (byte)0x00, (byte)0x00, 
                (byte)0x00, response[0]);
        return retVal;
        
    }
}
