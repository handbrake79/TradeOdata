package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.Catalogs.Store;


@JsonIgnoreProperties(ignoreUnknown = true)

public class StoreList {


    @JsonProperty("value")
    private Collection<Store> values;

    public StoreList() {
    }



    public Collection<Store> getValues() {
        return values;
    }

    public void setValues(Collection<Store> values) {
        this.values = values;
    }

    public void save() {

        try {
            Helper.getStoreDao().create(this.getValues());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
