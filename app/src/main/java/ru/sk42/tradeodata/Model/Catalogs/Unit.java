package ru.sk42.tradeodata.Model.Catalogs;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Helpers.Helper;

/**
 * Created by test on 19.04.2016.
 */

@DatabaseTable(tableName = "Units")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit extends CDO {

    @JsonProperty("Ref_Key")
    @DatabaseField(id = true)
    private String ref_Key;

    public Unit(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public Unit(){}

    @JsonProperty("Description")
    @DatabaseField
    private String description;


    static String TAG = "*** productUnit";
    public String getDescription() {

        return description;
    }

    public void setDescription(String mdescription) {
        this.description = mdescription;
    }

    public String getRef_Key() {
        return ref_Key;
    }


    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public void save() {
        try {
            Helper.getInstance().getDao(Unit.class).create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        if (ref_Key.equals(Constants.NULL_GUID))
            return true;
        else
            return false;
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_ЕдиницыИзмерения";
    }

    @Override
    public void setForeignObjects() {

    }

    public String toString() {
        return description;
    }

}
