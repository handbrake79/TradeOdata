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
import android.support.v4.app.Fragment;
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
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
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
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._main__activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (St.getInitComplete()) {
            finish();
            System.exit(0);
        } else {
            initApplication();
        }

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
                hideLoading();
                MainActivity.this.showToast("Сервер недоступен!");
                showSettingsActivity();
            }
        }
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void callPreload() {
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(MainActivity.this);
        Charact.createStub();
        DiscountCard.createStub();
        Intent i = new Intent(MainActivity.this, CommunicationWithServer.class);
        i.putExtra("from", "MainAct");
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.PRELOAD.ordinal());
        i.putExtra("receiverTag", mReceiver);
        showLoading("Загрузка данных с сервера 1С");
        startService(i);
    }

    private void initApplication() {

        progressDialog = new ProgressDialog(this);

        MyHelper.getInstance(getApplication());
        MyHelper.createTables();
        //MyHelper.dropAndCreateTables();

        St.setApplication(getApplication());

        showLoading("Проверка доступности сервера 1С");
        AsyncTask<String, Void, Boolean> task = new CheckServerAvailabilityAsync();
        task.execute();

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


    @Override
    public void onResume() {
        super.onResume();
        if (St.getInitComplete()) {
            finish();
        }
    }

    private void showLoading(String text) {
        if (loadingFragment == null) {
            loadingFragment = LoadingFragment.newInstance(text);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.loading, loadingFragment, "loading")
                    .addToBackStack("loading")
                    .commit();
        } else {
            loadingFragment.showMessage(text);
        }

    }

    private void hideLoading() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("loading");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onReceiveResultFromService(int resultCode, Bundle mResult) {
        if (resultCode == Constants.FEEDBACK) {
            String message = mResult.getString(MESSAGE_LABEL);

            if (message != null) {
                showLoading(message);
            }
            return;
        }

        hideLoading();
        Constants.REQUESTS requestedOperation = Constants.REQUESTS.values()[resultCode];
        if (requestedOperation == Constants.REQUESTS.PRELOAD) {
            boolean success = mResult.getBoolean(Constants.OPERATION_SUCCESS_LABEL);
            if (!success) {
                showToast(mResult.getString(Constants.MESSAGE_LABEL));
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return;
            }

//            showSnack("Предварительная загрузка завершена");
            St.setInitComplete();
            if (!Uttils.isUserSet()) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(Constants.REQUEST_SETTINGS_USER_LABEL, Constants.REQUEST_SETTINGS_USER);
                startActivityForResult(intent, Constants.REQUEST_SETTINGS_USER);
                finish();
            } else {
                Intent intent = new Intent(this, DocList_Activity.class);
                startActivity(intent);
            }
        }
    }

    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    void showSnack(String s) {
        Snackbar.make(getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
    }

}
