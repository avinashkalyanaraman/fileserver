package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;

import commons.ErrorCode;
import commons.ResponseHandler;
import commons.StatHandler;

public class FileHandler {

    public static byte delete(String path, Socket socket) {
        
        if (path == null) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File file = new File(path);
        if (!file.exists()) {
            ResponseHandler.sendResponseCode(socket,
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        if(file.delete()) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.SUCCESS_CODE);
            return ErrorCode.SUCCESS_CODE;
        } else {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.DELETE_FAIL_CODE);
            return ErrorCode.DELETE_FAIL_CODE;
        }
    }
    
    public static int write(String path, byte[] buf, 
            long offset, Socket socket) {
        
        if (path == null) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.BAD_PATH_CODE);
        }
        
        RandomAccessFile raf;
        boolean failed = true;
        try {
            raf = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            //Write file doesn't exist!
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        try {
            raf.seek(offset);
            raf.write(buf);
            failed = false;
            return ErrorCode.SUCCESS_CODE;
        } catch (IOException e) {
            //couldn't perform the seek/write!
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        } finally {
            try {
                raf.close();
                if (!failed) {
                    ResponseHandler.sendResponseCode(socket, 
                            ErrorCode.SUCCESS_CODE);
                    return ErrorCode.SUCCESS_CODE;
                }
            } catch (IOException e) {
                //Couldn't close file handle!
                ResponseHandler.sendResponseCode(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
        }
    }
    
    public static int append(String path, byte[] buf,
            Socket socket) {
        if (path == null) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        RandomAccessFile raf;
        boolean failed = true;
        try {
            raf = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            //Write file doesn't exist!
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        try {
            raf.seek(raf.length());
            raf.write(buf);
            failed = false;
            
            return ErrorCode.SUCCESS_CODE;
        } catch (IOException e) {
            //couldn't perform the seek/write!
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        } finally {
            try {
                raf.close();
                if (!failed) {
                    ResponseHandler.sendResponseCode(socket, 
                            ErrorCode.SUCCESS_CODE);
                    return ErrorCode.SUCCESS_CODE;
                }
                
            } catch (IOException e) {
                //Couldn't close file handle!
                ResponseHandler.sendResponseCode(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
        }
    }    
    
    
    public static int stat(String path, Socket socket) {
        return StatHandler.stat(path, socket);
    }
    
    public static int read(String path, long offset, 
            long numBytes, Socket socket) {
        if (path == null) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File file = new File(path);
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        //file exists, unless it has been deleted just after raf check!
        long size = file.length();
        
        if ((size - offset) < 0) {
            //XXX: What to do for read attempt beyond EOF?
            //attempt to read beyond EOF!
            //Simply return
            try {
                raf.close();
            } catch (IOException e) {
                //Unable to close raf
                ResponseHandler.sendResponseCode(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
            //XXX: Send 0 byte valid  read-response!
            
            byte[] buf = new byte[0];
            ResponseHandler.sendReadResponse(socket, buf);
            return ErrorCode.SUCCESS_CODE;
        }
        
        if(size < (offset + numBytes)) {
            numBytes = size - offset; 
        }        
        
        //lossy conversion from long to int!
        byte b[] = new byte[(int)numBytes];
        try {
            raf.seek(offset);
            raf.readFully(b);            
        } catch (IOException ioe) {
            //Couldn't seek/read!
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                // Unable to close file
                ResponseHandler.sendResponseCode(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
        }

        String s = new String(b);
        System.out.println(s);
        
        //send the b bytes out!
        ResponseHandler.sendReadResponse(socket, b);
        return ErrorCode.SUCCESS_CODE;
    }
}
