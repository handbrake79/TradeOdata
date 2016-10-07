package ru.sk42.tradeodata.Activities.Documents_List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.sk42.tradeodata.Model.Settings;

/**
 * Created by юзер on 24.06.2016.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DocList_Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (DocList_Activity) getActivity();
        Date date = activity.getStartDate();
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(date.getYear() + 1900, date.getMonth(), date.getDate(), 0, 0, 0);

        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        Date date = new Date();
        date.setDate(dd);
        date.setMonth(mm);
        date.setYear(yy - 1900);
        date = Settings.getStartOfDay(date);
        activity.setStartDate(date);
        activity.requestDocList();
    }

}