package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import icfeavoir.pfe.database.model.JuryDBModel;

@Dao
public interface JuryDAO {

    @Query("SELECT * FROM JuryDBModel")
    List<JuryDBModel> getAllJuries();

    @Query("SELECT * FROM JuryDBModel where juryId=:juryId")
    JuryDBModel getJury(int juryId);

    @Insert
    void insert(List<JuryDBModel> juries);

    @Insert
    void insert(JuryDBModel jury);

    @Query("DELETE FROM JuryDBModel where juryId=:juryId")
    void delete(int juryId);
}
