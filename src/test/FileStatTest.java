package test;


import client.request.DirListResponse;
import client.FileServerClient;

public class FileStatTest {

    public static void main(String[] args) throws Exception {

        int port = 61158;
        String s_nonce = "123456781234567\n";
        byte[] nonce = s_nonce.getBytes();
        
        String dir = "/Users/avinash/foo";
        DirListResponse dlr = FileServerClient.listlong(dir, nonce, port);
    }

}
