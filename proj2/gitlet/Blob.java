package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {

    public static final File BLOBS = Utils.join(Repository.OBJECTS_DIR, "blobs");

    private String blobId; //sha-1 hash code for this blob

    private byte[] fileContents;


    /**Constructor for blob using the file name. */
    public Blob(String fileName) {
        File f = Utils.join(Repository.CWD, fileName);
        fileContents = Utils.readContents(f);
        blobId = calculateHashCode();
    }

    public Blob(byte[] contents) {
        this.fileContents = contents;
        this.blobId = calculateHashCode();
    }

    /**Calculate the sha-1 hash code foe the file contents. */
    private String calculateHashCode() {
        return Utils.sha1((Object) fileContents);
    }

    /**Returns the hash code. */
    public String getBlobId() {
        return this.blobId;
    }

    /**Saves file contents into file using the hash code as its file name. */
    public void save() {
        File blob = Utils.join(BLOBS, blobId);
        Utils.writeContents(blob, fileContents);
    }

}
