package icfeavoir.pfe.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import icfeavoir.pfe.R;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.model.Note;
import icfeavoir.pfe.model.Project;
import icfeavoir.pfe.model.Student;
import icfeavoir.pfe.model.User;
import icfeavoir.pfe.proxy.LIPRJProxy;
import icfeavoir.pfe.proxy.NEWNTProxy;

public class PRJActivity extends PFEActivity {

    private int projectId;
    private TextView title;
    private TextView supervisor;
    private RelativeLayout confid;
    private Button posterButton;
    private TextView description;
    private LinearLayout studentsList;

    private LayoutInflater inflater;

    private Project project;
    private Map<Integer, Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj);

        this.title = findViewById(R.id.project_title);
        this.supervisor = findViewById(R.id.project_supervisor);
        this.confid = findViewById(R.id.project_confid);
        this.posterButton = findViewById(R.id.project_poster_button);
        this.description = findViewById(R.id.project_description);
        this.studentsList = findViewById(R.id.project_students_list);

        this.inflater = getLayoutInflater();
        this.notes = new HashMap<>();

        try {
            this.projectId = getIntent().getExtras().getInt("projectId");
            this.getProjectInfo(this.projectId);
        } catch (NullPointerException e) {
            this.noProjectException();
        }
    }

    private void getProjectInfo(int projectId) {
        JSONObject json = new JSONObject();
        try {
            json.put("projectId", this.projectId);
            LIPRJProxy proxy = new LIPRJProxy(this);
            proxy.call(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void noProjectException() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                title.setText(R.string.no_project_error);
                supervisor.setVisibility(View.INVISIBLE);
                confid.setVisibility(View.INVISIBLE);
                posterButton.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                studentsList.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showNotePopup(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        if (notes.containsKey(student.getStudentId())) {
            input.setText(notes.get(student.getStudentId()).getNote() + "");
        }

        final NEWNTProxy proxy = new NEWNTProxy(this);
        final JSONObject json = new JSONObject();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int note = Integer.parseInt(input.getText().toString());
                    notes.put(student.getStudentId(), new Note(student, project, User.getInstance().getUsername(), note));
                    json.put("proj", projectId);
                    json.put("student", student.getStudentId());
                    json.put("note", note);
                    proxy.call(json);

                } catch (Exception e) {
                    // probably a string...
                    Toast.makeText(getApplicationContext(), "Votre note n'a pas été enregistrée", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addStudents() {
        for (final Student student : this.project.getStudents()) {
            View studentView = inflater.inflate(R.layout.project_student_note_button, this.studentsList, false);
            TextView studentName = studentView.findViewById(R.id.student_name);
            studentName.setText(student.getFullName());

            Button noter = studentView.findViewById(R.id.student_note_btn);
            noter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNotePopup(student);
                }
            });

            studentsList.addView(studentView);
        }
    }

    @Override
    public void displayData(Object data) {
        try {
            this.project = (Project) data;

            // get the notes
            final List<NoteDBModel> notesDB = Database.getInstance(getApplicationContext())
                    .getNoteDAO()
                    .getNotesByProjectIdByProfUsername(project.getProjectId(), User.getInstance().getUsername());
            for (NoteDBModel noteDBModel : notesDB) {
                this.notes.put(noteDBModel.getStudentId() ,new Note(noteDBModel, getApplicationContext()));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    title.setText(project.getTitle());
                    if (! project.getSupervisor().equals("")) {
                        supervisor.setText(String.format(getResources().getString(R.string.project_superviseur), project.getSupervisor()));
                    }
                    confid.setVisibility(project.getConfid() == 1 ? View.VISIBLE : View.INVISIBLE);
                    posterButton.setText(R.string.go_to_poster_btn);
                    description.setText(project.getDescription().isEmpty() ? getResources().getString(R.string.project_no_description) : project.getDescription());
                    addStudents();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            this.noProjectException();
        }
    }
}
