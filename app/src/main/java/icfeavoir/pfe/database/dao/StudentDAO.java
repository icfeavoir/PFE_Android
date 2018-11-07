package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import icfeavoir.pfe.database.model.StudentDBModel;

@Dao
public interface StudentDAO {

    @Query("SELECT * FROM StudentDBModel")
    List<StudentDBModel> getAllPersons();

    @Query("SELECT * FROM StudentDBModel where studentId=:peopleId")
    StudentDBModel getPerson(int peopleId);

    @Insert
    void insert(List<StudentDBModel> persons);

    @Insert
    void insert(StudentDBModel person);

    @Query("DELETE FROM StudentDBModel where studentId=:personId")
    void delete(int personId);
}
