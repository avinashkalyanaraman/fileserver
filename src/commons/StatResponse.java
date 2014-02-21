package commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.attribute.FileTime;

import utils.PathType;

public class StatResponse implements Serializable{

    private String _fileName;
    private long _lastAccessTime; //in ms since epoch
    private long _lastModifiedTime; // in ms 
    private long _size;
    private PathType _type;
    
    private static final long serialVersionUID = 1L;

    public StatResponse(String fileName, FileTime lastAccessTime,
            FileTime lastModifiedTime, long size, PathType type) {
        _fileName = fileName;
        _lastAccessTime = lastAccessTime.toMillis();
        _lastModifiedTime = lastModifiedTime.toMillis();
        _size = size;
        _type = type;
    }

    public String getFileName() {
        return _fileName;
    }

    public long getLastAccessTime() {
        return _lastAccessTime;
    }

    public long getLastModifiedTime() {
        return _lastModifiedTime;
    }

    public long getSize() {
        return _size;
    }

    public PathType getType() {
        return _type;
    }

    public byte[] serialize() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] serializedBuf = null;
        try {
            out = new ObjectOutputStream(bos);   
            out.writeObject(this);
            serializedBuf = bos.toByteArray();
            return serializedBuf;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public static StatResponse deserialize(byte[] response) {
        if (response == null) {
            return null;
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(response);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            StatResponse obj = (StatResponse)in.readObject();
            return obj;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }

    }

    
}
