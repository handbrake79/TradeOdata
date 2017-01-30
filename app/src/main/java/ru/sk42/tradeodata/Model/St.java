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

    public static String getInfoBaseName() {
        if (InfoBaseName.isEmpty())
            return "ut836";
        return InfoBaseName;
    }


    public static Application getApp() {
        return application;
    }

    public static void setApplication(Application mapplication) {
        application = mapplication;
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    public static void readSettings() {
        SharedPreferences preferences = getPreferences();
        ServerAddress = preferences.getString("ServerAddress", "");
        ServerUser = preferences.getString("ServerUser", "");
        ServerPassword = preferences.getString("ServerPassword", "");
        Printer = preferences.getString("Printer", "");
        InfoBaseName = preferences.getString("InfoBaseName", "");
        String s = (preferences.getString("startDate", ""));
        SimpleDateFormat df = getDateFormat();
        if (s.equals("")) {
            Date d = Calendar.getInstance().getTime();
            d = getStartOfDay(d);
            s = df.format(d);
        }
        try {
            startDate = getStartOfDay(df.parse(s));
        } catch (ParseException e) {
            startDate = getStartOfDay(Calendar.getInstance().getTime());
        }

        Constants.SHIPPING_SERVICE = Product.getObject(Product.class, Constants.SHIPPING_GUID);
        Constants.UNLOAD_SERVICE = Product.getObject(Product.class, Constants.UNLOAD_GUID);

    }


    static SharedPreferences getPreferences() {
        String name = application.getBaseContext().getPackageName() + "_preferences";
        SharedPreferences preferences = application.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences;
    }

    public static Calendar getStartDate() {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        return c;
    }

    public static void setStartDate(Date mDate) {
        St.startDate = mDate;
        SharedPreferences preferences = getPreferences();
        preferences.edit().putString("startDate", getDateFormat().format(startDate)).commit();
    }

    public static Date getStartOfDay(Date mDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900 + mDate.getYear(), mDate.getMonth(), mDate.getDate(), 0, 0, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900 + date.getYear(), date.getMonth(), date.getDate(), 23, 59, 59);
        return calendar.getTime();
    }


}
