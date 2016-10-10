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
 * Created by test on 14.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable
public class Organisation extends CDO {
    @JsonProperty
    @DatabaseField
    private String Description;
    @DatabaseField(id = true, columnName = "ref_Key")
    @JsonProperty("Ref_Key")
    private String ref_Key;

    public Organisation(String ref_Key) {
        this.setRef_Key(ref_Key);
    }

    public Organisation() {
    }



    public String getDescription() {
        return Description;
    }

    @Override
    public void setDescription(String description) {
        Description = description;
    }

    public String getRef_Key() {
        return ref_Key;
    }

    @Override
    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public String getMaintainedTableName() {
        return "Catalog_Организации";
    }

    @Override
    public String getRetroFilterString() {
        return "Ref_Key eq guid'" + Constants.ORGANISATION_GUID + "'";
    }

    @Override
    public Dao<Organisation, Object> getDao() {
        return MyHelper.getOrganisationDao();
    }

    public boolean isEmpty() {
        return ref_Key.equals(Constants.NULL_GUID);
    }

    @Override
    public void save() {

        try {
            MyHelper.getInstance().getDao(Organisation.class).createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.getDescription().toString();
    }

}
