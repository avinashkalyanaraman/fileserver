package client.request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import server.Constants;

public class AppendRequest extends DefaultRequest{

    public static void send(Socket clientSocket, String path, byte[] wb,
            int nonce) throws UnknownHostException, IOException {
        
        BufferedOutputStream bos = new BufferedOutputStream(
                clientSocket.getOutputStream());


        //1K buffer containing request!
        byte[] request = new byte[1024 + wb.length];
        int w_offset = 0;

        w_offset = Commons.formPrefix(request, Constants.FILE_OPN_BYTE,
                Constants.FILE_APPEND_CMD_BYTE, path, nonce);

        //filling buflen
        int wb_size = wb.length;
        ByteBuffer bb_wbsize= ByteBuffer.allocate(4);
        bb_wbsize.putInt(wb_size);
        byte[] b_wbsize = bb_wbsize.array();
        System.arraycopy(b_wbsize, 0, request, w_offset, b_wbsize.length);
        w_offset += b_wbsize.length;
        
        //writebuffer
        System.arraycopy(wb, 0, request, w_offset, wb.length);
        w_offset += wb.length;

        bos.write(request, 0, w_offset);            
        bos.flush();
    }
    
}
