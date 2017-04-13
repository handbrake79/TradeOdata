package ru.sk42.tradeodata.Activities.Documents_List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.sk42.tradeodata.Activities.Document.DocumentActivity;
import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.LoadingFragment;
import ru.sk42.tradeodata.Activities.Settings.SettingsActivity;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.DocSaleList;
import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;

import static ru.sk42.tradeodata.R.id.doclist__nav_view;

public class DocList_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InteractionInterface, ServiceResultReceiver.ServiceResultReceiverInterface {

    private static final String TAG = "***Doclist activity";

    long mPrevTime, curtime;

    public ServiceResultReceiver mReceiver;

    Calendar startDate;

    Toolbar mToolbar;

    ActionBarDrawerToggle mDrawerToggle;

    RecyclerView mRecyclerView;

    DocList_Adapter mAdapter;
    DocSaleList doc_list;

    DrawerLayout mDrawerLayout;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ***OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doclist__activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        startDate = GregorianCalendar.getInstance();
        startDate.setTime(Uttils.getStartOfDay(startDate.getTime()));

        setToolbar();
        setDrawer();

        if (MyHelper.getDocumentCountOnDate(startDate.getTime()) == 0) {
            requestDocList();
        } else {
            updateTitle();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.doclist__fab);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        fab.startAnimation(animation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocList_Activity.this, DocumentActivity.class);
                intent.putExtra(Constants.MODE_LABEL, Constants.ModeNewOrder);
                startActivityForResult(intent, 0);
                mAdapter.notifyDataSetChanged();
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
        }


    }

    private void updateTitle() {
        String docsCount = "?";
        try {
            docsCount = String.valueOf(MyHelper.getDocSaleDao().countOf());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mToolbar.setTitle("Реализации " + " " + Uttils.formatDate(startDate) + " (" + docsCount + ")");
        mToolbar.setSubtitle(Settings.getCurrentUserStatic().getDescription());

    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.doclist__toolbar);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

    }


    private void setDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.doclist__drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        navigationView = (NavigationView) findViewById(doclist__nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (mPrevTime != 0) {
            curtime = System.currentTimeMillis();
            if ((curtime - mPrevTime) / 1000 <= 3) {
                finish();
            } else mPrevTime = 0;
        }
        if (mPrevTime == 0) {
            showSnack("Нажмите еще раз для выхода");
            mPrevTime = System.currentTimeMillis();
            return;
        }
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
                requestDocList();
            }
        }
        int y, m, d;
        y = startDate.get(Calendar.YEAR);
        m = startDate.get(Calendar.MONTH);
        d = startDate.get(Calendar.DATE);

        CalendarDatePickerDialogFragment datePickerDialog = new CalendarDatePickerDialogFragment()
                .setPreselectedDate(y, m, d)
                .setOnDateSetListener(new OnDateSetListener())
                .setFirstDayOfWeek(Calendar.MONDAY)
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
            intent.putExtra(Constants.MODE_LABEL, Constants.ModeExistingOrder);
            intent.putExtra(Constants.REF_KEY_LABEL, ((DocSale) selectedObject).getRef_Key());
            intent.putExtra(Constants.ID, ((DocSale) selectedObject).getId());
            startActivityForResult(intent, 0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            //закрылся документ
            setAdapter();
        }
    }

    @Override
    public void onRequestSuccess(Object obj) {
        Toast.makeText(this, "STRANGE THINGS GOING ON! " + obj.getClass().getName().toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onReceiveResultFromService(int resultCode, Bundle resultData) {
        if (resultCode == Constants.FEEDBACK) {
            String message = resultData.getString(Constants.MESSAGE_LABEL);
            showLoading(message);
            return;
        }
        if (resultCode == Constants.REQUESTS.LOAD_DOCUMENTS.ordinal()) {
            hideLoading();
            updateTitle();
            setAdapter();
        }
    }


    public void requestDocList() {
        showLoading("Загрузка документов пользователя");

        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra("StartDate", startDate.getTimeInMillis());
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.LOAD_DOCUMENTS.ordinal());
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "DocList");
        startService(i);

    }



    void showSnack(String s) {
        Snackbar.make(getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.doclist__item_settings:
                showSettingsActivity();
                break;
            case R.id.doclist__item_exit:
                finish();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


    private void showLoading(String... params) {
        LoadingFragment fragment = LoadingFragment.newInstance(params);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.doclist__coord, fragment, "loading")
                .addToBackStack("loading")
                .commit();

    }


    private void hideLoading() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("loading");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        //getSupportFragmentManager().popBackStack();
    }


}
