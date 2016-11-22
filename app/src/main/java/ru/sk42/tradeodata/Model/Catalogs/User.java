package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.RetroRequests.RetroConstants;


/**
 * Created by PostRaw on 14.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Users")
public class User extends CDO {

    @JsonProperty("Description")
    @DatabaseField
    private String description;

    @DatabaseField(id = true)
    @com.fasterxml.jackson.annotation.JsonProperty("Ref_Key")
    private String ref_Key;

    public User(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public User() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isEmpty() {
        return ref_Key.equals(Constants.NULL_GUID);
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_Пользователи";
    }

    @Override
    public String getRetroFilterString() {
        return RetroConstants.FILTERS.USERS;
    }

    @Override
    public Dao<User, Object> getDao() {
        return MyHelper.getUserDao();
    }

    public String getRef_Key() {
        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    @Override
    public void save() {
        try {
            MyHelper.getInstance().getDao(User.class).create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return this.getDescription();
    }


}
