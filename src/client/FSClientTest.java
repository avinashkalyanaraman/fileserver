package client;

import client.request.DirListResponse;
import client.request.ReadResponse;
import client.request.StatResponse;


public class FSClientTest {

    public static void main(String[] args) throws Exception{
        
        int retVal;
        DirListResponse dlr;
        
        FileServerID fsid = FileServerClient.start("fred");
        int port = fsid.getPort();
        byte[] nonce = fsid.getNonce();
        
        String dir1 = "/Users/guest1/foo";
//        retVal = FileServerClient.mkdir(dir1, nonce, port);
        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();

        /*
        String dir2= dir1+ "/foo2";
        //creating a dir within foo
        FileServerClient.mkdir(dir2, nonce, port);
        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();
        
        //creating file within foo
        String text = "My name is ak3ka";
        String filepath = dir1 + "/file1";
        FileServerClient.write(filepath, 
                text.getBytes(), 0, nonce, port);
        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();
        
        //appending to that file
        String append_text = " in uva";
        FileServerClient.append(filepath, append_text.getBytes(), 
                nonce, port);
        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();
        
        //reading contents of the file
        ReadResponse rr = FileServerClient.read(filepath, 0, 200, nonce, port);
        String contents = new String(rr.getReadBuf());
        System.out.println(contents);
        
        //stat-ing the file
        StatResponse sr = FileServerClient.stat(filepath, nonce, port);
        System.out.println(sr);

        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();
        
        //removing the file
        FileServerClient.rm(filepath, nonce, port);
        
        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();
        
        //removing subdir within foo
        FileServerClient.rmdir(dir2, nonce, port);
        
        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();

        //removing directory foo
        FileServerClient.rmdir(dir1, nonce, port);
        //FileServerClient.listlong(dir1, nonce, port);
        
        dlr = FileServerClient.listlong(dir1, nonce, port);
        dlr.disp();
        */
    }

}