package ru.sk42.tradeodata.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by хрюн моржов on 30.01.2017.
 */

@DatabaseTable
public class Printer {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String printerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }
}
