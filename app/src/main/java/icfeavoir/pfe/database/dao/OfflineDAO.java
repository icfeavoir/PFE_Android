package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import icfeavoir.pfe.database.model.OfflineDBModel;

@Dao
public interface OfflineDAO {

    @Insert
    void insert(OfflineDBModel query);

    @Query("SELECT * FROM OfflineDBModel")
    List<OfflineDBModel> getAllQueries();

    @Delete
    void delete(OfflineDBModel query);

}
