package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.InformationRegisters.ShippingRate;

/**
 * Created by PostRaw on 18.03.2016.
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingRatesList {


    @com.fasterxml.jackson.annotation.JsonProperty("value")
    private Collection<ShippingRate> values;

    public ShippingRatesList() {
    }


    public Collection<ShippingRate> getValues() {
        return values;
    }

    public void setValues(Collection<ShippingRate> values) {
        this.values = values;
    }


    public void save() {
        try {
            Iterator<ShippingRate> it = getValues().iterator();
            while (it.hasNext()) {
                MyHelper.getInstance().getDao(ShippingRate.class).createOrUpdate(it.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
