package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Currency;

/**
 * Created by PostRaw on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyList {


    @JsonProperty("value")
    private Collection<Currency> values;

    public CurrencyList() {}

    public Collection<Currency> getValues() {
        return values;
    }

    public void setValues(Collection<Currency> values) {
        this.values = values;
    }

    public void save() {
        try {
            MyHelper.getCurrencyDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
