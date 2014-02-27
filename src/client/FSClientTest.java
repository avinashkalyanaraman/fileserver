package client;

import client.request.ReadResponse;
import client.request.StatResponse;


public class FSClientTest {

    public static void main(String[] args) throws Exception{
        
        FileServerID fsid = FileServerClient.start("fred");
        int port = fsid.getPort();
        byte[] nonce = fsid.getNonce();
        
        String dir1 = "/Users/avinash/foo";
        int retVal = FileServerClient.mkdir(dir1, nonce, port);
        String dir2= dir1+ "/foo2";
        FileServerClient.mkdir(dir2, nonce, port);
        
        String text = "My name is ak3ka";
        String filepath = dir1 + "/file1";
        FileServerClient.write(filepath, 
                text.getBytes(), 0, nonce, port);
        
        String append_text = " in uva";
        FileServerClient.append(filepath, append_text.getBytes(), 
                nonce, port);
        
        ReadResponse rr =FileServerClient.read(filepath, 0, 200, nonce, port);
        String contents = new String(rr.getReadBuf());
        System.out.println(contents);
        
        StatResponse sr = FileServerClient.stat(filepath, nonce, port);
        System.out.println(sr);
        
        FileServerClient.rm(filepath, nonce, port);
        FileServerClient.rmdir(dir2, nonce, port);
        FileServerClient.rmdir(dir1, nonce, port);
        
    }

}
