package icfeavoir.pfe.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import icfeavoir.pfe.database.model.NoteDBModel;

@Dao
public interface NoteDAO {

    @Insert
    void insert(List<NoteDBModel> notes);
    @Insert
    void insert(NoteDBModel note);

    @Query("SELECT * FROM NoteDBModel WHERE projectId=:projectId")
    List<NoteDBModel> getNotesByProjectId(int projectId);
    @Query("SELECT * FROM NoteDBModel")
    List<NoteDBModel> getAll();

    @Query("UPDATE NoteDBModel SET note=:newNote WHERE projectId=:projectId AND studentId=:studentId")
    void updateNote(int newNote, int projectId, int studentId);

    @Query("DELETE FROM NoteDBModel WHERE projectId=:projectId")
    void deleteByProjectId(int projectId);
}
