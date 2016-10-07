package ru.sk42.tradeodata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import ru.sk42.tradeodata.Helpers.Helper;

/**
 * Created by я on 06.10.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestList {
    public TestList(){

    }

    @JsonProperty("value")
    Collection<Tttest> values;

    public Collection<Tttest> getValues() {
        return values;
    }

    public TestList setValues(Collection<Tttest> values) {
        this.values = values;
        return this;
    }

    public void save() {
        try {
            Helper.getInstance().getDao(Tttest.class).create(values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
