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
@DatabaseTable(tableName = "Customers")
public class Customer extends CDO {
    @DatabaseField(id = true)
    @JsonProperty("Ref_Key")
    private String ref_Key;

    @JsonProperty("Description")
    @DatabaseField
    private String Description;

    public Customer(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public Customer() {
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
        return "Catalog_Контрагенты";
    }

    @Override
    public String getRetroFilterString() {
        return RetroConstants.FILTERS.CUSTOMER;
    }


    @Override
    public Dao<Customer, Object> getDao() {
        return MyHelper.getCustomerDao();
    }

    public boolean isEmpty() {
        return ref_Key.equals(Constants.ZERO_GUID);
    }

    @Override
    public void save() {

        try {
            MyHelper.getInstance().getDao(Customer.class).create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.getDescription().toString();
    }

}
