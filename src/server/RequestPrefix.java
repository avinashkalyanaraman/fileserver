package server;

import utils.PathType;

public class RequestPrefix {

    //----------------------------------------------------
    //IMPORTANT :if any field is added or type is modified! Modify getSize()!
    //----------------------------------------------------
    
    private int _nonce;
    private PathType _pathType;
    private String _cmd;
    private int _pathNamelen;
    
    public RequestPrefix(int nonce, PathType pathType, String cmd,
            int pathNamelen) {
        this._nonce = nonce;
        this._pathType = pathType;
        this._cmd = cmd;
        this._pathNamelen = pathNamelen;
    }
    
    public static int getSize() {
        //4+1+1+4
        //sizeof(nonce) + sizeof(pathType) 
        // + sizeof(cmd) +sizeof(pathNameLen)
        return 10;
    }
    
    public int getPathLength() {
        return _pathNamelen;
    }

    public int getNonce() {
        return _nonce;
    }

    public PathType getPathType() {
        return _pathType;
    }

    public String getCmd() {
        return _cmd;
    }
    
    public boolean isReadReq() {
        return (_cmd.equals(Constants.FILE_READ_CMD));
    }
    
    public boolean isWriteReq() {
        return (_cmd.equals(Constants.FILE_WRITE_CMD));
        
    }
    
    public boolean isAppendReq() {
        return (_cmd.equals(Constants.FILE_APPEND_CMD));
    }
}
