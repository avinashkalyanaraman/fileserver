package client.mapping;

import java.util.HashMap;
import java.util.Map;

import client.FileServerID;

public class Mapper {

    static private Map<String, FileServerID> _unameToPortMapping =
            new HashMap<String,FileServerID>();
    
    static public FileServerID getClientMapping(String uname) {
        if (uname == null) {
            return null;
        }
        
        FileServerID port = _unameToPortMapping.get(uname);
        return port;
    }
    
    static public void setClientMapping(String uname, 
            FileServerID id) {
        if (uname == null || id == null) {
            return;
        }
        
        _unameToPortMapping.put(uname, id);
    }
        
}
