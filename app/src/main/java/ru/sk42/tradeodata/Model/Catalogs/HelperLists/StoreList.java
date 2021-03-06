package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Store;


@JsonIgnoreProperties(ignoreUnknown = true)

public class StoreList implements DataList {


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
            MyHelper.getStoreDao().create(this.getValues());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
