package ru.sk42.tradeodata.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sk42.tradeodata.Activities.Documents_List.DocList_Activity;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.XML.WrapperXML_DocSale;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.SettingsOld;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.RetroRequests.PatchDocument;
import ru.sk42.tradeodata.Services.ServiceGenerator;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReciever;
import ru.sk42.tradeodata.XML.DateConverter;

public class MainActivity extends AppCompatActivity implements ServiceResultReciever.Receiver {

    ServiceResultReciever mReceiver;
    long prevtime, curtime;
    ProgressDialog progressDialog;

    String TAG = "*** MainAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        progressDialog = new ProgressDialog(this);

        MyHelper.getInstance(getApplication());

//        MyHelper.dropAndCreateTables();
        MyHelper.createTables();

        SettingsOld.setApplication(getApplication());
        SettingsOld.readSettings();
        mReceiver = new ServiceResultReciever(new Handler());
        mReceiver.setReceiver(this);
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra("from", "MainAct");
        i.putExtra("mode", Constants.DATALOADER_MODE.PRELOAD.name());
        i.putExtra("receiverTag", mReceiver);
        startService(i);

        //test();

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

        if (resultCode == 1) {
            progressDialog.dismiss();
            showToast("Предварительная загрузка завершена");

            return;
        }

        String message = resultData.getString("Message");

        if (message != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Предзагрузка");
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing())
                progressDialog.show();
        }


    }

    void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    void test(){

        Registry registry = new Registry();
        Strategy strategy = new RegistryStrategy(registry);
        DateConverter dateConverter = new DateConverter();
        try {
            registry.bind(Date.class, dateConverter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Persister persister = new Persister(strategy);
        StringWriter writer = new StringWriter();
        DocSale docSale = null;
        try {
            docSale = MyHelper.getDocSaleDao().queryForAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        docSale.setDate(GregorianCalendar.getInstance().getTime());
        //docSale.setRef_Key(Constants.ZERO_GUID);
        docSale.setNumber(String.valueOf(docSale.getDate().getTime()));
        docSale.setComment("Ебать ты высокий!");
        WrapperXML_DocSale XMLDocSaleWrapper = new WrapperXML_DocSale(docSale);



        try {
            persister.write(XMLDocSaleWrapper, writer);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

        String s = writer.toString();

        EditText e = (EditText) findViewById(R.id.edtLog);
        e.setText(s);
        //showToast(s);

        PatchDocument request = ServiceGenerator.createXMLService(PatchDocument.class);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), s);
        Call<ResponseBody> call = request.call(docSale.getRef_Key(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String location = response.headers().get("Location");
                if(location == null){
                    return;
                }
                int i = location.indexOf("guid'");
                if(i > 0){
                    String guid = location.substring(i + 5, location.length() - 3);
                    showToast(guid);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToast(t.toString());

            }
        });


    }
}
