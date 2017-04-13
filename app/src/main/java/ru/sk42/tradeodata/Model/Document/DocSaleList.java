package ru.sk42.tradeodata.Model.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;

/**
 * Created by PostRaw on 14.04.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocSaleList {

    private static final String TAG = "DocSaleList class";
    @JsonProperty("value")
    private Collection<DocSale> values;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(generatedId = true)
    private int id;


    public DocSaleList() {
    }

    public void setForeignObjects() {
        for (DocSale docSale :
                this.getValues()) {
            docSale.setForeignObjects();
        }

    }

    public static DocSaleList getList() {
        DocSaleList list = new DocSaleList();
        try {
            list.setValues(MyHelper.getDocSaleDao().queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Collection<DocSale> getValues() {
        return values;
    }

    public void setValues(Collection<DocSale> values) {
        this.values = values;
    }

    public ArrayList<DocSale> getArrayList() {
        if (values == null) {
            return new ArrayList();
        } else
            return new ArrayList(values);
    }

    public void save() throws SQLException {

        for (DocSale docSale : this.getValues()
                ) {
            docSale.save();

        }

    }


    public Integer size() {
        return values.size();
    }
}
