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
    @Query("SELECT * FROM NoteDBModel WHERE projectId=:projectId AND profUsername=:username")
    List<NoteDBModel> getNotesByProjectIdByProfUsername(int projectId, String username);
    @Query("SELECT * FROM NoteDBModel WHERE studentId=:studentId AND projectId=:projectId AND profUsername=:username LIMIT 1")
    NoteDBModel getNoteByStudentByProjectIdByProfUsername(int studentId, int projectId, String username);
    @Query("SELECT * FROM NoteDBModel")
    List<NoteDBModel> getAll();

    @Query("UPDATE NoteDBModel SET note=:newNote WHERE projectId=:projectId AND studentId=:studentId AND profUsername=:profUsername")
    void updateNote(int newNote, int projectId, int studentId, String profUsername);

    @Query("DELETE FROM NoteDBModel WHERE projectId=:projectId")
    void deleteByProjectId(int projectId);
}
