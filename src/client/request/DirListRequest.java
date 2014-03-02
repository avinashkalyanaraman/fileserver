package client.request;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import commons.DirListing;

import utils.Conversions;

public class DirListRequest {

    public static void send(
            Socket clientSocket, byte pathtype, byte cmd, String path, 
            byte[] nonce) throws UnknownHostException, IOException{
        DefaultRequest.send(clientSocket, pathtype, cmd, path, nonce);
    }
    
    public static DirListResponse recv(Socket clientSocket)
            throws IOException{
    
        BufferedInputStream bis = null;

        bis = new BufferedInputStream(
                clientSocket.getInputStream());
        
        //status code + sizeof(read buffer)
        byte[] response_pfix = new byte[5];
        int bytes_read = bis.read(response_pfix, 0, response_pfix.length);
        if (bytes_read != response_pfix.length) {
            //Error!
            int errorCode = -1;
            if (bytes_read == 1) {
                errorCode = Conversions.getIntFromBytes( 
                        (byte)0x00, (byte)0x00, (byte)0x00,
                        response_pfix[0]);
                //XXX: Check later for bad errorCode return!
            }
            return new DirListResponse(errorCode, null);
        }
        
        int retVal = Conversions.getIntFromBytes( 
                (byte)0x00, (byte)0x00, (byte)0x00,
                response_pfix[0]);
        
        if (retVal != 0) {
            return new DirListResponse(retVal, null);
        }
        
        //success response!
        int read_buf_size = Conversions.getIntFromBytes(response_pfix[1], 
                response_pfix[2], response_pfix[3], response_pfix[4]);
       
        byte[] contents = new byte[512* 1024];
        byte[] read_buff = new byte[read_buf_size];
        
        int readOffset = 0;
        while(true) {
            
            //I 've read how much I am supposed to read
            if (readOffset == read_buf_size) {
                DirListing dl = DirListing.deserialize(read_buff);
                return new DirListResponse(0, dl);
            }
            
            bytes_read = bis.read(contents, 0, contents.length);
            if (bytes_read == -1) {
                //XXX: Server could 've sent a smaller response!
                return new DirListResponse(-1, null);
            }
            
            //Server sending response larger than it should -
            // shouldn't happen actually!
            if (readOffset + bytes_read > read_buf_size) {
                return new DirListResponse(-1, null);
            }
            
            System.arraycopy(contents, 0, read_buff, 
                    readOffset, bytes_read);            
            readOffset += bytes_read;

        }
        
    }
    
}
