package client.utils;

import java.util.HashMap;
import java.util.Map;

public class Mapper {

    static private Map<String, Integer> _unameToPortMapping =
            new HashMap<String,Integer>();
    
    static public int getClientPort(String uname) {
        if (uname == null) {
            return -1;
        }
        
        Integer port = _unameToPortMapping.get(uname);
        if (port == null) {
            return -1;
        }
        
        return port;
    }
    
    static public void setClientPort(String uname, int port) {
        if (uname == null || port < 0) {
            return;
        }
        
        _unameToPortMapping.put(uname, port);
    }
        
}
