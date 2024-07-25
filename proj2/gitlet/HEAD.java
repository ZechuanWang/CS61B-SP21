package gitlet;

import java.io.File;

/**HEAD file contains the name of the current branch, default set head point at master. */
public class HEAD {
    public static final File HEAD = Utils.join(Repository.GITLET_DIR, "head");

    /**Set the head. */
    public static void setHead(String branchName) {
        Utils.writeContents(HEAD, branchName);
    }

    /**Returns the head content. */
    public static String getHead() {
        return Utils.readContentsAsString(HEAD);
    }
}
