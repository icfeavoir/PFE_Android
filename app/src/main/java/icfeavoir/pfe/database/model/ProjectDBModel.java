package icfeavoir.pfe.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ProjectDBModel {

    @PrimaryKey private final int projectId;
    private final String title;
    private final String description;
    private final int confid;
    private final boolean poster;
    private final String supervisor;


    public ProjectDBModel(int projectId, String title, String description, int confid, boolean poster, String supervisor) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.confid = confid;
        this.poster = poster;
        this.supervisor = supervisor;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getConfid() {
        return confid;
    }

    public boolean isPoster() {
        return poster;
    }

    public String getSupervisor() {
        return supervisor;
    }
}
