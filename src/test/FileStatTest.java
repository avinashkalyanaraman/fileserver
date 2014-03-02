package test;


import client.request.DirListResponse;
import client.request.StatResponse;
import client.FileServerClient;

public class FileStatTest {

    public static void main(String[] args) throws Exception {

        int port = 61158;
        String s_nonce = "123456781234567\n";
        byte[] nonce = s_nonce.getBytes();
        
        String path = "/Users/guest1/foo/file1";
        StatResponse sr = FileServerClient.stat(path, nonce, port);
        System.out.println(sr);
    }

}
