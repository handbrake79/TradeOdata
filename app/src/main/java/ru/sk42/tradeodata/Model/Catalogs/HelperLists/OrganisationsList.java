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
import java.util.List;

import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.Catalogs.Organisation;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by test on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationsList {


    @JsonProperty("value")
    private Collection<Organisation> values;

    public OrganisationsList() {
        values = new ArrayList<Organisation>();
    }

    public Collection<Organisation> getValues() {
        return values;
    }

    public void setValues(Collection<Organisation> values) {
        this.values = values;
    }

    public void save() {
        try {
            Helper.getOrganisationDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
