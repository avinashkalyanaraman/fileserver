package test;

import java.io.BufferedOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import server.Constants;

public class TestClient {

    public static void main(String[] args) throws Exception {
        Socket clientSocket = new Socket("localhost", Constants.port);
        
        BufferedOutputStream bos = new BufferedOutputStream(
                clientSocket.getOutputStream());
        
        byte[] b = new byte[1024 * 1024];
        int offset = 0;
        int nonce = 121;
        
        ByteBuffer bb_nonce = ByteBuffer.allocate(4);
        //the initial order of a byte buffer is always BIG_ENDIAN.        
        bb_nonce.putInt(nonce);
        byte[] b_nonce = bb_nonce.array();        
        System.arraycopy(b_nonce, 0, b, offset, b_nonce.length);
        offset += b_nonce.length;
        
        b[offset++] = Constants.FILE_OPN_BYTE;
        b[offset++] = Constants.FILE_DELETE_CMD_BYTE;
        
        String path = "/Users/avinash/arbit/foo";
        byte[] b_path = path.getBytes();
        
        int pathLen = b_path.length;
        
        ByteBuffer bb = ByteBuffer.allocate(4);
        //the initial order of a byte buffer is always BIG_ENDIAN.        
        bb.putInt(pathLen);
        byte[] b_len = bb.array();       
        System.arraycopy(b_len, 0, b, offset, b_len.length);
        offset += b_len.length;
        
        System.arraycopy(b_path, 0, b, offset, b_path.length);
        offset += b_path.length;
        
        //filling args!
        ByteBuffer bb_arg1= ByteBuffer.allocate(8);
        long arg1 = 3;
        bb_arg1.putLong(arg1);
        byte[] b_arg1 = bb_arg1.array();
        System.arraycopy(b_arg1, 0, b, offset, b_arg1.length);
        offset += b_arg1.length;
        
//        ByteBuffer bb_arg2= ByteBuffer.allocate(8);
//        long arg2 = 111112;
//        bb_arg2.putLong(arg2);
//        byte[] b_arg2 = bb_arg2.array();
//        System.arraycopy(b_arg2, 0, b, offset, b_arg2.length);
//        offset += b_arg2.length;
//        
        
        String s= "my name is ak3ka";
        byte[] wb = s.getBytes();
        int wb_size = wb.length;
        
 //       byte[] wb = new byte[wb_size];
        ByteBuffer bb_arg3= ByteBuffer.allocate(4);
        int arg2 = wb_size;
        bb_arg3.putInt(arg2);
        byte[] b_arg3 = bb_arg3.array();
        System.arraycopy(b_arg3, 0, b, offset, b_arg3.length);
        offset += b_arg3.length;
        
        //writebuffer
        System.arraycopy(wb, 0, b, offset, wb.length);
        offset += wb.length;
        
        bos.write(b, 0, offset);
        
        bos.close(); //close will flush!
        clientSocket.close();
    }

}
