package client.request;

public class StatResponse {

    private int _errorCode;
    private commons.StatResponse _sr;
    
    public StatResponse(int errorCode, commons.StatResponse sr) {
        _sr = sr;
        _errorCode = errorCode;
    }

    public int getErrorCode() {
        return _errorCode;
    }

    public commons.StatResponse getStat() {
        return _sr;
    }            
    
    public String toString() {
        if (_errorCode != 0) {
            return null;
        }
        
        if (_sr == null) {
            return null;
        } else {
            return _sr.toString();
        }
    }
}
