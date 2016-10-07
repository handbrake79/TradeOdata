package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Unit;

/**
 * Created by юзер on 30.05.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitsList {


    @JsonProperty("value")
    private Collection<Unit> values;

    public UnitsList() {
    }

    public Collection<Unit> getValues() {
        return values;
    }

    public void setValues(Collection<Unit> values) {
        this.values = values;
    }

    public void save() {
        try {
            MyHelper.getUnitDao().create(this.getValues());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
