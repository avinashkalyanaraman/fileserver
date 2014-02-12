package test;

import dir.DirHandler;
import file.FileHandler;

public class FileSystemTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        test();
    }

    private static void test() {
        String dirPath = "/Users/avinash/arbit/foo";
//        String dirPath = "/Users/avinash/Documents/eclipse_workspaces/"
//                + "workspace_fileserver/FileServer/src/file/FileHandler.java";
//        
        String s = "xcvb";
        byte[] b = s.getBytes();
        int retval = FileHandler.write(dirPath,b, 1, null);
        System.out.println(retval);
    }


}
