package ru.sk42.tradeodata.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ru.sk42.tradeodata.Activities.Documents_List.DocList_Activity;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Settings;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.LoadDataFromServer;
import ru.sk42.tradeodata.Services.MyResultReceiver;

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver {

    MyResultReceiver mReceiver;
    long prevtime, curtime;
    ProgressDialog progressDialog;

    String TAG = "*** MainAct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.____main____activity);



        progressDialog = new ProgressDialog(this);

        MyHelper.getInstance(getApplication());
        MyHelper.createTables();
        Settings.setApplication(getApplication());
        Settings.readSettings();
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent i = new Intent(this, LoadDataFromServer.class);
        i.putExtra("from","MainAct");
        i.putExtra("mode", Constants.DATALOADER_MODE.PRELOAD.name());
        i.putExtra("receiverTag", mReceiver);
        startService(i);


    }


    public void btnSettingsOnClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }



    @Override
    public void onBackPressed() {
        if (prevtime != 0) {
            curtime = System.currentTimeMillis();
            if ((curtime - prevtime) / 1000 <= 3) {
                finish();
                System.exit(0);
            } else prevtime = 0;
        }
        if (prevtime == 0) {
            Toast.makeText(this, "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
            prevtime = System.currentTimeMillis();
            return;
        }
    }


    public void btnDocListClick(View view) {
        Intent intent = new Intent(this, DocList_Activity.class);
        startActivity(intent);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode == 1)
        {
            progressDialog.dismiss();
            showToast("Предварительная загрузка завершена");

            return;
        }

        String message = resultData.getString("Message");

        if(message != null)
        {
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Предзагрузка");
            progressDialog.setMessage(message);
            if(!progressDialog.isShowing())
                progressDialog.show();
        }


    }

    void showToast(String s){Toast.makeText(this, s, Toast.LENGTH_SHORT).show();}

}
