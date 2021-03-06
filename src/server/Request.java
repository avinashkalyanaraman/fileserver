package server;

import java.net.Socket;

import commons.Constants;
import commons.ErrorCode;
import server.dir.DirHandler;
import server.file.FileHandler;
import server.response.DefaultResponse;
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
            DefaultResponse.send(socket, 
                    ErrorCode.INVALID_CMD_CODE,
                    ErrorCode.getErrorMsgFromErrorCode(
                            ErrorCode.INVALID_CMD_CODE));
        }
        
        if(_cmd.equalsIgnoreCase(Constants.FILE_READ_CMD)) {
            FileHandler.read(_path, _args[0], _args[1], socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_WRITE_CMD)) {
            FileHandler.write(_path, _writeBuf, _args[0], socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_TRUNCAPPEND_CMD)) {
            FileHandler.truncAppend(_path, _writeBuf, _args[0], socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_STAT_CMD)) {
            FileHandler.stat(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_DELETE_CMD)) {
            FileHandler.delete(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_CREAT_CMD)) {
            FileHandler.createNewFile(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_CAN_READ_CMD)) {
            FileHandler.canRead(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_CAN_WRITE_CMD)) {
            FileHandler.canWrite(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.FILE_DOESEXIST_CMD)) {
            FileHandler.isExists(_path, socket);
        } else {
            DefaultResponse.send(socket, 
                    ErrorCode.INVALID_CMD_CODE,
                    ErrorCode.getErrorMsgFromErrorCode(
                            ErrorCode.INVALID_CMD_CODE));
        }
    }


    private void handleDirOp(Socket socket) {
        if (_cmd == null) {
            DefaultResponse.send(socket, 
                    ErrorCode.INVALID_CMD_CODE,
                    ErrorCode.getErrorMsgFromErrorCode(
                            ErrorCode.INVALID_CMD_CODE));
        }
        
        if (_cmd.equalsIgnoreCase(Constants.DIR_CREATE_CMD)) {
            DirHandler.mkdir(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.DIR_DELETE_CMD)) {
            DirHandler.remove(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.DIR_LISTLONG_CMD)) {
            DirHandler.listlong(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.DIR_CAN_READ_CMD)) {
            DirHandler.canRead(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.DIR_CAN_WRITE_CMD)) {
            DirHandler.canWrite(_path, socket);
        } else if (_cmd.equalsIgnoreCase(Constants.DIR_ISDIR_CMD)) {
            DirHandler.isDir(_path, socket);
        } else if(_cmd.equalsIgnoreCase(Constants.DIR_DOESEXIST_CMD)) { 
            DirHandler.isExists(_path, socket);
        } else {
            DefaultResponse.send(socket, 
                    ErrorCode.INVALID_CMD_CODE,
                    ErrorCode.getErrorMsgFromErrorCode(
                            ErrorCode.INVALID_CMD_CODE));
        }
        
    }
}
