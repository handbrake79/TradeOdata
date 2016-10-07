package ru.sk42.tradeodata.Activities.Documents_List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sk42.tradeodata.Activities.Document.DocumentActivity;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Documents.DocSaleList;
import ru.sk42.tradeodata.Model.Settings;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.RetroRequests.DocsRequest;
import ru.sk42.tradeodata.RetroRequests.RetroConstants;
import ru.sk42.tradeodata.RetroRequests.ServiceGenerator;
import ru.sk42.tradeodata.Services.LoadDataAndSetObjectsService;
import ru.sk42.tradeodata.Services.MyResultReceiver;

public class DocList_Activity extends AppCompatActivity implements MyActivityFragmentInteractionInterface, MyResultReceiver.Receiver {

    private static final String TAG = "Doclist activity";
    Date startDate;
    public MyResultReceiver mReceiver;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ***OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documents__list_activity);

        if(Settings.getCurrentUser() == null) {
            showToast("Dыберите пользователя в настройках");
            this.finish();
        }

        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);

        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setStartDate(Settings.getStartDate());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                        .setAction("Изменить дату", new View.OnClickListener() {
                            @Override
                            public void onClick(View view1) {
                                selectDate();
                            }
                        }).show();

            }
        });


        selectDate();
    }

    private void selectDate() {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    void showListFragment() {

        DocListFragment fragment = DocListFragment.newInstance();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.doc_list__fragment, fragment, String.valueOf(R.id.doc_list__fragment));
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();

    }

    @Override
    public void onItemSelection(Object selectedObject) {
        //это может быть выбор документа из списка
        if (selectedObject instanceof DocSale) {

            Intent intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("mode", Constants.ModeExistingOrder);
            intent.putExtra("ref_Key", ((DocSale) selectedObject).getRef_Key());
            startActivity(intent);
        }
    }

    @Override
    public void onDetachFragment(Fragment fragment) {

    }

    @Override
    public void onRequestSuccess(Object obj) {
        Toast.makeText(this, "STRANGE THINGS GOING ON! " + obj.getClass().getName().toString(), Toast.LENGTH_LONG).show();
    }

    public Date getStartDate() {
        return startDate;
    }

    void setStartDate(Date startDate) {
        this.startDate = startDate;
        Settings.setStartDate(startDate);
        //requestDocList();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        progress.hide();
        showListFragment();
    }

    private void callDataLoaderService() {
        Intent i = new Intent(this, LoadDataAndSetObjectsService.class);
        i.putExtra("id", 0);
        i.putExtra("mode", Constants.DATALOADER_MODE.DOCLIST.name());
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from","DocList");
        startService(i);
    }


    public void requestDocList(){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        startDate = getStartDate();
        String d1 = fmt.format(startDate.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, 1);  // number of days to add
        String d2 = fmt.format(c.getTime());

        showProgress("Запрос к 1С", "Загружается список документов");
        String filter = "Date gt datetime'" + d1 + "' and Date lt datetime'" + d2 + "'" + " and Контрагент_Key eq guid'" + Constants.CUSTOMER_GUID + "'" + " and Ответственный_Key eq guid'" + Settings.getCurrentUser().getRef_Key() + "'";


        DocsRequest request = ServiceGenerator.createService(DocsRequest.class);
        Call<DocSaleList> callDocuments = request.call(RetroConstants.getMap(filter));
        progress.show();
        callDocuments.enqueue(new Callback<DocSaleList>() {
            @Override
            public void onResponse(Call<DocSaleList> call, Response<DocSaleList> response) {
                DocSaleList list = response.body();
                if(list == null) {
                    Log.wtf(TAG, "onResponse: DocSaleList is null", new Exception());
                    return;
                }
                showProgress("Persist to DB","Получено " + list.size() + " документов, сохраняем в БД.");

                Helper.getInstance().deleteDocSaleList();

                try {
                    list.save();
                }
                catch (Exception e){
                    Log.w(TAG, "onResponse: " + e.toString() );
                }

                callDataLoaderService();

            }

            @Override
            public void onFailure(Call<DocSaleList> call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.toString());

            }
        });



    }


    void showToast(String message){
        Log.d(TAG, "toast: "+ Constants.getCurrentTimeStamp() + " - " + message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    void showProgress(String title, String message) {
        Log.d(TAG, "progress: " + Constants.getCurrentTimeStamp() + " - " + message);
        //Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        //showRequestsCount();

//        progress.setIndeterminate(true);
        if(progress == null){
            progress = new ProgressDialog(this);
            progress.setIndeterminate(true);}
        progress.setTitle(title);
        progress.setMessage(message);
//        progress.show();
    }


}
