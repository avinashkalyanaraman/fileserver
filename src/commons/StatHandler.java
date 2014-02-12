package commons;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class StatHandler {

    public static int stat(String path, Socket socket) {
        if (path == null) {
            ResponseHandler.sendResponseCode(socket, 
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
            ResponseHandler.sendResponseCode(socket,
                    ErrorCode.IO_ERROR_CODE);
            return ErrorCode.IO_ERROR_CODE;
        }
        
        if (attr == null) {
            //couldn't retrieve file attributes
            ResponseHandler.sendResponseCode(socket,
                    ErrorCode.ATTR_FETCH_FAIL_CODE);
            return ErrorCode.ATTR_FETCH_FAIL_CODE;
        }
        
        System.out.println("name             = " + file.getFileName());
        System.out.println("creationTime     = " + attr.creationTime());
        System.out.println("lastAccessTime   = " + attr.lastAccessTime());
        System.out.println("lastModifiedTime = " + attr.lastModifiedTime());
        System.out.println("size             = " + attr.size());
        
        if (attr.isRegularFile()) {
            System.out.println("File");
        } else if(attr.isDirectory()) {
            System.out.println("Dir");
        } else if (attr.isSymbolicLink()) {
            System.out.println("SymLink");
        } else {
            System.out.println("Other");
        }
        
        
        return ErrorCode.SUCCESS_CODE;
    }
}
