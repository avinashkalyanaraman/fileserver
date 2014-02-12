package test;

public class TempTest {

    public static void main(String args[]) {
        int[] a= {2,3,4};
        change_a(a);
        System.out.println(a[0] +"," + a[1] + "," + a[2]);
    }
    
    static void change_a(int[] a) {
        for(int ii=0; ii<a.length; ii++) {
            a[ii]++;
        }
    }
}
