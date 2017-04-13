package ru.sk42.tradeodata.Helpers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by хрюн моржов on 26.10.2016.
 */
public class Uttils {
    public final static DateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    public final static DateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");
    public final static DateFormat DATE_FORMAT_1C = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    static final String TAG = "UTTILS";

    public static boolean isShippingDateValid(Calendar shippingDate) {
        Calendar endOfYesterday = getEndOfYesterday();
        //если дата доставки больше или равна текущей дате, это годная дата
        return shippingDate.after(endOfYesterday);
    }

    public static Calendar getEndOfYesterday() {
        Calendar yesterday = GregorianCalendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 23);
        yesterday.set(Calendar.MINUTE, 59);
        yesterday.set(Calendar.SECOND, 59);
        return yesterday;
    }

    public static boolean isShippingTimeValid(Date shippingTimeFrom, Date shippingTimeTo) {
        return shippingTimeFrom.getTime() <= shippingTimeTo.getTime();
    }

    public static boolean dateIsNotSet(Calendar mShippingDate) {
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.YEAR, 1);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.DATE, 2);
        return c.after(mShippingDate);
    }

    public static String formatDoubleToMoney(double d) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return String.valueOf(df.format(d));
    }

    public static String formatDoubleToQty(double d) {
        DecimalFormat df = new DecimalFormat("#0.000");
        return String.valueOf(df.format(d));
    }

    public static boolean isPredefined(Product product) {
        return product.getRef_Key().equals(Constants.SHIPPING_SERVICE_GUID)
                || product.getRef_Key().equals(Constants.UNLOAD_SERVICE_GUID);
    }

    public static String formatInt(int i) {
        DecimalFormat df = new DecimalFormat("#0");
        return String.valueOf(df.format(i));
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isUserSet() {
        if (Settings.getCurrentUserStatic() == null) {
            return false;
        }
        return !Settings.getCurrentUserStatic().getRef_Key().equals(Constants.ZERO_GUID);

    }

    public static String formatDate(Calendar date) {
        return Uttils.DATE_FORMATTER.format(date.getTime()).toString();
    }


    private static class checkServerAvailable extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... params) {
            String addr = params[0];
            addr = addr.replaceAll("[^\\d.]", "");
            boolean connected = false;
            try {
                connected = InetAddress.getByName(addr).isReachable(2000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return connected;
        }

        protected void onPostExecute(Boolean result) {
            // here you have the result
        }
    }
}
