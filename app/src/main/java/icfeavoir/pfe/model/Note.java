package icfeavoir.pfe.model;

import com.google.gson.annotations.SerializedName;

public class Note {
    private int userId;
    private String forename;
    private String surname;
    @SerializedName("mynote")
    private int note;
    @SerializedName("avgnote")
    private int avgNote;

    public Note(int userId, String forename, String surname, int note, int avgNote) {
        this.userId = userId;
        this.forename = forename;
        this.surname = surname;
        this.note = note;
        this.avgNote = avgNote;
    }

    public int getUserId() {
        return userId;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public int getNote() {
        return note;
    }

    public int getAvgNote() {
        return avgNote;
    }

    public String getName() {
        return this.getForename() + " " + this.getSurname();
    }

    public String toString() {
        return "[NOTE] " +
                "userId " + this.getUserId() +
                " - name " + this.getName() +
                " - note " + this.getNote() +
                " - AVG " + this.getAvgNote();
    }
}
