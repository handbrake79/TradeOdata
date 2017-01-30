package ru.sk42.tradeodata.NetworkRequests;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.Model.St;

/**
 * Created by хрюн моржов on 26.01.2017.
 */

public class CheckServerAvailability extends AsyncTask<Activity, Void, Boolean> {
    Activity activity;

    protected Boolean doInBackground(Activity... params) {
        activity = params[0];
        ConnectivityManager connMan = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL urlServer = new URL(Settings.getServerAddressStatic() + Settings.getInfoBaseNameStatic());
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
                urlConn.connect();
                if (urlConn.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(activity, "Сервер доступен", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Ошибка подключения!", Toast.LENGTH_SHORT).show();
        }
    }
}



