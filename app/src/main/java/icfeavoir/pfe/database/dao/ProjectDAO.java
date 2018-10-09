package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import icfeavoir.pfe.database.model.ProjectDBModel;

@Dao
public interface ProjectDAO {

    @Query("SELECT * FROM ProjectDBModel")
    List<ProjectDBModel> getAllProjects();

    @Query("SELECT * FROM ProjectDBModel where juryId=:juryId")
    List<ProjectDBModel> getProjectByJury(int juryId);

    @Query("SELECT * FROM ProjectDBModel where projectId=:projectId")
    ProjectDBModel getProject(int projectId);

    @Insert
    void insert(List<ProjectDBModel> projects);

    @Insert
    void insert(ProjectDBModel project);

    @Query("DELETE FROM ProjectDBModel where projectId=:projectId")
    void delete(int projectId);
}
