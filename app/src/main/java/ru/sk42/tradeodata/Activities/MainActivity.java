package ru.sk42.tradeodata.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.sk42.tradeodata.Activities.Documents_List.DocList_Activity;
import ru.sk42.tradeodata.Activities.Settings.SettingsActivity;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.St;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;

import static ru.sk42.tradeodata.Model.Constants.MESSAGE_LABEL;

public class MainActivity extends AppCompatActivity implements ServiceResultReceiver.ServiceResultReceiverInterface {

    ServiceResultReceiver mReceiver;
    long mPrevTime, curtime;
    ProgressDialog progressDialog;

    private static boolean mNetworkAvailable = false;

    private static String TAG = "*** MainAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._main__activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initApplication();

    }

    public void deleteDatabaseAndReload(View view) {
        MyHelper.dropAndCreateTables();
        initApplication();
    }

    public class CheckServerAvailabilityAsync extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... params) {
            ConnectivityManager connMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL urlServer = new URL(Settings.getServerAddressStatic() + Settings.getInfoBaseNameStatic());
                    HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                    urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
                    urlConn.connect();
                    return urlConn.getResponseCode() == 200;
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
                callPreload();
            } else {
                progressDialog.hide();
                MainActivity.this.showToast("Сервер недоступен!");
            }
        }
    }

    private void callPreload() {
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(MainActivity.this);
        Charact.createStub();
        Intent i = new Intent(MainActivity.this, CommunicationWithServer.class);
        i.putExtra("from", "MainAct");
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.PRELOAD.ordinal());
        i.putExtra("receiverTag", mReceiver);
        startService(i);
    }

    private void initApplication() {

        progressDialog = new ProgressDialog(this);

        MyHelper.getInstance(getApplication());
        MyHelper.createTables();

        St.setApplication(getApplication());

        progressDialog.setTitle("Проверка доступности сервера");
        progressDialog.show();
        AsyncTask<String, Void, Boolean> task = new CheckServerAvailabilityAsync();
        task.execute();

    }


    public void btnSettingsOnClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (mPrevTime != 0) {
            curtime = System.currentTimeMillis();
            if ((curtime - mPrevTime) / 1000 <= 3) {
                finish();
                System.exit(0);
            } else mPrevTime = 0;
        }
        if (mPrevTime == 0) {
            showSnack("Нажмите еще раз для выхода");
            mPrevTime = System.currentTimeMillis();
            return;
        }
    }


    public void btnDocListClick(View view) {
        showDocList();
    }

    private void showDocList() {
        Intent intent = new Intent(this, DocList_Activity.class);
        startActivity(intent);

    }

    @Override
    public void onReceiveResultFromService(int resultCode, Bundle resultData) {

        if (resultCode == Constants.FEEDBACK) {
            String message = resultData.getString(MESSAGE_LABEL);

            if (message != null) {
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Загрузка данных с сервера");
                progressDialog.setMessage(message);
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
            return;
        }

        Constants.REQUESTS requestedOperation = Constants.REQUESTS.values()[resultCode];
        if (requestedOperation == Constants.REQUESTS.PRELOAD) {
            progressDialog.dismiss();

            showSnack("Предварительная загрузка завершена");

            //TODO         // переписать нормально
            try {
                Settings.setDefaultStartingPointStatic(MyHelper.getStartingPointDao().queryForEq(Constants.REF_KEY_LABEL, "96487975-3968-426e-9dff-50f4da82431e").get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Settings.setDefaultVehicleTypeStatic(MyHelper.getVehicleTypesDao().queryForEq(Constants.REF_KEY_LABEL, "b56961f4-294a-11e2-a8fe-984be1645107").get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            showDocList();
        }
    }

    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    void showSnack(String s) {
        Snackbar.make(getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
    }

}
