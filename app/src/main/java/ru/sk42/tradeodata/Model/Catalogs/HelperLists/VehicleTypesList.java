package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;

/**
 * Created by я on 29.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleTypesList {
    public VehicleTypesList() {
    }

    @JsonProperty("value")
    Collection<VehicleType> values;

    public Collection<VehicleType> getValues() {
        return values;
    }

    public void setValues(Collection<VehicleType> values) {
        this.values = values;
    }

    public void save() {
        try {
            Dao<VehicleType, Object> dao = Helper.getInstance().getDao(VehicleType.class);
            dao.delete(dao.queryForAll());
            dao.create(values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
