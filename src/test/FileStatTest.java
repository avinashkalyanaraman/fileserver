package test;


import client.request.DefaultResponse;
import client.request.DirListResponse;
import client.request.StatResponse;
import client.FileServerClient;

public class FileStatTest {

    public static void main(String[] args) throws Exception {

        int port = 52271;
        String s_nonce = "123456781234567\n";
        byte[] nonce = s_nonce.getBytes();
        
        String path = "/Users/avinash/";
        String filepath = path + "/file1";
        //StatResponse sr = FileServerClient.stat(filepath, nonce, port);
        //FileServerClient.mkdir(path, nonce, port);
        //System.out.println(sr);
        byte[] b = new byte[1024 * 1024 * 50];
        byte[] contents = new String("avinash").getBytes();
        DefaultResponse dr = FileServerClient.write(filepath, 
                contents, 0, nonce, port);
        System.out.println(dr.getErrorCode());
        byte[] tappendcontents = new String(" is the first alphabet").getBytes();
        dr = FileServerClient.truncAppend(filepath, tappendcontents, 1,
                nonce, port);
        System.out.println(dr.getErrorCode());
        //String text = "My name is avinash";
        /*DefaultResponse retVal = FileServerClient.write(filepath, 
                text.getBytes(), 0, nonce, port);
    */
        }

}
