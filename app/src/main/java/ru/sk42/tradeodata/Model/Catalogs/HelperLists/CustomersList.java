package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Customer;

/**
 * Created by PostRaw on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomersList implements DataList {


    @JsonProperty("value")
    private Collection<Customer> values;

    public CustomersList() {
        values = new ArrayList<Customer>();
    }


    public Collection<Customer> getValues() {
        return values;
    }

    public void setValues(Collection<Customer> values) {
        this.values = values;
    }

    public void save() {
        try {
            MyHelper.getCustomerDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
