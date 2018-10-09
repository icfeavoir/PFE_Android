package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.communication.MYJURCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.JuryDBModel;
import icfeavoir.pfe.model.Jury;

public class MYJURProxy extends Proxy {

    public MYJURProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void getDataFromInternet(JSONObject json) {
        // call the communication class and give "this" to enable the callback
        MYJURCommunication com = new MYJURCommunication(this.getContext(), this);
        // empty JSON for all data
        com.getData(json);
    }

    @Override
    void getDataWithoutInternet(JSONObject json) {
        ArrayList<Jury> allJuries = new ArrayList<>();
        // call the database
        try {
            List<JuryDBModel> juries = Database.getInstance(this.getContext()).getJuryDAO().getAllJuries();
            // convert JuryDB in Jury
            Jury jury;
            for (JuryDBModel juryDB : juries) {
                jury = new Jury(juryDB.getJuryId(), juryDB.getDate());
                allJuries.add(jury);
            }

            // display data
            this.sendDataToController(allJuries);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveDataFromInternet(ArrayList<?> elements) {
        Jury jury;
        List<JuryDBModel> juriesDB = new ArrayList<>();
        // convert every Jury in JuryDB
        for (Object obj : elements) {
            try {
                jury = (Jury) obj;
                juriesDB.add(new JuryDBModel(jury.getJuryId(), jury.getDate()));
            } catch (Exception e){
                e.printStackTrace();
            }

            // save data in DB
            Database.getInstance(this.getContext()).getJuryDAO().insert(juriesDB);
        }
    }

    /*
    * Callback when retrieving data from the Internet
     */
    @Override
    public void returnDataAfterInternet(JSONObject json) {
        ArrayList<Jury> alJuries = new ArrayList<>();
        Jury jury = null;
        try {
            JSONArray juriesArr = json.getJSONArray("juries");
            for (int i = 0; i < juriesArr.length(); i++) {
                JSONObject juryObject = juriesArr.getJSONObject(i);
                jury = new Jury(juryObject);

                // add jury to the list
                alJuries.add(jury);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // at the end we display data
        this.sendDataToController(alJuries);

        // and we save everything in the db
        this.saveDataFromInternet(alJuries);
    }

    @Override
    void sendDataToController(ArrayList<?> elements) {
        try {
            ArrayList<Jury> juries = (ArrayList<Jury>) elements;
            this.getActivity().displayData(juries);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
