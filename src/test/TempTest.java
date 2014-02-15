package test;

import java.net.Socket;

public class TempTest {

    public static void main(String args[]) throws Exception {
        try {
            Socket sock = new Socket("localhosty", 7979);           
        } finally {
            String s = "aaa";
            System.out.println(s);
        }
    }
    
}
