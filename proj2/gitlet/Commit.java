package gitlet;



import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  The Commit class handles the serialization of Commit objects. It also deals with conversion between commit ids and commit objects.
 *  Each Commit records mappings of held file names and their corresponding file content.
 *  Specifically, it fulfil the following purposes:
 * 1.Constructs Commit objects;
 * 2.Serializes and saves Commit objects to the .gitlet/commits directory;
 * 3.Given a commit id, retrieves the corresponding Commit object.
 *  @author Zechuan Wang
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    public static final File COMMITS_DIR = Utils.join(Repository.OBJECTS_DIR, "commits");

    /** The message of this Commit. */
    private String commitMessage; // commit message
    private Date curTime;

    private String timeStamp; // time when the commit is done

    private String firstParentID; // first parent commit id
    private String secondParentID; // second commit parent id

    private String commitID;
    private Map<String, String> fileNameToBlobID; // give the map from file name to blob id


    /* TODO: fill in the rest of this class. */
    public Commit() {
        this.commitMessage = "initial commit";
        this.curTime = new Date(0);
        this.timeStamp = Commit.dateToTimeStamp(curTime);
        firstParentID = "";
        secondParentID = "";
        fileNameToBlobID = new TreeMap<>();
        commitID = generateCommitID();

    }

    /**Returns commit message. */
    public String getCommitMessage() {
        return this.commitMessage;
    }

    /**Converts Date to standard time stamp. */
    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    /**Returns time stamp. */
    public String getTimeStamp() {
        return this.timeStamp;
    }

    /**Generates Commit ID. */
    private String generateCommitID() {
        return Utils.sha1(commitMessage, timeStamp, firstParentID, secondParentID, fileNameToBlobID.toString());
    }
    /**Get commit id. */
    public String getCommitID() {
        return commitID;
    }
    /**Returns the first parent commit id. */
    public String getFirstParentID() {
        return this.firstParentID;
    }

    /**Returns the second parent commit id. */
    public String getSecondParent() {
        return this.secondParentID;
    }

    /**Writes commit object into file. */
    public void save() {
        File commit = Utils.join(COMMITS_DIR, this.getCommitID());
        Utils.writeObject(commit, this);
    }

    /**Loads the Commit Object from the commits directory with its commit id. */
    public static Commit load(String commitID) {
       if (commitID.length() < 40) { // for the situation that only contains the prefix of the commit id.
           assert commitID != null;
           List<String> allCimmits = Utils.plainFilenamesIn(COMMITS_DIR);
           for (String s : allCimmits) {
               if (s.startsWith(commitID)) {
                   commitID = s;
                   break;
               }
           }
       }
       File commit = Utils.join(COMMITS_DIR, commitID);
       if (!commit.exists()) {
           return null;
       } else {
           return Utils.readObject(commit, Commit.class);
       }
    }

    /**Returns the file to blob id mapping. */
    public Map<String, String> getFileNameToBlobID() {
        return this.fileNameToBlobID;
    }


    public static void main(String[] args){
        Commit tmp = new Commit();
        System.out.println(tmp.curTime);

        System.out.println(Commit.dateToTimeStamp(tmp.curTime));

        System.out.println(tmp.getCommitID());
    }
}
