package server.response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import commons.ErrorCode;

public class DefaultResponse {

    /**
     * Sends the response code alone!
     * @param socket
     * @param code
     */
    public static void send(Socket socket, byte code,
            String errorMsg) {
        if (socket == null) {
            return;
        }
        
        if (errorMsg == null && code != ErrorCode.SUCCESS_CODE) {
            errorMsg = ErrorCode.getErrorMsgFromErrorCode(code);
        }
        
        byte[] msgBytes;
        if (errorMsg != null) {
            msgBytes = errorMsg.getBytes();
        } else {
            msgBytes = new byte[0];
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
