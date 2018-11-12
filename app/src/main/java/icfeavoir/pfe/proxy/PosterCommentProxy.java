package icfeavoir.pfe.proxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.controller.PRJActivity;
import icfeavoir.pfe.database.Database;

/**
 * Special Proxy only in local
 */
public class PosterCommentProxy extends Proxy {
    public PosterCommentProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(JSONObject json) {
        this.callWithoutInternet(json);
    }

    @Override
    void callWithoutInternet(final JSONObject json) {
        if (json.has("proj")) {
            try {
                final int projectId = json.getInt("proj");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // save in the database
                        try {
                            if (json.has("SAVE") && json.has("comment")) {
                                String comment = json.getString("comment");
                                Database.getInstance(getContext())
                                        .getProjectDAO()
                                        .updatePosterComment(comment, projectId);
                            } else if (json.has("GET")) {
                                String posterComment = Database.getInstance(getContext())
                                        .getProjectDAO()
                                        .getPosterComment(projectId);
                                sendDataToController(posterComment);
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {

    }

    @Override
    void sendDataToController(Object elements) {
        if (elements instanceof String) {
            Map<PRJActivity.possibleDataType, String> map = new HashMap<>();
            map.put(PRJActivity.possibleDataType.POSTER_COMMENT, (String) elements);
            this.getActivity().displayData(map);
        }
    }
}
