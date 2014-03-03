package test;


import client.request.DefaultResponse;
import client.request.DirListResponse;
import client.request.StatResponse;
import client.FileServerClient;

public class FileStatTest {

    public static void main(String[] args) throws Exception {

        int port = 63192;
        String s_nonce = "123456781234567\n";
        byte[] nonce = s_nonce.getBytes();
        
        String path = "/Users/guest1/foo";
        String filepath = path + "/file1";
        //StatResponse sr = FileServerClient.stat(path, nonce, port);
        //FileServerClient.mkdir(path, nonce, port);
        //System.out.println(sr);
        String text = "My name is avinash";
        DefaultResponse retVal = FileServerClient.write(filepath, 
                text.getBytes(), 0, nonce, port);
    }

}
