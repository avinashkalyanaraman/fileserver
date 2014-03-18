package client;

import commons.Constants;

public class FileServerID {

    private byte[] _nonce = new byte[Constants.NONCE_SIZE];
    private int _port;
    private Process _fsProcess;
    
    public FileServerID(byte[] nonce, int port, Process p) {
        System.arraycopy(nonce, 0, _nonce, 0, nonce.length);
        _port = port; 
        _fsProcess = p;
    }
    
    public byte[] getNonce () {
        return _nonce;
    }
    
    public int getPort() {
        return _port;
    }
    
    public Process getFSProcess() {
        return _fsProcess;
    }
}
