package ru.sk42.tradeodata.Model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.User;

/**
 * Created by test on 14.03.2016.
 */
public class SettingsOld {
    private static String InfoBaseName;
    private static String ServerAddress;
    private static String ServerPassword;
    private static String Printer;
    private static String ServerUser;
    private static Date startDate;
    private static User currentUser;
    private static Application application;

    public static User getCurrentUser() {
        return currentUser;
    }

    static void setCurrentUser(String desrc) {
        if (desrc == null) return;
        if (desrc.isEmpty()) return;

        try {
            List<User> list = MyHelper.getInstance().getDao(User.class).queryForEq("description", desrc);
            if (list.size() > 0) {
                currentUser = list.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getInfoBaseName() {
        if (InfoBaseName.isEmpty())
            return "ut836";
        return InfoBaseName;
    }

    public static String getServerAddress() {

        if (ServerAddress.isEmpty())
            return "http://192.168.0.171/";
        return ServerAddress;
    }

    public static String getServerUser() {
        if (ServerUser.isEmpty())
            return "Коган";
        return ServerUser;
    }

    public static String getServerPassword() {
        if (ServerPassword.isEmpty())
            return "";
        return ServerPassword;
    }

    public static String getPrinter() {
        return Printer;
    }

    public static Application getApplication() {
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

        renewCurrentUser();
    }

    public static void renewCurrentUser(){
        SharedPreferences preferences = getPreferences();
        setCurrentUser(preferences.getString("currentUserKey",""));
    }

    static SharedPreferences getPreferences() {
        String name = application.getBaseContext().getPackageName() + "_preferences";
        SharedPreferences preferences = application.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences;
    }

    public static Date getStartDate() {
        return startDate;
    }

    public static void setStartDate(Date mDate) {
        SettingsOld.startDate = mDate;
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
