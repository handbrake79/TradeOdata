package ru.sk42.tradeodata.Model.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;

/**
 * Created by PostRaw on 14.04.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocSaleList {

    private static final String TAG = "DocSaleList class";
    @JsonProperty("value")
    private Collection<DocSale> values;


    public DocSaleList() {
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
                List<DocSale> list = MyHelper.getDocSaleDao().queryForAll();
                docSale.save();

            }

    }


    public Integer size() {
        return values.size();
    }
}
