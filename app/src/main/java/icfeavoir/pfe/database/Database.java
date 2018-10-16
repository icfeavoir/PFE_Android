package icfeavoir.pfe.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import icfeavoir.pfe.database.dao.JuryDAO;
import icfeavoir.pfe.database.dao.NoteDAO;
import icfeavoir.pfe.database.dao.ProjectDAO;
import icfeavoir.pfe.database.model.JuryDBModel;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;

// increment the version at each database update !
@android.arch.persistence.room.Database(entities = {
        JuryDBModel.class,
        ProjectDBModel.class,
        NoteDBModel.class
}, version = 3)
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
    public abstract NoteDAO getNoteDAO();
}
