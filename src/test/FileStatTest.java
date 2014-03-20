package test;


import client.request.DefaultResponse;
import client.request.DirListResponse;
import client.request.StatResponse;
import client.FileServerClient;

public class FileStatTest {

    public static void main(String[] args) throws Exception {

        int port = 52892;
        String s_nonce = "123456781234567\n";
        byte[] nonce = s_nonce.getBytes();
        
        String path = "/Users/avinash/";
        String filepath = path + "/file2";
        /*byte[] contents = new String("avinash").getBytes();
        DefaultResponse dr = FileServerClient.write(filepath, 
                contents, 0, nonce, port);
        System.out.println(dr.getErrorCode());
        byte[] tappendcontents = new String(" is the first alphabet").getBytes();
        dr = FileServerClient.truncAppend(filepath, tappendcontents, 1,
                nonce, port);
        System.out.println(dr.getErrorCode());
        */
        DefaultResponse dr = FileServerClient.creat(filepath, nonce, port);
        System.out.println(dr.getErrorCode());
        
    }

}
