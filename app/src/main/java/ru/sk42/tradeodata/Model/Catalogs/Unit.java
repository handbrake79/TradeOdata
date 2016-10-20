package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by test on 19.04.2016.
 */

@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit extends CDO {

    static String TAG = "*** productUnit";

    @JsonProperty("Ref_Key")
    @DatabaseField(id = true)
    private String ref_Key;

    @JsonProperty("Description")
    @DatabaseField
    private String description;

    @JsonProperty("Вес")
    @DatabaseField
    float weight;

    @JsonProperty("Объем")
    @DatabaseField
    float volume;

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public Unit(String ref_Key) {
        this.ref_Key = ref_Key;
    }


    public Unit(){}

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
            MyHelper.getInstance().getDao(Unit.class).create(this);
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
    public String getRetroFilterString() {
        return "";
    }

    @Override
    public Dao<Unit, Object> getDao() {
        return MyHelper.getUnitDao();
    }

    public String toString() {
        return description;
    }

}
