package test;

import java.io.File;

public class LSLTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String path = "/Users/avinash/";
        
        File dir = new File(path);
        File[] files = dir.listFiles();
        
        for(File file: files) {
            System.out.println(file.getAbsolutePath());
        }
    }

}
