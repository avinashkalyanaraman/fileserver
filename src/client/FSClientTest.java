package client;

public class FSClientTest {

    public static void main(String[] args) throws Exception{
        int nonce = 121;
        
        String dir1 = "/Users/avinash/foo";
        int retVal = FileServerClient.mkdir(dir1, nonce);
        Thread.sleep(100);
        String dir2= dir1+ "/foo2";
        FileServerClient.mkdir(dir2, nonce);
        Thread.sleep(100);
        
        String text = "My name is ak3ka";
        String filepath = dir1 + "/file1";
        FileServerClient.write(filepath, 
                text.getBytes(), 0, nonce);
        Thread.sleep(100);
        
        String append_text = " in uva";
        FileServerClient.append(filepath, append_text.getBytes(), nonce);
        Thread.sleep(100);
        
        FileServerClient.read(filepath, 0, 200, nonce);
        Thread.sleep(100);
        
        FileServerClient.rm(filepath, nonce);
        Thread.sleep(100);
        FileServerClient.rmdir(dir2, nonce);
        Thread.sleep(100);
        FileServerClient.rmdir(dir1, nonce);
        Thread.sleep(100);
        
    }

}
