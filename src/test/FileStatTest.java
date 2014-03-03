package test;


import client.request.DirListResponse;
import client.request.StatResponse;
import client.FileServerClient;

public class FileStatTest {

    public static void main(String[] args) throws Exception {

        int port = 62103;
        String s_nonce = "123456781234567\n";
        byte[] nonce = s_nonce.getBytes();
        
        String path = "/Users/avinash/foo";
        //StatResponse sr = FileServerClient.stat(path, nonce, port);
        FileServerClient.mkdir(path, nonce, port);
        //System.out.println(sr);
    }

}
