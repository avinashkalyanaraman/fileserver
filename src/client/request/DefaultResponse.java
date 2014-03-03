package client.request;

public class DefaultResponse {

    private int _errorCode;
    private String _errorMsg;
    
    public DefaultResponse(int ec, String msg) {
        _errorCode = ec;
        _errorMsg = msg;
    }
    
    public DefaultResponse(int ec, byte[] msgBuf) {
        _errorCode = ec;
        _errorMsg = new String(msgBuf);
    }

    public DefaultResponse(int ec) {
        _errorCode = ec;
        _errorMsg = null;
    }

    public int getErrorCode() {
        return _errorCode;
    }

    public String getErrorMsg() {
        return _errorMsg;
    }
    
}
