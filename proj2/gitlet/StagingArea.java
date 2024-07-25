package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class StagingArea implements Serializable {

    public static final File STAGEAREA = Utils.join(Repository.GITLET_DIR, "stage");

    private Map<String, String> addition;

    private Map<String, String> removal;

    public StagingArea() {
        addition = new HashMap<>();
        removal = new HashMap<>();
    }

    public Map<String, String> getFileToBlobID() {
        return addition;
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

    public void clear() {
        addition.clear();
    }

}
