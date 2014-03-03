package client.request;

public class StatResponse {

    private int _errorCode;
    private commons.StatAttributes _sa;
    private String _errorMsg;
    
    public StatResponse(int errorCode, commons.StatAttributes sa) {
        _sa = sa;
        _errorCode = errorCode;
    }

    public StatResponse(int errorCode, String errorMsg) {
        _errorCode = errorCode;
        _errorMsg = errorMsg;
    }
    
    public String getErrorMsg() {
        return _errorMsg;
    }

    public int getErrorCode() {
        return _errorCode;
    }

    public commons.StatAttributes getStat() {
        return _sa;
    }            
    
    public String toString() {
        if (_errorCode != 0) {
            return null;
        }
        
        if (_sa == null) {
            return null;
        } else {
            return _sa.toString();
        }
    }
}
