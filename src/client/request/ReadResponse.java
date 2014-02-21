package client.request;

public class ReadResponse {

    int error_code;
    byte[] read_buff;
    
    public ReadResponse(int error_code, byte[] read_buff) {
        this.error_code = error_code;
        this.read_buff = read_buff;
    }
    
    public int getErrorCode() {
        return error_code;
    }
    
    public byte[] getReadBuf() {
        return read_buff;
    }
}
