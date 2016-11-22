package ru.sk42.tradeodata.Model.Catalogs;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by PostRaw on 16.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Chars")
public class Charact extends CDO{
    private static final String TAG = "Charact";
    @DatabaseField(id = true)
    @JsonProperty("Ref_Key")
    private String ref_Key;
    @DatabaseField
    @JsonProperty("Owner_Key")
    private String owner_Key;
    @DatabaseField
    @JsonProperty("Description")
    private String description;

    public Charact(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public Charact() {
    }

    public static <T> T getObject(Class<T> clazz, String key) {
        try {
            List<Charact> list = MyHelper.getInstance().getDao(Charact.class).queryForEq("Ref_Key", key);
            if (list.size() > 0)
                return (T) list.get(0);
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void createStub(){
        Charact charact = new Charact(Constants.NULL_GUID);
        charact.setOwner_Key(Constants.NULL_GUID);
        charact.setDescription("-");
        try {
            charact.save();
        } catch (SQLException e) {
            Log.e(TAG, "createStub: FAILED", e);
            e.printStackTrace();
        }

    }

    @Override
    public void save() throws SQLException {

        MyHelper.getCharactDao().createOrUpdate(this);

    }

    public String getDescription() {
        if (description == null)
            return "нет характеристики";
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getRef_Key() {
        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public boolean isEmpty() {
        if (this.ref_Key.equals(Constants.NULL_GUID))
            return true;
        else
            return false;
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_ХарактеристикиНоменклатуры";
    }

    @Override
    public String getRetroFilterString() {
        return "";
    }

    @Override
    public Dao<Charact, Object> getDao() {
        return MyHelper.getCharactDao();
    }

    public String getOwner_Key() {
        return owner_Key;
    }

    public void setOwner_Key(String owner_Key) {
        this.owner_Key = owner_Key;
    }
}
