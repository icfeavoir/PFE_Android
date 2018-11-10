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
            int note = noteDBModel.getNote();
            return new Note(student, project, profUsername, note);
        }
        else {
            return null;
        }

    }

    public static DBModel dbModelFactory(Model model) {
        if (model instanceof Project) {
            Project project = (Project) model;
            return new ProjectDBModel(
                project.getProjectId(),
                project.getTitle(),
                project.getDescription(),
                project.getConfid(),
                project.hasPoster(),
                project.getSupervisor(),
                project.getJuryId()
            );
        } else {
            return null;
        }
    }
}
