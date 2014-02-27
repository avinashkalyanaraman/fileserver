package client.request;

import commons.DirListing;
import commons.StatAttributes;

public class DirListResponse {

    private int _errorCode;
    private DirListing _listing;
    
    public DirListResponse(int errorCode, DirListing listing) {
        _errorCode = errorCode;
        _listing = listing;
    }
    
    public void disp() {
        if (_listing == null || _errorCode != 0) {
            System.out.println("----------------------");
            System.out.println("----------------------");
            return;
        }
        
        System.out.println("----------------------");
        for(StatAttributes sa : _listing.getDirListing()) {
            System.out.println(sa);
        }
        System.out.println("----------------------");
    }
}
