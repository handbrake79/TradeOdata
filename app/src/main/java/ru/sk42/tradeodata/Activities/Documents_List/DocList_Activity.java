package ru.sk42.tradeodata.Activities.Documents_List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.sk42.tradeodata.Activities.Document.DocumentActivity;
import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.DocSaleList;
import ru.sk42.tradeodata.Model.SettingsOld;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.LoadDataFromServer;
import ru.sk42.tradeodata.Services.ServiceResultReciever;

public class DocList_Activity extends AppCompatActivity implements InteractionInterface, ServiceResultReciever.Receiver {

    private static final String TAG = "***Doclist activity";

    public ServiceResultReciever mReceiver;

    Calendar startDate = GregorianCalendar.getInstance();

    ProgressDialog progress;

    RecyclerView mRecyclerView;

    DocList_Adapter mAdapter;
    DocSaleList doc_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ***OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documents__list_activity);

        if (SettingsOld.getCurrentUser() == null) {
            showToast("Dыберите пользователя в настройках");
            this.finish();
        }

        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);

        mReceiver = new ServiceResultReciever(new Handler());
        mReceiver.setReceiver(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startDate = SettingsOld.getStartDate();

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


        View rvView = findViewById(R.id.rvDocList);

        //create RecyclerView
        if (rvView instanceof RecyclerView) {
            Context context = rvView.getContext();
            mRecyclerView = (RecyclerView) rvView;
            mRecyclerView.setSelected(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            setAdapter();
            setActionBarTitle();

        }
//        selectDate();
    }

    private void setActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        String title = Uttils.DATE_FORMATTER.format(startDate.getTime());
        if (doc_list != null)
            title += " документов " + doc_list.size().toString() + "";
        else
            title += " документов еще нет";
        actionBar.setTitle(title);

    }

    private void setAdapter() {
        mAdapter = new DocList_Adapter(new ArrayList<DocSale>(), this);
        doc_list = DocSaleList.getList();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmValues(doc_list.getArrayList());
        mAdapter.notifyDataSetChanged();
    }


    private void selectDate() {
        class OnDateSetListener implements CalendarDatePickerDialogFragment.OnDateSetListener {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                startDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                SettingsOld.setStartDate(startDate.getTime());
                requestDocList();
            }
        }
        int y, m, d;
        y = startDate.get(Calendar.YEAR);
        m = startDate.get(Calendar.MONTH);
        d = startDate.get(Calendar.DATE);

        CalendarDatePickerDialogFragment datePickerDialog = new CalendarDatePickerDialogFragment()
                .setPreselectedDate(y, m, d)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setOnDateSetListener(new OnDateSetListener())
                .setDoneText("Выбрать")
                .setCancelText("Отмена")
                .setThemeLight();
        datePickerDialog.show(getSupportFragmentManager(), "date_picker_dialog_fragment");
    }


    @Override
    public void onItemSelected(Object selectedObject) {
        //это может быть выбор документа из списка
        if (selectedObject instanceof DocSale) {

            Intent intent = new Intent(this, DocumentActivity.class);
            intent.putExtra("mode", Constants.ModeExistingOrder);
            intent.putExtra("ref_Key", ((DocSale) selectedObject).getRef_Key());
            startActivityForResult(intent, 0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            //закрылся документ
            setAdapter();
        }
    }

    @Override
    public void onRequestSuccess(Object obj) {
        Toast.makeText(this, "STRANGE THINGS GOING ON! " + obj.getClass().getName().toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 1) {
            progress.hide();
            setAdapter();
        }
        if (resultCode == 0) {
            String message = resultData.getString("Message");
            showProgress(message);
        }
    }


    public void requestDocList() {

        Intent i = new Intent(this, LoadDataFromServer.class);
        i.putExtra("StartDate", startDate.getTimeInMillis());
        i.putExtra("mode", Constants.DATALOADER_MODE.REQUEST_DOCUMENTS.name());
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "DocList");
        startService(i);

    }


    void showToast(String message) {
        Log.d(TAG, "toast: " + Constants.getCurrentTimeStamp() + " - " + message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    void showProgress(String message) {
        Log.d(TAG, "progress: " + Constants.getCurrentTimeStamp() + " - " + message);
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setIndeterminate(true);
        }
        progress.setTitle(Uttils.DATE_FORMATTER.format(startDate.getTime()).toString());
        progress.setMessage(message);
        progress.show();
    }

}
