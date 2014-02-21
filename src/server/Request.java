package server;

import java.net.Socket;

import commons.ErrorCode;
import commons.ResponseHandler;

import dir.DirHandler;
import file.FileHandler;
import utils.PathType;

public class Request {
    
    private PathType _type;
    private String _cmd;
    private String _path;
    private int _writeBufLen;
    private byte[] _writeBuf;
    
    //This is the offset into writeBuf where the next byte of contents from
    // client gets written to. Eventually, at the end it should be equal to
    // _writeBufLen.
    private int _numBytesRead;
    
    private long[] _args = new long[Constants.MAX_NUM_ARGS];
    
    public Request(PathType type, String cmd, String path,
            int buflen, long[] args) {
        _type = type;
        _cmd = cmd;
        _path = path;
        _writeBufLen = buflen;
        _writeBuf = new byte[buflen];
        _numBytesRead = 0;

        System.arraycopy(args, 0, _args, 0, args.length);
        
        System.out.println("Creating request with type : " + 
                type + " cmd = " + cmd + "path = " + path + 
                " write buf len = " + _writeBufLen) ;
        for (int ii=0; ii< args.length; ii++) {
            System.out.println("arg["+ii+"] : " + args[ii]);
        }

    }
    
    public int getWriteBufSize() {
        return _writeBufLen;
    }
    
    public boolean isWrite() {
        return (_cmd.equals("w") || (_cmd.equals("a"))); 
    }
        
    
    public byte[] getWriteBuf() {
        return _writeBuf;
    }


    public void writeToBuf(byte[] src, int offset, int numBytes) {
        System.arraycopy(src, offset, _writeBuf, _numBytesRead, numBytes);
        _numBytesRead += numBytes;
    }

    public int getNumBytesRead() {
        return _numBytesRead;
    }
    
    public PathType getPathType() {
        return _type;
    }
    
    public String getPath() {
        return _path;
    }

    //Call the corresponding fs method and write response
    public void handle(Socket socket) {
        if (socket == null) {
            return;
        }
        
        if (_type == PathType.FILE) {
            handleFileOp(socket);
        } else if (_type == PathType.DIRECTORY){
            handleDirOp(socket);
        }
    }


    private void handleFileOp(Socket socket) {
        if (_cmd == null) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.INVALID_CMD_CODE);
        }
        
        if(_cmd.equalsIgnoreCase(Constants.FILE_READ_CMD)) {
            FileHandler.read(_path, _args[0], _args[1], socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_WRITE_CMD)) {
            FileHandler.write(_path, _writeBuf, _args[0], socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_APPEND_CMD)) {
            FileHandler.append(_path, _writeBuf, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_STAT_CMD)) {
            FileHandler.stat(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_DELETE_CMD)) {
            FileHandler.delete(_path, socket);
        } else {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.INVALID_CMD_CODE);
        }
    }


    private void handleDirOp(Socket socket) {
        if (_cmd == null) {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.INVALID_CMD_CODE);
        }
        
        if (_cmd.equalsIgnoreCase(Constants.DIR_CREATE_CMD)) {
            DirHandler.mkdir(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.DIR_DELETE_CMD)) {
            DirHandler.remove(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.DIR_LISTLONG_CMD)) {
            DirHandler.listlong(_path, socket);
        } else {
            ResponseHandler.sendResponseCode(socket, 
                    ErrorCode.INVALID_CMD_CODE);
        }
        
    }
}
