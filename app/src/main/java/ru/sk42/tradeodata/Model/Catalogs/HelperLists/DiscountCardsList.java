package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;

/**
 * Created by test on 04.03.2016.
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
            Helper.getDiscountCardDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
