package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import icfeavoir.pfe.database.model.StudentProjectDBModel;

@Dao
public interface StudentProjectDAO {

    @Query("SELECT * FROM StudentProjectDBModel where projectId=:projectId")
    List<StudentProjectDBModel> getProjectPersons(int projectId);

    @Insert
    void insert(List<StudentProjectDBModel> personProject);

    @Insert
    void insert(StudentProjectDBModel personProject);

    @Query("DELETE FROM StudentProjectDBModel where projectId=:projectId")
    void delete(int projectId);
}
