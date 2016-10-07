package ru.sk42.tradeodata.Model.Documents;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.CDO;

/**
 * Created by test on 14.04.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocSaleList extends CDO {

    private static final String TAG = "DocSaleList class";

    public DocSaleList() {
    }


    @JsonProperty("value")
    private Collection<DocSale> values;

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

    @Override
    public void save() throws SQLException {

            for (DocSale doc : this.getValues()
                    ) {
                doc.save();

            }

    }

    @Override
    public void setRef_Key(String s) {

    }

    @Override
    public String getRef_Key() {
        return null;
    }

    @Override
    public void setDescription(String s) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return null;
    }

    public void setForeignObjects() {
        for (DocSale docSale : values) {
            docSale.setForeignObjectsInHeader();
        }
    }


    public Integer size() {
        return values.size();
    }


    public static DocSaleList getList() {
        DocSaleList list = new DocSaleList();
        try {
            list.setValues((Collection<DocSale>) Helper.getDocSaleDao().queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
