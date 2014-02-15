package client.request;

import java.nio.ByteBuffer;

public class Commons {

    //Returns the number of bytes written to request
    public static int formPrefix(byte[] request, byte pathtype,
            byte cmd, String path, int nonce) {

        int w_offset = 0;

        //nonce!
        ByteBuffer bb_nonce = ByteBuffer.allocate(4);
        bb_nonce.putInt(nonce);
        byte[] b_nonce = bb_nonce.array();        
        System.arraycopy(b_nonce, 0, request, w_offset, b_nonce.length);
        w_offset += b_nonce.length;

        //f/d
        request[w_offset++] = pathtype;
        //cmd
        request[w_offset++] = cmd;

        //pathlength and path
        byte[] b_path = path.getBytes();        
        int pathLen = b_path.length;

        //pathlen first
        ByteBuffer bb_pathlen = ByteBuffer.allocate(4);
        //the initial order of a byte buffer is always BIG_ENDIAN.        
        bb_pathlen.putInt(pathLen);
        byte[] b_pathLen = bb_pathlen.array();       
        System.arraycopy(b_pathLen, 0, request, w_offset, b_pathLen.length);
        w_offset += b_pathLen.length;

        //path
        System.arraycopy(b_path, 0, request, w_offset, b_path.length);
        w_offset += b_path.length;

        return w_offset;
    }

}
