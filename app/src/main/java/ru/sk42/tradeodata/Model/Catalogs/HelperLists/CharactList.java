package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Charact;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CharactList implements DataList {

    @JsonProperty("value")
    private Collection<Charact> values;

    public CharactList() {
    }


    public Collection<Charact> getValues() {
        return values;
    }

    public void setValues(Collection<Charact> values) {
        this.values = values;
    }


    public void save() {
        try {
            MyHelper.getCharactDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
