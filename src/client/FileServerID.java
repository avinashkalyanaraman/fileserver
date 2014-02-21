package client;

import server.Constants;

public class FileServerID {

    private byte[] _nonce = new byte[Constants.NONCE_SIZE];
    private int _port;
    
    public FileServerID(byte[] nonce, int port) {
        System.arraycopy(nonce, 0, _nonce, 0, nonce.length);
        _port = port; 
    }
    
    public byte[] getNonce () {
        return _nonce;
    }
    
    public int getPort() {
        return _port;
    }
}
