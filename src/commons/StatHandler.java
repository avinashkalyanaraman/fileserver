package commons;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import utils.PathType;

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
        
        StatResponse sr = new StatResponse(file.getFileName().toString(),
                attr.lastAccessTime(), attr.lastModifiedTime(),
                attr.size(), type);
        
        byte[] response = sr.serialize();
        if (response == null) {
            response = new byte[0];
        }
        
        ResponseHandler.sendStatResponse(socket, response);
        
        return ErrorCode.SUCCESS_CODE;
    }
}
