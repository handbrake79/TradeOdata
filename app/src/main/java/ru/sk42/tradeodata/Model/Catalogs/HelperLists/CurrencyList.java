package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.Catalogs.Currency;

/**
 * Created by test on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyList {


    @JsonProperty("value")
    private Collection<Currency> values;

    public CurrencyList() {}

    public void setValues(Collection<Currency> values) {
        this.values = values;
    }

    public Collection<Currency> getValues() {
        return values;
    }


    public void save() {
        try {
            Helper.getCurrencyDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
