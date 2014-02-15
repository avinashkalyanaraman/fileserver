package client.request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import server.Constants;

public class ReadRequest extends DefaultRequest{

    public static void send(Socket clientSocket, String path, long offset,
            long numBytes, int nonce) throws UnknownHostException, IOException{

        BufferedOutputStream bos = null;

        bos = new BufferedOutputStream(
                clientSocket.getOutputStream());


        //1K buffer containing request!
        byte[] request = new byte[1024];
        int w_offset = 0;

        w_offset = Commons.formPrefix(request, Constants.FILE_OPN_BYTE,
                Constants.FILE_READ_CMD_BYTE, path, nonce);

        //filling args!
        ByteBuffer bb_arg1= ByteBuffer.allocate(8);
        long arg1 = offset;
        bb_arg1.putLong(arg1);
        byte[] b_arg1 = bb_arg1.array();
        System.arraycopy(b_arg1, 0, request, w_offset, b_arg1.length);
        w_offset += b_arg1.length;

        ByteBuffer bb_arg2= ByteBuffer.allocate(8);
        long arg2 = numBytes;
        bb_arg2.putLong(arg2);
        byte[] b_arg2 = bb_arg2.array();
        System.arraycopy(b_arg2, 0, request, w_offset, b_arg2.length);
        w_offset += b_arg2.length;

        bos.write(request, 0, w_offset);
        bos.flush();

    }

}
