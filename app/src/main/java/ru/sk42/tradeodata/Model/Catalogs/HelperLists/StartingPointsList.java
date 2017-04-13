package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;

/**
 * Created by я on 29.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartingPointsList implements DataList {
    @JsonProperty("value")
    Collection<StartingPoint> values;

    public StartingPointsList() {
    }

    public Collection<StartingPoint> getValues() {
        return values;
    }

    public void setValues(Collection<StartingPoint> values) {
        this.values = values;
    }

    public void save() {
        try {
            Dao<StartingPoint, Object> dao = MyHelper.getStartingPointDao();
            dao.delete(dao.queryForAll());
            dao.create(values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
