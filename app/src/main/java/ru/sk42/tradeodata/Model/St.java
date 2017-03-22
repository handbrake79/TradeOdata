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
    private static String InfoBaseName;
    private static String ServerAddress;
    private static String ServerPassword;
    private static String Printer;
    private static String ServerUser;
    private static Date startDate;
    private static Application application;

    public static Application getApp() {
        return application;
    }

    public static void setApplication(Application mapplication) {
        application = mapplication;
    }


}
