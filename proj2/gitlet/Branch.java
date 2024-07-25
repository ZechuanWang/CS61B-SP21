package gitlet;

import java.io.File;

public class Branch {
    public static final File BRANCHES = Utils.join(Repository.GITLET_DIR, "branches");

    /**Writes commitID(pointer) into the branch name file. */
    public static void setCommitID(String branchName, String commitID) {
        File branch = Utils.join(BRANCHES, branchName);
        Utils.writeContents(branch, commitID);
    }

    /**Returns the commit id in the branch file. */
    public static String getCommitID(String branchName) {
        File branch = Utils.join(BRANCHES, branchName);
        if (!branch.exists()) {
            return null;
        }
        return Utils.readContentsAsString(branch);
    }
}
