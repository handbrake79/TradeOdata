package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.query.Exists;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by PostRaw on 19.04.2016.
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
    private float weight;

    @JsonProperty("Объем")
    @DatabaseField
    private float volume;

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

    public Unit(){}

    public Unit(String s) {
        this.ref_Key = s;
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return ref_Key.equals(Constants.ZERO_GUID);
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
