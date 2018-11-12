package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import icfeavoir.pfe.database.model.ProjectDBModel;
import icfeavoir.pfe.model.Student;

@Dao
public interface ProjectDAO {

    @Query("SELECT * FROM ProjectDBModel")
    List<ProjectDBModel> getAllProjects();

    @Query("SELECT * FROM ProjectDBModel where juryId=:juryId")
    List<ProjectDBModel> getProjectByJury(int juryId);

    @Query("SELECT * FROM ProjectDBModel where projectId=:projectId")
    ProjectDBModel getProject(int projectId);

    @Query("SELECT * FROM StudentProjectDBModel where projectId=:projectId")
    List<Student> getStudents(int projectId);

    @Query("UPDATE ProjectDBModel SET globalNote=:globalNote WHERE projectId=:projectId")
    void updateGlobalNote(Double globalNote, int projectId);
    @Query("SELECT globalNote FROM ProjectDBModel WHERE projectId=:projectId")
    Double getGlobalNote(int projectId);

    @Query("UPDATE ProjectDBModel SET comment=:comment WHERE projectId=:projectId")
    void updatePosterComment(String comment, int projectId);
    @Query("SELECT comment FROM ProjectDBModel WHERE projectId=:projectId")
    String getPosterComment(int projectId);

    @Insert
    void insert(List<ProjectDBModel> projects);

    @Insert
    void insert(ProjectDBModel project);

    @Query("DELETE FROM ProjectDBModel where projectId=:projectId")
    void delete(int projectId);
}
