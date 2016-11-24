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
import ru.sk42.tradeodata.RetroRequests.RetroConstants;


/**
 * Created by PostRaw on 14.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Contracts")
public class Contract extends CDO {

    @JsonProperty
    @DatabaseField
    private String Description;

    @DatabaseField(id = true, columnName = "Ref_Key")
    @JsonProperty("Ref_Key")
    private String ref_Key;

    public Contract(String ref_Key) {
        this.setRef_Key(ref_Key);
    }

    public Contract() {
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
        return "Catalog_ДоговорыКонтрагентов";
    }

    @Override
    public String getRetroFilterString() {
        return RetroConstants.FILTERS.CONTRACT;
    }

    @Override
    public Dao<Contract, Object> getDao() {
        return MyHelper.getContractDao();
    }


    public boolean isEmpty() {
        return ref_Key.equals(Constants.ZERO_GUID);
    }

    @Override
    public void save() {

        try {
            MyHelper.getInstance().getDao(Contract.class).createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.getDescription().toString();
    }


    public static Contract newInstance() {
        return Contract.getObject(Contract.class, Constants.CONTRACT_GUID);
    }
}
