package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UsersList;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.CDO;


/**
 * Created by test on 14.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Users")
public class User extends CDO {

    public User(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    @JsonProperty("Description")
    @DatabaseField
    private String description;

    @DatabaseField(id = true)
    @com.fasterxml.jackson.annotation.JsonProperty("Ref_Key")
    private String ref_Key;

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
    public void setForeignObjects() {

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
            Helper.getInstance().getDao(User.class).create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return this.getDescription();
    }


}
