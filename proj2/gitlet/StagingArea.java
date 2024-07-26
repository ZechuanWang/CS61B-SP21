package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class StagingArea implements Serializable {

    public static final File STAGEAREA = Utils.join(Repository.GITLET_DIR, "stage");

    /**file name to blob id mapping. */
    private Map<String, String> addition;

    private Map<String, String> removal;

    public StagingArea() {
        addition = new HashMap<>();
        removal = new HashMap<>();
    }


    public static StagingArea load() {
        return Utils.readObject(STAGEAREA, StagingArea.class);
    }


    public void save() {
        Utils.writeObject(STAGEAREA, this);
    }

    public Map<String, String> getAddition() {
        return addition;
    }

    public Map<String, String> getRemoval() {
        return removal;
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }

}
