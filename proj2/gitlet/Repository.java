package gitlet;

import java.io.File;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *  gitlet repo structure
 *      *   .gitlet
 *      *      |--objects
 *      *      |     |--commits
 *      *      |     |--blobs
 *      *      |--branches
 *      *      |     |--master
 *      *      |     |--other heads
 *      *      |--HEAD
 *      *      |--stage
 *
 *  @author Zechuan Wang
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**The objects directory. */
    public static final File OBJECTS_DIR = Utils.join(GITLET_DIR, "objects");
    /* TODO: fill in the rest of this class. */
    public static void init(){
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        Commit.COMMITS_DIR.mkdir();
        Blob.BLOBS.mkdir();
        Branch.BRANCHES.mkdir();
        Commit initialCommit = new Commit();
        String commitID = initialCommit.getCommitID();
        initialCommit.save();

        StagingArea stage = new StagingArea();
        stage.save();
        Branch.setCommitID("master", commitID);
        HEAD.setHead("master");

    }

    public static void add(String fileName) {
        if (!Utils.join(CWD, fileName).exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob blob = new Blob(fileName);
        String blobID =  blob.getBlobId();
        StagingArea stagingArea = StagingArea.load();
        Map<String, String> addition = stagingArea.getAddition();
        if (addition.containsKey(fileName)) { // staging an already-staged file
            addition.replace(fileName, blobID);
        } else {
            addition.put(fileName, blobID);
        }
        Commit curCommit = Commit.load(Branch.getCommitID(HEAD.getHead()));
        Map<String, String> commitBlobs  = curCommit.getFileNameToBlobID();
        if (blobID.equals(commitBlobs.get(fileName))) {
            addition.remove(blobID);
        }
        blob.save();
        stagingArea.save();
    }
}
