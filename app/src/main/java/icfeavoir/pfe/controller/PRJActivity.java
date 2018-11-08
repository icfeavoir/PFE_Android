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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import icfeavoir.pfe.R;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;
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
    private Button globalNoteButton;
    private Button posterButton;
    private TextView description;
    private LinearLayout studentsList;

    private LayoutInflater inflater;

    private Project project;
    private int globalNote;
    private Map<Integer, Integer> notes;
    private Map<Integer, Integer> avgNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj);

        this.title = findViewById(R.id.project_title);
        this.supervisor = findViewById(R.id.project_supervisor);
        this.confid = findViewById(R.id.project_confid);
        this.globalNoteButton = findViewById(R.id.project_global_note_button);
        this.posterButton = findViewById(R.id.project_poster_button);
        this.description = findViewById(R.id.project_description);
        this.studentsList = findViewById(R.id.project_students_list);

        this.inflater = getLayoutInflater();
        this.notes = new HashMap<>();
        this.avgNotes = new HashMap<>();
        this.globalNote = -1;

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
                globalNoteButton.setVisibility(View.INVISIBLE);
                posterButton.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                studentsList.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showGlobalNotePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        if (globalNote != -1) {
            input.setText(globalNote + "");
        }

        final NEWNTProxy proxy = new NEWNTProxy(this);
        final JSONObject json = new JSONObject();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // TODO: find how to save a global note with API ???
                    int note = Integer.parseInt(input.getText().toString());
//                    notes.put(student.getStudentId(), new Note(student, project, User.getInstance().getUsername(), note));
//                    json.put("proj", projectId);
//                    json.put("student", student.getStudentId());
//                    json.put("note", note);
//                    proxy.call(json);
                    globalNote = note;

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

    private void showNotePopup(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        if (notes.containsKey(student.getStudentId())) {
            input.setText(notes.get(student.getStudentId()) + "");
        }

        final NEWNTProxy proxy = new NEWNTProxy(this);
        final JSONObject json = new JSONObject();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int note = Integer.parseInt(input.getText().toString());
                    if (note < 0 || note > 20) {
                        throw new Exception("Invalid note");
                    }
                    notes.put(student.getStudentId(), note);
                    json.put("proj", projectId);
                    json.put("student", student.getStudentId());
                    json.put("note", note);
                    proxy.call(json);

                } catch (Exception e) {
                    // probably a string or nothing or -1 or 42
                    Toast.makeText(getApplicationContext(), "Votre note n'a pas été enregistrée car elle est invalide", Toast.LENGTH_LONG).show();
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

            TextView avgNote = studentView.findViewById(R.id.student_avg_note);
            if (this.avgNotes.containsKey(student.getStudentId()) && this.avgNotes.get(student.getStudentId()) != -1) {
                // at least a note
                avgNote.setText(String.format(getResources().getString(R.string.project_avg_note), this.avgNotes.get(student.getStudentId()).toString()));
            } else {

                avgNote.setText(R.string.project_student_no_note);
            }

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
                    .getNotesByProjectId(project.getProjectId());

            Map<Integer, List<Integer>> studentsToNotes = new HashMap<>();
            for (NoteDBModel noteDBModel : notesDB) {
                if (noteDBModel.getProfUsername().equals(User.getInstance().getUsername())) {
                    // this prof note
                    this.notes.put(noteDBModel.getStudentId(), noteDBModel.getNote());
                }
                if (studentsToNotes.containsKey(noteDBModel.getStudentId())) {
                    // we add a note in this list
                    studentsToNotes.get(noteDBModel.getStudentId()).add(noteDBModel.getNote());
                } else {
                    studentsToNotes.put(noteDBModel.getStudentId(), new ArrayList<Integer>());
                    studentsToNotes.get(noteDBModel.getStudentId()).add(noteDBModel.getNote());
                }
            }

            for (Map.Entry<Integer, List<Integer>> entry : studentsToNotes.entrySet()) {
                int studentId = entry.getKey();
                List<Integer> userNotes = entry.getValue();
                int avg = -1;
                int sum = 0;
                for (int note : userNotes ) {
                    sum += note;
                }
                if (userNotes.size() > 0) {
                    avg = sum / userNotes.size();
                }
                this.avgNotes.put(studentId, avg);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    title.setText(project.getTitle());
                    if (! project.getSupervisor().equals("")) {
                        supervisor.setText(String.format(getResources().getString(R.string.project_superviseur), project.getSupervisor()));
                    }
                    globalNoteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showGlobalNotePopup();
                        }
                    });
                    confid.setVisibility(project.getConfid() == 1 ? View.VISIBLE : View.INVISIBLE);
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
