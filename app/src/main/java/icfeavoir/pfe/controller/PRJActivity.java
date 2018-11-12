package icfeavoir.pfe.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import icfeavoir.pfe.proxy.GlobalNoteProxy;
import icfeavoir.pfe.proxy.LIPRJProxy;
import icfeavoir.pfe.proxy.NEWNTProxy;
import icfeavoir.pfe.proxy.NOTESProxy;
import icfeavoir.pfe.proxy.POSTRProxy;
import icfeavoir.pfe.proxy.PosterCommentProxy;
import icfeavoir.pfe.proxy.Proxy;

public class PRJActivity extends PFEActivity {

    private int projectId;
    private TextView title;
    private TextView supervisor;
    private RelativeLayout confid;
    private Button globalNoteButton;
    private Button posterButton;
    private TextView description;
    private LinearLayout studentsList;
    private String posterComment;

    private LayoutInflater inflater;

    private Project project;
    private Double globalNote;
    private Map<Integer, Double> notes;
    private Map<Integer, Double> avgNotes;

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
        this.posterComment = "";

        this.inflater = getLayoutInflater();
        this.notes = new HashMap<>();
        this.avgNotes = new HashMap<>();
        this.globalNote = -1.0;

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

        Log.i("POPUP", "gn " + globalNote);
        if (globalNote >= 0) {
            input.setText(globalNote + "");
        }

        final GlobalNoteProxy proxy = new GlobalNoteProxy(this);
        final JSONObject json = new JSONObject();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Double note = Double.parseDouble(input.getText().toString());
                    json.put("proj", projectId);
                    json.put("note", note);
                    json.put("SAVE", true);
                    proxy.call(json);
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

        if (notes.containsKey(student.getStudentId()) && notes.get(student.getStudentId()) >= 0) {
            input.setText(notes.get(student.getStudentId()) + "");
        }

        final NEWNTProxy NEWNTproxy = new NEWNTProxy(this);
        final JSONObject NEWNTjson = new JSONObject();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Double note = Double.parseDouble(input.getText().toString());
                    if (note < 0 || note > 20) {
                        throw new Exception("Invalid note");
                    }
                    notes.put(student.getStudentId(), note);
                    NEWNTjson.put("proj", projectId);
                    NEWNTjson.put("student", student.getStudentId());
                    NEWNTjson.put("note", note);
                    NEWNTproxy.call(NEWNTjson);

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

    private void showPosterPopup() {
        final PFEActivity it = this;
        final int projectId = this.projectId;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Poster");

        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(this);
        input.setHint("Commentaire...");
        final Button bigPoster = new Button(this);
        bigPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(it, POSTRActivity.class);
                i.putExtra("projectId", projectId);
                startActivity(i);
            }
        });
        bigPoster.setBackground(getDrawable(R.drawable.pretty_button));
        bigPoster.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bigPoster.setTextColor(getResources().getColor(R.color.darkBlue));
        bigPoster.setText(R.string.big_poster_project_btn);
        view.addView(bigPoster);
        view.addView(input);
        builder.setView(view);

        input.setText(posterComment);

        final PosterCommentProxy proxy = new PosterCommentProxy(this);
        final JSONObject json = new JSONObject();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String comment = input.getText().toString();
                    json.put("proj", projectId);
                    json.put("comment", comment);
                    json.put("SAVE", true);
                    proxy.call(json);
                    posterComment = comment;

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
        this.studentsList.removeAllViews();
        for (final Student student : this.project.getStudents()) {
            View studentView = inflater.inflate(R.layout.project_student_note_button, this.studentsList, false);

            TextView studentName = studentView.findViewById(R.id.student_name);
            studentName.setText(student.getFullName());

            TextView avgNote = studentView.findViewById(R.id.student_avg_note);
            if (this.avgNotes.containsKey(student.getStudentId()) && this.avgNotes.get(student.getStudentId()) >= 0) {
                // at least a note
                avgNote.setText(String.format(getResources().getString(R.string.project_avg_note), this.avgNotes.get(student.getStudentId()).toString()));
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

    private void setProjectInfo(final Project project) {
        this.project = project;
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
                posterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPosterPopup();
                    }
                });
            }
        });

        // call the notes proxy
        NOTESProxy proxy = new NOTESProxy(this);
        JSONObject json = new JSONObject();
        try {
            json.put("proj", projectId);
            proxy.call(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // call the global note proxy
        GlobalNoteProxy globalNoteProxy = new GlobalNoteProxy(this);
        JSONObject globalNoteJson = new JSONObject();
        try {
            globalNoteJson.put("proj", projectId);
            globalNoteJson.put("GET", true);
            globalNoteProxy.call(globalNoteJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // call the poster comment proxy
        PosterCommentProxy posterCommentProxy = new PosterCommentProxy(this);
        JSONObject posterCommentJson = new JSONObject();
        try {
            posterCommentJson.put("proj", projectId);
            posterCommentJson.put("GET", true);
            posterCommentProxy.call(posterCommentJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setProjectNotes(final List<Note> notes) {
        for (Note note : notes) {
            this.notes.put(note.getStudent().getStudentId(), note.getNote());
            this.avgNotes.put(note.getStudent().getStudentId(), note.getAvg());
        }

        // we add students after we get project infos + notes
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addStudents();
            }
        });
    }

    @Override
    public void displayData(Object data) {
        if (data instanceof Project) {
            this.setProjectInfo((Project) data);
        } else if (data instanceof ArrayList && ((ArrayList) data).size() > 0 && ((ArrayList) data).get(0) instanceof Note) {
            this.setProjectNotes((ArrayList<Note>) data);
        } else if (data instanceof HashMap) {
            try {
                HashMap<possibleDataType, Object> map = (HashMap<possibleDataType, Object>) data;
                if (map.containsKey(possibleDataType.GLOBAL_NOTE)) {
                    // this is the global note
                    this.globalNote = (Double) map.get(possibleDataType.GLOBAL_NOTE);
                } else if (map.containsKey(possibleDataType.POSTER_COMMENT)) {
                    // poster comment
                    this.posterComment = (String) map.get(possibleDataType.POSTER_COMMENT);
                }
            } catch (Exception e) {

            }
        } else {
            this.noProjectException();
        }
    }

    public enum possibleDataType {
        GLOBAL_NOTE,
        POSTER_COMMENT,
    }
}
