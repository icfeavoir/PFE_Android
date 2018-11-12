package icfeavoir.pfe.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import icfeavoir.pfe.database.dao.JuryDAO;
import icfeavoir.pfe.database.dao.NoteDAO;
import icfeavoir.pfe.database.dao.OfflineDAO;
import icfeavoir.pfe.database.dao.StudentDAO;
import icfeavoir.pfe.database.dao.StudentProjectDAO;
import icfeavoir.pfe.database.dao.ProjectDAO;
import icfeavoir.pfe.database.dao.UserDAO;
import icfeavoir.pfe.database.model.JuryDBModel;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.database.model.OfflineDBModel;
import icfeavoir.pfe.database.model.StudentDBModel;
import icfeavoir.pfe.database.model.StudentProjectDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;
import icfeavoir.pfe.database.model.UserDBModel;

// increment the version at each database update !
@android.arch.persistence.room.Database(entities = {
        JuryDBModel.class,
        NoteDBModel.class,
        ProjectDBModel.class,
        UserDBModel.class,
        OfflineDBModel.class,
        StudentDBModel.class,
        StudentProjectDBModel.class,
}, version = 14)
public abstract class Database extends RoomDatabase {

    private static final String DB_NAME = "database.db";
    private static volatile Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static Database create(final Context context) {
        return Room.databaseBuilder(
                context,
                Database.class,
                DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract JuryDAO getJuryDAO();
    public abstract ProjectDAO getProjectDAO();
    public abstract UserDAO getUserDAO();
    public abstract NoteDAO getNoteDAO();
    public abstract OfflineDAO getOfflineDAO();
    public abstract StudentDAO getStudentDAO();
    public abstract StudentProjectDAO getStudentProjectDAO();
}
