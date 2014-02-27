package server.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import server.response.DefaultResponse;
import server.response.ReadResponse;
import utils.PathType;
import commons.ErrorCode;
import server.response.StatResponse;

public class FileHandler {

    public static byte delete(String path, Socket socket) {
        
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File file = new File(path);
        if (!file.exists()) {
            DefaultResponse.send(socket,
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        if(file.delete()) {
            DefaultResponse.send(socket, 
                    ErrorCode.SUCCESS_CODE);
            return ErrorCode.SUCCESS_CODE;
        } else {
            DefaultResponse.send(socket, 
                    ErrorCode.DELETE_FAIL_CODE);
            return ErrorCode.DELETE_FAIL_CODE;
        }
    }
    
    public static int write(String path, byte[] buf, 
            long offset, Socket socket) {
        
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
        }
        
        RandomAccessFile raf;
        boolean failed = true;
        try {
            raf = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            //Write file doesn't exist!
            DefaultResponse.send(socket, 
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
            DefaultResponse.send(socket, 
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        } finally {
            try {
                raf.close();
                if (!failed) {
                    DefaultResponse.send(socket, 
                            ErrorCode.SUCCESS_CODE);
                    return ErrorCode.SUCCESS_CODE;
                }
            } catch (IOException e) {
                //Couldn't close file handle!
                DefaultResponse.send(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
        }
    }
    
    public static int append(String path, byte[] buf,
            Socket socket) {
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        RandomAccessFile raf;
        boolean failed = true;
        try {
            raf = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            //Write file doesn't exist!
            DefaultResponse.send(socket, 
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
            DefaultResponse.send(socket, 
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        } finally {
            try {
                raf.close();
                if (!failed) {
                    DefaultResponse.send(socket, 
                            ErrorCode.SUCCESS_CODE);
                    return ErrorCode.SUCCESS_CODE;
                }
                
            } catch (IOException e) {
                //Couldn't close file handle!
                DefaultResponse.send(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
        }
    }    
    
    
    public static int stat(String path, Socket socket) {
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        //XXX: What do we want to send during stat on a file!
        Path file = Paths.get(path);
        
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(file, BasicFileAttributes.class);
        } catch (IOException e) {
            // couldn't retrieve file attributes
            DefaultResponse.send(socket,
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        }
        
        if (attr == null) {
            //couldn't retrieve file attributes
            DefaultResponse.send(socket,
                    ErrorCode.ATTR_FETCH_FAIL_CODE);
            return ErrorCode.ATTR_FETCH_FAIL_CODE;
        }
        
        PathType type;
        if (attr.isRegularFile()) {
            type = PathType.FILE;
        } else if(attr.isDirectory()) {
            type = PathType.DIRECTORY;
        } else if (attr.isSymbolicLink()) {
            type = PathType.LINK;
        } else {
            type = PathType.OTHER;
        }
        
        commons.StatResponse sr = new commons.StatResponse(
                file.getFileName().toString(),
                attr.lastAccessTime(), attr.lastModifiedTime(),
                attr.size(), type);
        
        byte[] response = sr.serialize();
        if (response == null) {
            response = new byte[0];
        }
        
        StatResponse.send(socket, response);
        
        return ErrorCode.SUCCESS_CODE;
    
    }
    
    public static int read(String path, long offset, 
            long numBytes, Socket socket) {
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File file = new File(path);
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            DefaultResponse.send(socket, 
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
                DefaultResponse.send(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
            //XXX: Send 0 byte valid  read-response!
            
            byte[] buf = new byte[0];
            ReadResponse.send(socket, buf);
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
            DefaultResponse.send(socket, 
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                // Unable to close file
                DefaultResponse.send(socket, 
                        ErrorCode.IO_ERROR_CODE);
                return ErrorCode.IO_ERROR_CODE;
            }
        }

        String s = new String(b);
        System.out.println(s);
        
        //send the b bytes out!
        ReadResponse.send(socket, b);
        return ErrorCode.SUCCESS_CODE;
    }
}