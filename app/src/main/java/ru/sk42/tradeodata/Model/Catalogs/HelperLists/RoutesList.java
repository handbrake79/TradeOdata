package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Route;

/**
 * Created by я on 29.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutesList implements DataList {
    @JsonProperty("value")
    Collection<Route> values;

    public RoutesList() {
    }

    public Collection<Route> getValues() {
        return values;
    }

    public void setValues(Collection<Route> values) {
        this.values = values;
    }

    public void save() {
        try {
            Dao<Route, Object> dao = MyHelper.getRouteDao();
            dao.delete(dao.queryForAll());
            dao.create(values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
