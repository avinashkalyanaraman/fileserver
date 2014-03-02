package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import commons.Constants;
import client.mapping.Mapper;
import client.request.AppendRequest;
import client.request.DefaultRequest;
import client.request.DirListRequest;
import client.request.DirListResponse;
import client.request.ReadRequest;
import client.request.ReadResponse;
import client.request.StatRequest;
import client.request.StatResponse;
import client.request.WriteRequest;

public class FileServerClient {

    public static ReadResponse read(String path, long offset, 
            long numBytes, byte[] nonce, int port) 
                    throws UnknownHostException, IOException{

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
        ReadResponse rr = null;
        try {
            rr = ReadRequest.recv(clientSocket, (int)numBytes);
        } catch (Exception e) {
            clientSocket.close();
            throw e;
        }
        
        return rr;
    }


    //Returns 0 or non-0 depending on whether write was successful or not!
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

    //Returns 0 or non-0 depending on whether append was successful or not!
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

    public static StatResponse stat(String path, byte[] nonce, int port) 
            throws UnknownHostException, IOException {
        if (path == null) {
            throw new NullPointerException("delete path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
        
        StatResponse sr = null;
        try {
            StatRequest.send(clientSocket,
                    Constants.FILE_OPN_BYTE, Constants.FILE_STAT_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
        
        
        try {
            sr = StatRequest.recv(clientSocket);
        } catch (Exception e) {
            clientSocket.close();
            throw e;
        }
        
        return sr;
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

    public static DirListResponse listlong(String path, 
            byte[] nonce, int port)
            throws UnknownHostException, IOException {
        
        if (path == null) {
            throw new NullPointerException("list path cannot be null");
        }        
        
        Socket clientSocket = new Socket("localhost", port);
        
        try {
            DirListRequest.send(clientSocket,
                    Constants.DIR_OPN_BYTE, Constants.DIR_LISTLONG_CMD_BYTE,
                    path, nonce);
        } catch(Exception e) {
            clientSocket.close();
            throw e;
        }
        
        //read response!
        DirListResponse dlr = null;
        try {
            dlr = DirListRequest.recv(clientSocket);
        } catch (Exception e) {
            clientSocket.close();
            throw e;
        }
        
        return dlr;
    }

    //Invoked to start the file server!
    //returns the <nonce,port> where the server was started!
    public static synchronized FileServerID start(String uname) {
        
        //To Handle race!
        FileServerID fsid = Mapper.getClientMapping(uname);
        
        //Has someone else already created a mapping?
        if (fsid != null) {
            return fsid;
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
        
        int port = 0;
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
        
        fsid = new FileServerID(nonce, port);
        Mapper.setClientMapping(uname, fsid);
        return fsid;
    }
}