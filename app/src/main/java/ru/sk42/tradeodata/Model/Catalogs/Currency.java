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
 * Created by PostRaw on 14.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Currency")
public class Currency extends CDO {

    @JsonProperty
    @DatabaseField
    public String Description;

    @DatabaseField(id = true, columnName = "Ref_Key")
    @JsonProperty("Ref_Key")
    public String ref_Key;

    public Currency(String ref_Key) {
        this.setRef_Key(ref_Key);
    }

    public Currency() {
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
        return "Catalog_Валюты";
    }

    @Override
    public Dao<Currency, Object> getDao() {
        return MyHelper.getCurrencyDao();
    }

    @Override
    public String getRetroFilterString() {
        return "";
    }


    public boolean isEmpty() {
        return ref_Key.equals(Constants.ZERO_GUID);
    }

    @Override
    public void save() {

        try {
            MyHelper.getInstance().getDao(Currency.class).createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.getDescription().toString();
    }

    public static Currency newInstance() {
        return Currency.getObject(Currency.class, Constants.CURRENCY_GUID);
    }
}
