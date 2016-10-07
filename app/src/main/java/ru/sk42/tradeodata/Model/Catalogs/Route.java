package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;

/**
 * Created by я on 29.09.2016.
 */
@DatabaseTable(tableName = "routes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route extends CDO
{

    @DatabaseField(id = true)
    @JsonProperty("Ref_Key")
    private String ref_Key;
    @DatabaseField
    @JsonProperty
    private String Code;
    @DatabaseField
    @JsonProperty
    private String Description;

    public Route() {
    }

    public Route(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    @Override
    public String getRef_Key() { return this.ref_Key; }

    @Override
    public void setRef_Key(String Ref_Key) { this.ref_Key = Ref_Key; }

    public String getCode() { return this.Code; }

    public void setCode(String Code) { this.Code = Code; }

    public String getDescription() { return this.Description; }

    public void setDescription(String Description) { this.Description = Description; }

    @Override
    public void save() throws SQLException {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_МаршрутыАТ";
    }

    @Override
    public String getRetroFilterString() {
        return "";
    }

    @Override
    public Dao<Route, Object> getDao() {
        return MyHelper.getRouteDao();
    }
}