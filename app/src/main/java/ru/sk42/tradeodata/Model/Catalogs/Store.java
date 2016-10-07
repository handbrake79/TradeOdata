package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Model.CDO;


/**
 * Created by test on 31.03.2016.
 */
@DatabaseTable(tableName = "Stores")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Store extends CDO {
    public Store(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    @JsonProperty("Ref_Key")
    @DatabaseField(id = true)
    private String ref_Key;

    @JsonProperty("Description")
    @DatabaseField
    private String description;

    public Store() {
    }

    @Override
    public void save() throws SQLException {

    }

    public String getRef_Key() {
        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_Склады";
    }

    @Override
    public void setForeignObjects() {

    }
}
