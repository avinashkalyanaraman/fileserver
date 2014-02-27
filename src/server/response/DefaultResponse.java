package server.response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DefaultResponse {

    /**
     * Sends the response code alone!
     * @param socket
     * @param code
     */
    public static void send(Socket socket, byte code) {
        if (socket == null) {
            return;
        }
        
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    socket.getOutputStream());
            
            byte[] buf = new byte[1];
            buf[0] = code;
            
            bos.write(buf);
            bos.close();
        } catch (IOException e) {
            System.err.println("Error writing out to client");
            e.printStackTrace();
        }
        
    }
    
}
