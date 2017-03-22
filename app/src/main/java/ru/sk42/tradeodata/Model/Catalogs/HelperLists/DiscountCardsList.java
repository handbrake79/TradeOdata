package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;

/**
 * Created by PostRaw on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountCardsList {


    @JsonProperty("value")
    private Collection<DiscountCard> values;

    public DiscountCardsList(){
    }


    public Collection<DiscountCard> getValues() {
        return values;
    }

    public void setValues(Collection<DiscountCard> values) {
        this.values = values;
    }

    public void save() {
        try {
            MyHelper.getDiscountCardDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return values.size();
    }
}
