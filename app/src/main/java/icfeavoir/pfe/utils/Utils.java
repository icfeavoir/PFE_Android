package icfeavoir.pfe.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import icfeavoir.pfe.R;
import icfeavoir.pfe.communication.Communication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.OfflineDBModel;
import icfeavoir.pfe.notification.NotificationPublisher;
import icfeavoir.pfe.proxy.Proxy;

public class Utils {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void scheduleNotification(Context context, Notification notification, int delay, int id) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        }
    }

    public static Notification getNotification(Context context, String title, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        return builder.build();
    }

    public static void sendOfflineData(final PFEActivity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<OfflineDBModel> queries = Database.getInstance(activity.getApplicationContext()).getOfflineDAO().getAllQueries();
                for (OfflineDBModel query : queries) {
                    Proxy proxy = Proxy.proxyBuilder(Communication.API_ENDPOINTS.valueOf(query.getEndpoint()), activity);
                    try {
                        JSONObject json = new JSONObject(query.getQuery());
                        proxy.call(json);
                        // delete it
                        Database.getInstance(activity.getApplicationContext()).getOfflineDAO().delete(query);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
