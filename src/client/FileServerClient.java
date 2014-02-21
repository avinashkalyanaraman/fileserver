package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.Constants;
import client.request.AppendRequest;
import client.request.DefaultRequest;
import client.request.ReadRequest;
import client.request.WriteRequest;
import client.utils.Mapper;

public class FileServerClient {

    //XXX:Worry about return type!
    public static byte[] read(String path, long offset, 
            long numBytes, byte[] nonce, int port) throws UnknownHostException, IOException{

        if (path == null) {
            throw new NullPointerException("Read path cannot be null");
        }
                
        Socket clientSocket =  new Socket(
                "localhost", port);
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
            long offset, byte[] nonce, int port) throws UnknownHostException, 
            IOException{                

        if (path == null) {
            throw new NullPointerException("Write path cannot be null");
        }        

        Socket clientSocket =  new Socket(
                "localhost", port);
        
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
    public static int append(String path, byte[] wb, byte[] nonce, int port)
            throws UnknownHostException, IOException {

        if (path == null) {
            throw new NullPointerException("Append path cannot be null");
        }        

        Socket clientSocket = new Socket("localhost", port);
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
    public static int stat(String path, byte[] nonce, int port) 
            throws UnknownHostException, IOException {
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
        
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

    public static int rm(String path, byte[] nonce, int port) 
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
        
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

    public static int mkdir (String path, byte[] nonce, int port) 
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("Mkdir path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
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

    public static int rmdir(String path, byte[] nonce, int port) 
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("Rmdir path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
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
    public static int list(String path, byte[] nonce, int port) 
            throws UnknownHostException, IOException{
        
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
        
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
    public static int listlong(String path, byte[] nonce, int port)
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
        
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

    //Invoked to start the file server!
    //returns the <nonce,port> where the server was started!
    public static synchronized FileServerID start(String uname) {
        
        //To Handle race!
        int port = Mapper.getClientPort(uname);
        if (port != -1) {
            return null;
        }
        
        List<String> cmd = new ArrayList<String>();
        cmd.add("java");
        cmd.add("server/Driver");
        Process p = null;
        ProcessBuilder pb = new ProcessBuilder(cmd);
        File workingDir = new File( "/Users/avinash/Documents/eclipse_workspaces/workspace_fileserver/FileServer/bin");
        pb.directory(workingDir);
        
        byte[] nonce = new byte[Constants.NONCE_SIZE];
        
        try {
            pb.redirectOutput(Redirect.PIPE);
            
            p = pb.start();
            //p = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            return null;
        }
        
        BufferedReader stdout = new BufferedReader(new 
                InputStreamReader(p.getInputStream()));
        
        BufferedOutputStream stdin = new BufferedOutputStream(
                p.getOutputStream());
        
        try {
            new Random().nextBytes(nonce);
            stdin.write(nonce);
            stdin.flush();
        } catch (Exception ioe) {
            //kill the bad process we created!
            p.destroy();
            return null;
        }                
        
        try {
            String line = stdout.readLine();
            port = Integer.parseInt(line);
            
            if (port == 0) {
                p.destroy();
                return null;
            }
        } catch (Exception e) {
            p.destroy();
            return null;
        }
        
        FileServerID fsid = new FileServerID(nonce, port);
        return fsid;
    }
}