package gitlet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        Commit curCommit = Commit.load(Branch.getCommitID(HEAD.getHead()));
        //If the current working version of the file is identical to the version in the current commit,
        // do not stage it to be added, and remove it from the staging area
        if (blobID.equals(curCommit.getBlobID(fileName))) {
            stagingArea.clear();
            stagingArea.save();
            return;
        }
        if (addition.containsKey(fileName)) { // staging an already-staged file overwrites the previous one
            addition.replace(fileName, blobID);
        } else {
            addition.put(fileName, blobID);
        }
        stagingArea.getRemoval().remove(fileName);
        blob.save();
        stagingArea.save();
    }
    private static void commitWithMergeBranch(String message, String curCommitID, String mergeBranchCommitID) {
        if (message.isBlank()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        StagingArea stagingArea = StagingArea.load();
        if (stagingArea.getAddition().isEmpty()) {
            System.out.println("No changes added to the commit.");
            stagingArea.save();
            return;
        }
        Commit newCommit = new Commit(message, curCommitID, mergeBranchCommitID);
        for (Map.Entry<String, String> entry : stagingArea.getAddition().entrySet()) {
            String fileName = entry.getKey();
            String blobID = entry.getValue();
            newCommit.getFileNameToBlobIDMap().put(fileName, blobID);
        }
        for (Map.Entry<String, String> entry : stagingArea.getRemoval().entrySet()) {
            String fileName  = entry.getKey();
            newCommit.getFileNameToBlobIDMap().remove(fileName);
        }
        Branch.setCommitID(HEAD.getHead(), newCommit.getCommitID());
        stagingArea.clear();
        stagingArea.save();
        newCommit.save();
    }

    public static void commit(String message) {
        String curCommitID = Branch.getCommitID(HEAD.getHead());
        commitWithMergeBranch(message, curCommitID, "");
    }

    public static void rm(String fileName) {
        StagingArea stagingArea = StagingArea.load();
        Commit curCommit = Commit.load(Branch.getCommitID(HEAD.getHead()));
        if (!stagingArea.getAddition().containsKey(fileName) && !curCommit.getFileNameToBlobIDMap().containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        if (stagingArea.getAddition().containsKey(fileName)) {
            stagingArea.getAddition().remove(fileName);
            stagingArea.save();
            return;
        }
        if (curCommit.getFileNameToBlobIDMap().containsKey(fileName) && Utils.join(CWD, fileName).exists()) {
            stagingArea.getRemoval().put(fileName, curCommit.getBlobID(fileName));
            Utils.join(CWD, fileName).delete();
            stagingArea.save();
            return;
        }

    }

    public static void log() {
        String commit = Commit.load(Branch.getCommitID(HEAD.getHead())).getCommitID();
        while (!commit.isBlank()) {
            Commit tmp = Commit.load(commit);
            System.out.println(tmp.toString());
            commit = tmp.getFirstParentID();
        }

    }

    public static void global_log() {
        List<String> commits = Utils.plainFilenamesIn(Commit.COMMITS_DIR);
        for (String commit : commits) {
            Commit tmp = Commit.load(commit);
            System.out.println(tmp.toString());
        }
    }

    public static void find(String message) {
        List<String> commits = Utils.plainFilenamesIn(Commit.COMMITS_DIR);
        StringBuilder builder = new StringBuilder();
        for (String commit : commits) {
            Commit tmp = Commit.load(commit);
            if (tmp.getCommitMessage().equals(message)) {
                builder.append(commit).append("\n");
            }
        }
        String res = builder.toString();
        if (res.isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        } else {
            System.out.println(res);
        }
    }
    public static void status() {
        System.out.println("=== Branches ===");
        //Prints out the branches
        List<String> branches = Utils.plainFilenamesIn(Branch.BRANCHES);
        Collections.sort(branches);
        System.out.println("*" + HEAD.getHead());
        for (String branch : branches) {
            if (!branch.equals(HEAD.getHead())) {
                System.out.println(branch);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        StagingArea stagingArea = StagingArea.load();
        List<String> stagedFiles = new ArrayList<>();
        stagedFiles.addAll(stagingArea.getAddition().keySet());
        Collections.sort(stagedFiles);
        printString(stagedFiles);
        System.out.println("=== Removed Files ===");
        List<String> removedFiles = new ArrayList<>();
        removedFiles.addAll(stagingArea.getRemoval().keySet());
        Collections.sort(removedFiles);
        printString(removedFiles);
        System.out.println("=== Modifications Not Staged For Commit ===");
        Commit curCommit = Commit.load(Objects.requireNonNull(Branch.getCommitID(HEAD.getHead())));
        List<String> curWorkingDirFiles = Utils.plainFilenamesIn(CWD);
        assert  curWorkingDirFiles != null;
        List<String> modifiedNotStagedForCommit = modifiedNotStagedForCommit(stagingArea, curCommit, curWorkingDirFiles);
        Collections.sort(modifiedNotStagedForCommit);
        printString(modifiedNotStagedForCommit);
        System.out.println("=== Untracked Files ===");
        List<String> unTrackedFiles = unTrackedFiles(stagingArea,curCommit,curWorkingDirFiles);
        printString(unTrackedFiles);
    }

    /**Helper function for printing the array. */
    private static void printString(List<String> strings) {
        for (String string : strings) {
            System.out.println(string);
        }
        System.out.println();
    }
    private static List<String> modifiedNotStagedForCommit(StagingArea stagingArea, Commit curCommit, List<String> curWorkingDirFiles) {
        List<String> result = new ArrayList<>();
        List<String> commitTrackedFiles = new ArrayList<>();
        commitTrackedFiles.addAll(curCommit.getFileNameToBlobIDMap().keySet());
        for (String fileName : curWorkingDirFiles) {
            Blob blob = new Blob(fileName);
            boolean tracked = curCommit.getFileNameToBlobIDMap().containsKey(fileName);
            boolean changed = !blob.getBlobId().equals(curCommit.getBlobID(fileName)); // blob id is not null but the curCommit's blob id may be null.
            boolean staged = stagingArea.getAddition().containsKey(fileName);
            //Situation 1 : Tracked in the current commit, changed in the working directory, but not staged;
            if (tracked && changed && !staged) {
                result.add(fileName + " (modified)");
                continue;
            }

            changed = !blob.getBlobId().equals(stagingArea.getAddition().get(fileName));
            //Situation 2 : Staged for addition, but with different contents than in the working directory;
            if (staged && changed) {
                result.add(fileName + " (modified)");
            }
        }
        for (String fileName : stagingArea.getAddition().keySet()) {
            //Situation 3 : Staged for addition, but deleted in the working directory
            if (!curWorkingDirFiles.contains(fileName)) {
                result.add(fileName + " (deleted)");
            }
        }
        for (String fileName : curCommit.getFileNameToBlobIDMap().keySet()) {
            //Situation 4 : Not staged for removal, but tracked in the current commit and deleted from the working directory.
            boolean staged = stagingArea.getRemoval().containsKey(fileName);
            boolean deleted = !curWorkingDirFiles.contains(fileName);

            if (!staged && deleted) {
                result.add(fileName + " (deleted)");
            }
        }
        Collections.sort(result);
        return result;
    }
    private static List<String> unTrackedFiles(StagingArea stagingArea, Commit curCommit, List<String> curWorkingDirFiles) {
        List<String> result = new ArrayList<>();
        for (String fileName : curWorkingDirFiles) {
            boolean tracked = curCommit.getFileNameToBlobIDMap().containsKey(fileName);
            boolean staged = stagingArea.getAddition().containsKey(fileName);
            if (!tracked && !staged) {
                result.add(fileName);
            }
        }
        Collections.sort(result);
        return result;
    }

    public static void checkout(String[] args) {
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            checkoutFile(Branch.getCommitID(HEAD.getHead()), args[2]);
            return;
        }
        if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            checkoutFile(args[1], args[3]);
            return;
        }
        if (args.length == 2) {
            checkoutBranch(args[1]);
            return;
        }
        System.out.println("Incorrect operands.");
        System.exit(0);
    }

    /**Takes the version of the file as it exists in the head commit and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.*/
    private static void checkoutFile(String commitID, String fileName) {
        Commit curCommit = Commit.load(commitID);
        if (curCommit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!curCommit.getFileNameToBlobIDMap().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File blobFile = Utils.join(Blob.BLOBS, curCommit.getBlobID(fileName));
        byte[] contents = Utils.readContents(blobFile);
        Utils.writeContents(Utils.join(CWD, fileName), (Object) contents);
    }

    /**Takes all files in the commit at the head of the given branch, and puts them in the working directory,
    // overwriting the versions of the files that are already there if they exist. Also, at the end of this command,
    // the given branch will now be considered the current branch (HEAD).
    // Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
    // The staging area is cleared, unless the checked-out branch is the current branch
     */
    private static void checkoutBranch(String branchName) {
        if (!Utils.join(Branch.BRANCHES, branchName).exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (branchName.equals(HEAD.getHead())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        Commit commit = Commit.load(Branch.getCommitID(branchName)); // branch
        checkoutCommit(commit);
        HEAD.setHead(branchName);
    }
    /**Helper function
     * If a working file is untracked in the current branch and would be overwritten by the checkout,
     * print error message and exit. */
    private static void checkoutCommit(Commit commit) {
        StagingArea stagingArea = StagingArea.load();
        String curCommitID = Branch.getCommitID(HEAD.getHead());
        assert curCommitID != null;
        Commit curCommit = Commit.load(curCommitID);
        assert curCommit != null;
        List<String> curWorkingDirFiles = Utils.plainFilenamesIn(CWD);
        assert curWorkingDirFiles != null;
        List<String> unTrackedFiles = unTrackedFiles(stagingArea, curCommit, curWorkingDirFiles);
        List<String> branchCommitTrackedFiles = new ArrayList<>();
        branchCommitTrackedFiles.addAll(commit.getFileNameToBlobIDMap().keySet());
        if (!unTrackedFiles.isEmpty()) {
            for (String unTrackedFile : unTrackedFiles) {
                if (branchCommitTrackedFiles.contains(unTrackedFile)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        for (String fileName : branchCommitTrackedFiles) {
            String blobID = commit.getBlobID(fileName);
            byte[] contents = Utils.readContents(Utils.join(Blob.BLOBS, blobID));
            Utils.writeContents(Utils.join(CWD, fileName), (Object) contents);
        }

        for (String fileName : curCommit.getFileNameToBlobIDMap().keySet()) {
            if (!branchCommitTrackedFiles.contains(fileName)) {
                Utils.join(CWD, fileName).delete();
            }
        }
        stagingArea.clear();
        stagingArea.save();

    }

    public static void branch(String branchName) {
        if (Utils.join(Branch.BRANCHES, branchName).exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        String commitID = Branch.getCommitID(HEAD.getHead());
        Branch.setCommitID(branchName, commitID);
    }

    public static void rmBranch(String branchName) {
        File branchFile = Utils.join(Branch.BRANCHES, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(HEAD.getHead())) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branchFile.delete();
    }

    public static void reset(String commitID) {
        Commit commit = Commit.load(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        checkoutCommit(commit);
        Branch.setCommitID(HEAD.getHead(), commit.getCommitID());
    }

    public static void merge(String branchName) {
        StagingArea stagingArea = StagingArea.load();
        if (!stagingArea.getAddition().isEmpty() || !stagingArea.getRemoval().isEmpty()) { //failure case 1
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        String mergeCommitID = Branch.getCommitID(branchName);
        if (mergeCommitID == null) { //failure case 2
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (HEAD.getHead().equals(branchName)) { //failure case 3
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Commit mergeCommit = Commit.load(mergeCommitID);
        assert mergeCommit != null;
        String curCommitID = Branch.getCommitID(HEAD.getHead());
        assert curCommitID != null;
        Commit curCommit = Commit.load(curCommitID);
        assert curCommit != null;
        List<String> curWorkingDirFiles = Utils.plainFilenamesIn(CWD);
        assert curWorkingDirFiles != null;
        List<String> unTrackedFiles = unTrackedFiles(stagingArea, curCommit, curWorkingDirFiles);
        if (!unTrackedFiles.isEmpty()) {
            for (String unTrackedFile : unTrackedFiles) {
                if (mergeCommit.getFileNameToBlobIDMap().containsKey(unTrackedFile)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        String splitPointCommitID = getSplitPointCommitID(curCommitID, mergeCommitID);
        if (mergeCommitID.equals(splitPointCommitID)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (curCommitID.equals(splitPointCommitID)) {
            checkoutCommit(mergeCommit);
            Branch.setCommitID(HEAD.getHead(), mergeCommitID);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        Commit splitPointCommit = Commit.load(splitPointCommitID);
        assert splitPointCommit != null;

    }
    private static String getSplitPointCommitID(String curCommitID, String mergeCommitID) {
        Set<String> commitSet = new HashSet<>();
        Queue<String> que = new ArrayDeque<>();
        que.add(curCommitID);
        while (!que.isEmpty()) {
            String commitID = que.remove();
            Commit commit = Commit.load(commitID);
            if (commit.getFirstParentID() != null) {
                que.add(commit.getFirstParentID());
            }
            if (commit.getSecondParentID() != null) {
                que.add(commit.getSecondParentID());
            }
        }
        que.add(mergeCommitID);
        while (!que.isEmpty()) {
            String commitID = que.remove();
            Commit commit = Commit.load(commitID);
            if (commitSet.contains(commitID)) {
                return commitID;
            }
            if (commit.getFirstParentID() != null) {
                que.add(commit.getFirstParentID());
            }
            if (commit.getSecondParentID() != null) {
                que.add(commit.getSecondParentID());
            }
        }
        return null;
    }
    private static boolean processMerge(StagingArea stagingArea, Commit splitPointCommit, Commit curCommit, Commit mergeCommit) {
        boolean conflict = false;
        Map<String, String> splitBlobs = splitPointCommit.getFileNameToBlobIDMap();
        Map<String, String> curBlobs = curCommit.getFileNameToBlobIDMap();
        Map<String, String> mergeBlobs = mergeCommit.getFileNameToBlobIDMap();
        for (String fileName : mergeBlobs.keySet()) {
            String mergeBlobID = mergeBlobs.get(fileName);
            String splitBlobID = splitBlobs.get(fileName);
            String curBlobID = curBlobs.get(fileName);
            //Situation 1 : Any files that have been modified in the given branch since the split point,
            // but not modified in the current branch since the split point should be changed to their versions in the given branch
            if (splitBlobID != null && !mergeBlobID.equals(splitBlobID)) {
                if (splitBlobID.equals(curBlobID)) {
                    checkoutFile(mergeCommit.getCommitID(), fileName);
                    stagingArea.getAddition().put(fileName, mergeBlobID);
                    continue;
                }
            }
            //Situation 5 : Any files that were not present at the split point and
            //are present only in the given branch should be checked out and staged.
            if (splitBlobID == null && curBlobID == null) {
                checkoutFile(mergeCommit.getCommitID(), fileName);
                stagingArea.getAddition().put(fileName, mergeBlobID);
                continue;
            }
            //Situation 8 :the contents of one are changed and the other file is deleted
            if (splitBlobID != null && !mergeBlobID.equals(splitBlobID) && curBlobID == null) {
                conflict = true;
                processConflict(stagingArea, fileName, curBlobID, mergeBlobID);
            }

        }
        for (String fileName : curBlobs.keySet()) {
            String curBlobID = curBlobs.get(fileName);
            String splitBlobID = splitBlobs.get(fileName);
            String mergeBlobID = mergeBlobs.get(fileName);
            //Situation 6 :Any files present at the split point, unmodified in the current branch,
            // and absent in the given branch should be removed
            if (curBlobID.equals(splitBlobID)) {
                if (mergeBlobID == null) {
                    Utils.join(CWD, fileName).delete();
                    stagingArea.getRemoval().put(fileName, curBlobID);
                    continue;
                }
            }
            //Situation 8 : he contents of both are changed and different from other
            if (splitBlobID != null && mergeBlobID != null) {
                if (!curBlobID.equals(splitBlobID) && !mergeBlobID.equals(splitBlobID)) {
                    if (!curBlobID.equals(mergeBlobID)) {
                        conflict = true;
                        processConflict(stagingArea, fileName, curBlobID, mergeBlobID);
                    }
                }
            }
            //Situation 8 :the contents of one are changed and the other file is deleted
            if (splitBlobID != null && !curBlobID.equals(splitBlobID) && mergeBlobID == null) {
                conflict = true;
                processConflict(stagingArea, fileName, curBlobID, mergeBlobID);
            }
            //Situation 8 : the file was absent at the split point and has different contents in the given and current branches
            if (splitBlobID == null && curBlobID != null && mergeBlobID != null) {
                if (!curBlobID.equals(mergeBlobID)) {
                    conflict = true;
                    processConflict(stagingArea, fileName, curBlobID, mergeBlobID);
                }
            }
        }
        return conflict;
    }
    private static void processConflict(StagingArea stagingArea, String fileName, String curBlobID, String mergeBlobID) {
        String newContens = conflictFileContents(curBlobID, mergeBlobID);
        Blob newBlob = new Blob(newContens.getBytes(StandardCharsets.UTF_8));
        newBlob.save();
        File file = Utils.join(CWD, fileName);
        Utils.writeContents(file, newContens);
        stagingArea.getAddition().put(fileName, newBlob.getBlobId());
    }
    private static String conflictFileContents(String curBlobID, String mergeBlobID) {
        String curContents;
        String mergeContents;
        if (curBlobID == null) {
            curContents = "";
        } else {
            curContents = Utils.readContentsAsString(Utils.join(Blob.BLOBS, curBlobID));
        }
        if (mergeBlobID == null) {
            mergeContents = "";
        } else {
            mergeContents = Utils.readContentsAsString(Utils.join(Blob.BLOBS, mergeBlobID));
        }
        return "<<<<<<< HEAD\n" + curContents + "=======\n" + mergeContents + ">>>>>>>\n";
    }
}
