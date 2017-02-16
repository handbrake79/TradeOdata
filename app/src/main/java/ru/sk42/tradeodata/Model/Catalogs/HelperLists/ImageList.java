package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.ImageProduct;


@JsonIgnoreProperties(ignoreUnknown = true)

public class ImageList {


    @JsonProperty("value")
    private Collection<ImageProduct> values;

    public ImageList() {
    }



    public Collection<ImageProduct> getValues() {
        return values;
    }

    public void setValues(Collection<ImageProduct> values) {
        this.values = values;
    }

    public void save() {

        try {
            MyHelper.getProductImageDao().create(this.getValues());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Integer size() {
        return values.size();
    }


}
