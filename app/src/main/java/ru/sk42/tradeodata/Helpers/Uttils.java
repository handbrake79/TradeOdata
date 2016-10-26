package ru.sk42.tradeodata.Helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by хрюн моржов on 26.10.2016.
 */
public class Uttils {
    public final static DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
    public final static DateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");

    public static boolean isShippingDateValid(Calendar shippingDate) {
        Calendar yesterday = getYesterday();
        if(shippingDate.after(yesterday)){
            //если дата доставки больше или равна текущей дате, это годная дата
            return true;
        }else {
            return false;
        }
    }

    public static Calendar getYesterday(){
        Calendar yesterday = GregorianCalendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY,23);
        yesterday.set(Calendar.MINUTE,59);
        yesterday.set(Calendar.SECOND,59);
        return yesterday;
    }

    public static boolean isShippingTimeValid(Date shippingTimeFrom, Date shippingTimeTo) {
        if(shippingTimeFrom.getTime() <= shippingTimeTo.getTime()){
            return true;
        }
        else {
            return false;
        }
    }
}
