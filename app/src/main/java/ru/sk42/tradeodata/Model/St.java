package ru.sk42.tradeodata.Model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.User;

/**
 * Created by PostRaw on 14.03.2016.
 */
public class St {
    private static Application application;
    private static boolean initComplete;


    public static Application getApp() {
        return application;
    }

    public static void setApplication(Application mapplication) {
        application = mapplication;
    }


    public static void setInitComplete() {
        initComplete = true;
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

    }

    public static boolean getInitComplete() {
        return initComplete;
    }
}
