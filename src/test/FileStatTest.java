package test;

import client.request.StatResponse;

import client.FileServerClient;

public class FileStatTest {

    public static void main(String[] args) throws Exception {

        int port = 57188;
        String s_nonce = "123456781234567\n";
        byte[] nonce = s_nonce.getBytes();
        System.out.println(nonce.length);
        
        String file = "/Users/avinash/test";
        StatResponse sr = FileServerClient.stat(file, nonce, port);
    }

}
