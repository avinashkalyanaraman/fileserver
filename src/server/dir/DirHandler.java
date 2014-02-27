package server.dir;

import java.io.File;
import java.net.Socket;

import server.file.FileHandler;
import server.response.DefaultResponse;
import commons.ErrorCode;

public class DirHandler {

    /**
     * Function to create a directory at path
     * @param path
     * @return
     */
    public static int mkdir(String path, Socket socket) {
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File newDir = new File (path);        
        if (newDir.exists()) {
            DefaultResponse.send(socket, 
                    ErrorCode.DIR_ALREADY_EXISTS_CODE);
            return ErrorCode.DIR_ALREADY_EXISTS_CODE;
        }
        
        if (newDir.mkdir()) {
            //success!
            DefaultResponse.send(socket, 
                    ErrorCode.SUCCESS_CODE);
            return ErrorCode.SUCCESS_CODE;
        }
        
        DefaultResponse.send(socket, 
                ErrorCode.MKDIR_FAIL_CODE);
        
        return ErrorCode.MKDIR_FAIL_CODE;
    }
    
    public static int list(String path, Socket socket) {
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File dir = new File(path);
        if (!dir.exists()) {
            DefaultResponse.send(socket,
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        File[] files = dir.listFiles();
        
        for(File file: files) {
            System.out.println(file.getName());
        }
        return 0;
    }
        
    public static int listlong(String path, Socket socket) {
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File dir = new File(path);
        if(!dir.exists()) {
            DefaultResponse.send(socket,
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        File[] files = dir.listFiles();
        
        for(File file: files) {
           FileHandler.stat(file.getAbsolutePath(),
                   socket); 
        }
        
        return 0;
    }
    
    
    public static int remove(String path, Socket socket) {
        if (path == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.BAD_PATH_CODE);
            return ErrorCode.BAD_PATH_CODE;
        }
        
        File dir = new File(path);
        if (!dir.exists()) {
            DefaultResponse.send(socket, 
                    ErrorCode.FNF_CODE);
            return ErrorCode.FNF_CODE;
        }
        
        if(dir.delete()) {
            DefaultResponse.send(socket, 
                    ErrorCode.SUCCESS_CODE);
            return ErrorCode.SUCCESS_CODE;
        } else {
            DefaultResponse.send(socket, 
                    ErrorCode.DELETE_FAIL_CODE);
            return ErrorCode.DELETE_FAIL_CODE;
        }
    }
}
