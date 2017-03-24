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
    }

    public static boolean getInitComplete() {
        return initComplete;
    }
}
