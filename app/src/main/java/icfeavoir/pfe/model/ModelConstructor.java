package icfeavoir.pfe.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.DBModel;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;
import icfeavoir.pfe.database.model.StudentDBModel;
import icfeavoir.pfe.database.model.StudentProjectDBModel;

public class ModelConstructor  {

    public static Model modelFactory(DBModel dbModel, Context context) {
        if (dbModel instanceof ProjectDBModel) {
            ProjectDBModel projectDBModel = (ProjectDBModel) dbModel;
            Project project = new Project(
                projectDBModel.getProjectId(),
                projectDBModel.getTitle(),
                projectDBModel.getDescription(),
                projectDBModel.getConfid(),
                projectDBModel.hasPoster(),
                projectDBModel.getSupervisor(),
                projectDBModel.getJuryId(),
                new ArrayList<Student>()
            );
            // get the students
            List<StudentProjectDBModel> studentsProject = Database.getInstance(context).getStudentProjectDAO().getProjectPersons(projectDBModel.getProjectId());
            for (StudentProjectDBModel ppDB : studentsProject) {
                StudentDBModel studentDB = Database.getInstance(context).getStudentDAO().getPerson(ppDB.getStudentId());
                Student student = new Student(studentDB.getStudentId(), studentDB.getForename(), studentDB.getSurname());
                project.addStudent(student);
            }
            return project;
        }
        else if (dbModel instanceof NoteDBModel) {
            NoteDBModel noteDBModel = (NoteDBModel) dbModel;
            StudentDBModel studentDB = Database.getInstance(context)
                    .getStudentDAO()
                    .getPerson(noteDBModel.getStudentId());
            Student student = new Student(studentDB, context);
            ProjectDBModel projectDB = Database.getInstance(context)
                    .getProjectDAO()
                    .getProject(noteDBModel.getProjectId());
            Project project = (Project) modelFactory(projectDB, context);
            String profUsername = noteDBModel.getProfUsername();
            Double note = noteDBModel.getNote();
            Double avg = noteDBModel.getAvg();
            return new Note(student, project, profUsername, note, avg);
        }
        else {
            return null;
        }

    }

    public static DBModel dbModelFactory(Model model, Context context) {
        if (model instanceof Project) {
            final Project project = (Project) model;
            // check is this project already exists
            Double globalNote = Database.getInstance(context)
                    .getProjectDAO()
                    .getGlobalNote(project.getProjectId());
            String comment = Database.getInstance(context)
                    .getProjectDAO()
                    .getPosterComment(project.getProjectId());
            return new ProjectDBModel(
                    project.getProjectId(),
                    project.getTitle(),
                    project.getDescription(),
                    project.getConfid(),
                    project.hasPoster(),
                    project.getSupervisor(),
                    project.getJuryId(),
                    globalNote,
                    comment);

        } else {
            return null;
        }
    }
}
