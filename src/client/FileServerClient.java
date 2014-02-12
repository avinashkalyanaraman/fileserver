package client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import server.Constants;

public class FileServerClient {

    //XXX:Worry about return type!
    public static byte[] read(String path, long offset, 
            long numBytes, int nonce) {

        if (path == null) {
            //XXX: Handle error!
            return null;
        }        

        Socket clientSocket;
        BufferedOutputStream bos = null;
        try {
            clientSocket = new Socket("localhost", Constants.port);
            bos = new BufferedOutputStream(
                    clientSocket.getOutputStream());
        } catch (UnknownHostException e) {
            //XXX: Handle error!
            return null;
        } catch (IOException e) {
            //XXX: Handle error
            return null;
        }        

        //1K buffer containing request!
        byte[] request = new byte[1024];
        int w_offset = 0;

        w_offset = formPrefix(request, Constants.FILE_OPN_BYTE,
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

        try {
            bos.write(request, 0, w_offset);
            bos.close();
            
            //XXX: Read response!
        } catch (IOException ioe) {
            //return error;
            return null;
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }  
        }
        //return read response
        return null;
    }


    //Returns 0 or 1 depending on whether write was successful or not!
    public static int write(String path, byte[] wb, 
            long offset, int nonce) {                

        if (path == null) {
            //XXX: Handle error!
            return -1;
        }        

        Socket clientSocket;
        BufferedOutputStream bos = null;
        try {
            clientSocket = new Socket("localhost", Constants.port);
            bos = new BufferedOutputStream(
                    clientSocket.getOutputStream());
        } catch (UnknownHostException e) {
            //XXX: Handle error!
            return -1;
        } catch (IOException e) {
            //XXX: Handle error
            return -1;
        }        

        byte[] request = new byte[1024 + wb.length];
        int w_offset = 0;

        w_offset = formPrefix(request, Constants.FILE_OPN_BYTE,
                Constants.FILE_WRITE_CMD_BYTE, path, nonce);

        //filling args!
        ByteBuffer bb_arg1= ByteBuffer.allocate(8);
        long arg1 = offset;
        bb_arg1.putLong(arg1);
        byte[] b_arg1 = bb_arg1.array();
        System.arraycopy(b_arg1, 0, request, w_offset, b_arg1.length);
        w_offset += b_arg1.length;

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
        
        try {
            bos.write(request, 0, w_offset);
            bos.close();
            
            //XXX: Read response!
        } catch (IOException ioe) {
            //return error;
            return -1;
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return -1;
            }  
        }
        
        return 0;
    }

    //Returns 0 or 1 depending on whether append was successful or not!
    public static int append(String path, byte[] wb, int nonce) {

        if (path == null) {
            //XXX: Handle error!
            return -1;
        }        

        Socket clientSocket;
        BufferedOutputStream bos = null;
        try {
            clientSocket = new Socket("localhost", Constants.port);
            bos = new BufferedOutputStream(
                    clientSocket.getOutputStream());
        } catch (UnknownHostException e) {
            //XXX: Handle error!
            return -1;
        } catch (IOException e) {
            //XXX: Handle error
            return -1;
        }        

        //1K buffer containing request!
        byte[] request = new byte[1024 + wb.length];
        int w_offset = 0;

        w_offset = formPrefix(request, Constants.FILE_OPN_BYTE,
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
        
        try {
            bos.write(request, 0, w_offset);
            bos.close();
            
            //XXX: Read response!
        } catch (IOException ioe) {
            //return error;
            return -1;
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return -1;
            }  
        }
        
        return 0;
    }

    //XXX:Worry about return type!
    public static int stat(String path, int nonce) {
        return 0;
    }

    
    private static int sendNonParameterizedRequest(
            byte pathtype, byte cmd, String path, int nonce) {
        
        Socket clientSocket;
        BufferedOutputStream bos = null;
        
        try {
            clientSocket = new Socket("localhost", Constants.port);
            bos = new BufferedOutputStream(
                    clientSocket.getOutputStream());
        } catch (UnknownHostException e) {
            //XXX: Handle error!
            return -1;
        } catch (IOException e) {
            //XXX: Handle error
            return -1;
        }        

        byte[] request = new byte[1024];
        int w_offset = 0 ;

        w_offset = formPrefix(request, pathtype, cmd
                , path, nonce);
        
        try {
            bos.write(request, 0, w_offset);
            bos.close();
            
            //XXX: Read the response!
        } catch (IOException ioe) {
            //return error;
            return -1;
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                return -1;
            }  
        }
        
        //return response
        return 0;
    }
    
    public static int rm(String path, int nonce) {
        
        if (path == null) {
            //XXX: Handle error!
            return -1;
        }        
        
        int retVal = sendNonParameterizedRequest(
                Constants.FILE_OPN_BYTE, Constants.FILE_DELETE_CMD_BYTE,
                path, nonce);
        
        return retVal;
        
    }

    public static int mkdir (String path, int nonce) {
        
        if (path == null) {
            //XXX: Handle error!
            return -1;
        }        
        
        int retVal = sendNonParameterizedRequest(
                Constants.FILE_OPN_BYTE, Constants.DIR_CREATE_CMD_BYTE,
                path, nonce);
        
        return retVal;
    }

    public static int rmdir(String path, int nonce) {
        
        if (path == null) {
            //XXX: Handle error!
            return -1;
        }        
        
        int retVal = sendNonParameterizedRequest(
                Constants.FILE_OPN_BYTE, Constants.DIR_DELETE_CMD_BYTE,
                path, nonce);
        
        return retVal;
    }

    //XXX: Worry about return type
    public static int list(String path, int nonce) {
        return 0;
    }

    //XXX: Worry about return type
    public static int listlong(String path, int nonce) {
        return 0;
    }

    //Returns the number of bytes written to request
    private static int formPrefix(byte[] request, byte pathtype,
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

        return 0;
    }

}
