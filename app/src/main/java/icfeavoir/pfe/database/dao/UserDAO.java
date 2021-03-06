package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import icfeavoir.pfe.database.model.UserDBModel;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM UserDBModel LIMIT 1")
    UserDBModel getUser();

    @Query("SELECT * FROM UserDBModel WHERE username=:username AND password=:password LIMIT 1")
    UserDBModel getUser(String username, String password);

    @Insert
    void insert(UserDBModel user);

    @Query("DELETE FROM UserDBModel")
    void delete();

}
