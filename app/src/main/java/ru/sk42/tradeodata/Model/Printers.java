package ru.sk42.tradeodata.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by PostRaw on 04.03.2016.
 */

public class Printers {

    @JsonProperty
    private ArrayList<String> Printers;

    public Printers() {
        Printers = new ArrayList<String>();
    }

    public ArrayList<String> getPrinters() {
        return Printers;
    }

    public void setPrinters(ArrayList<String> printers) {
        Printers = printers;
    }

    public CharSequence[] getCharSequence() {
        if (Printers == null) {
            CharSequence[] cs = new CharSequence[1];
            String asd = "нет данных";
            cs[0] = asd;
            return cs;
        } else {
            final CharSequence[] charSequenceItems = Printers.toArray(new CharSequence[Printers.size()]);
            return charSequenceItems;
        }
    }
}
