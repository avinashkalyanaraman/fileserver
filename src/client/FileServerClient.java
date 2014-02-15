package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.Constants;
import client.request.AppendRequest;
import client.request.DefaultRequest;
import client.request.ReadRequest;
import client.request.WriteRequest;

public class FileServerClient {

    //XXX:Worry about return type!
    public static byte[] read(String path, long offset, 
            long numBytes, int nonce) throws UnknownHostException, IOException{

        if (path == null) {
            throw new NullPointerException("Read path cannot be null");
        }
                
        Socket clientSocket =  new Socket(
                "localhost", Constants.port);
        try{
            ReadRequest.send(clientSocket, path,
                    offset, numBytes, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }

        //return read response
        
        return null;
    }


    //Returns 0 or 1 depending on whether write was successful or not!
    public static int write(String path, byte[] wb, 
            long offset, int nonce) throws UnknownHostException, IOException{                

        if (path == null) {
            throw new NullPointerException("Write path cannot be null");
        }        

        Socket clientSocket =  new Socket(
                "localhost", Constants.port);
        
        try {
            WriteRequest.send(clientSocket, path, wb, offset, nonce);
        } catch (Exception e) {
            clientSocket.close();
            throw e;
        }
        
        //read response
        int retVal = -1;
        try {
            retVal = WriteRequest.recv(clientSocket);
        } catch(IOException ioe) {
            clientSocket.close();
            throw ioe;
        }
        return retVal;
    }

    //Returns 0 or 1 depending on whether append was successful or not!
    public static int append(String path, byte[] wb, int nonce)
            throws UnknownHostException, IOException {

        if (path == null) {
            throw new NullPointerException("Append path cannot be null");
        }        

        Socket clientSocket = new Socket("localhost", Constants.port);
        try {
            AppendRequest.send(clientSocket, path, wb, nonce);
        } catch (Exception e) {
            clientSocket.close();
            throw e;
        }
        
        //read response
        int retVal = -1;
        try {
            retVal = AppendRequest.recv(clientSocket);
        } catch(IOException ioe) {
            clientSocket.close();
            throw ioe;
        }
        return retVal;
    }

    //XXX:Worry about return type!
    public static int stat(String path, int nonce) 
            throws UnknownHostException, IOException {
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", Constants.port);
        
        try {
            DefaultRequest.send(clientSocket,
                    Constants.FILE_OPN_BYTE, Constants.FILE_DELETE_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
        
        
        //read response
        return 0;
    }

    public static int rm(String path, int nonce) 
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", Constants.port);
        
        try {
            DefaultRequest.send(clientSocket,
                    Constants.FILE_OPN_BYTE, Constants.FILE_DELETE_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
        
        
        //read response
        int retVal = -1;
        try {
            retVal = DefaultRequest.recv(clientSocket);
        } catch(IOException ioe) {
            clientSocket.close();
            throw ioe;
        }
        return retVal;        
        
    }

    public static int mkdir (String path, int nonce) 
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("Mkdir path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", Constants.port);
        try {
            DefaultRequest.send(clientSocket,
                    Constants.DIR_OPN_BYTE, Constants.DIR_CREATE_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
       
        //read response
        int retVal = -1;
        try {
            retVal = DefaultRequest.recv(clientSocket);
        } catch(IOException ioe) {
            clientSocket.close();
            throw ioe;
        }
        return retVal;
    }

    public static int rmdir(String path, int nonce) 
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("Rmdir path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", Constants.port);
        try {
            DefaultRequest.send(clientSocket,
                    Constants.DIR_OPN_BYTE, Constants.DIR_DELETE_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
        
        //read response
        int retVal = -1;
        try {
            retVal = DefaultRequest.recv(clientSocket);
        } catch(IOException ioe) {
            clientSocket.close();
            throw ioe;
        }
        return retVal;
    }

    //XXX: Worry about return type
    public static int list(String path, int nonce) 
            throws UnknownHostException, IOException{
        
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", Constants.port);
        
        try {
            DefaultRequest.send(clientSocket,
                    Constants.FILE_OPN_BYTE, Constants.FILE_DELETE_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
        
        //read response!
        return 0;
    }

    //XXX: Worry about return type
    public static int listlong(String path, int nonce)
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", Constants.port);
        
        try {
            DefaultRequest.send(clientSocket,
                    Constants.FILE_OPN_BYTE, Constants.FILE_DELETE_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
        
        //read response!
        return 0;
    }

}