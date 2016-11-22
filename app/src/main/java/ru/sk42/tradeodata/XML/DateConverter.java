package ru.sk42.tradeodata.XML;

import android.util.Log;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.text.ParseException;
import java.util.Date;

import ru.sk42.tradeodata.Helpers.Uttils;

/**
 * Created by хрюн моржов on 18.11.2016.
 */

public class DateConverter implements Converter<Date> {
    private static final String TAG = "xml conv ***";

    @Override
    public Date read(InputNode node)  {
        String value = node.getAttribute("value").toString();
        Log.d(TAG, "read: date" + value);
        try {
            return Uttils.DATE_FORMAT_1C.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void write(OutputNode node, Date value)  {
        String formattedString = Uttils.DATE_FORMAT_1C.format(value);
        Log.d(TAG, "write: formatted date = " + formattedString);
        node.setValue(formattedString);
    }
}
