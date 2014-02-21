package client.request;

import java.nio.ByteBuffer;

public class Commons {

    //Returns the number of bytes written to request
    public static int formPrefix(byte[] request, byte pathtype,
            byte cmd, String path, byte[] nonce) {

        int w_offset = 0;                

        //nonce!
        if (nonce != null) {
            System.arraycopy(nonce, 0, request, w_offset, nonce.length);
            w_offset += nonce.length;
        }
        
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
