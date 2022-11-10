package DocumentManager;

import java.sql.Timestamp;

public class Doc {
    private String ID;
    private String creator;
    private Timestamp timestamp;
    private String description;
    private String filename;

    public Doc(String ID, String creator, Timestamp timestamp, String description, String filename) {
        super();//为什么一个没有父类的类ye'需要调用super来初始化他
        this.ID = ID;
        this.creator = creator;
        this.timestamp = timestamp;
        this.description = description;
        this.filename=filename;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
