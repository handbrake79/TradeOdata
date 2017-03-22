package ru.sk42.tradeodata.Model.Catalogs;

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
 * Created by я on 29.09.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleType extends CDO {

    @DatabaseField(id = true)
    @JsonProperty("Ref_Key")
    private String ref_Key;

    @DatabaseField
    @JsonProperty
    private String Code;

    @DatabaseField
    @JsonProperty
    private String Description;

    @DatabaseField
    @JsonProperty("МаксимальныйОбъем")
    private Integer maxVolume;

    @DatabaseField
    @JsonProperty("МаксимальнаяГрузоподъемность")
    private Integer maxTonnage;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @DatabaseField
    boolean enabled;

    public static VehicleType newInstance() {
        VehicleType obj = new VehicleType();
        obj.setRef_Key(Constants.ZERO_GUID);
        obj.setDescription("");
        return obj;
    }


    public VehicleType() {
    }

    public VehicleType(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    @Override
    public String getRef_Key() {
        return this.ref_Key;
    }

    @Override
    public void setRef_Key(String Ref_Key) {
        this.ref_Key = Ref_Key;
    }

    public String getCode() {
        return this.Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public Integer getMaxVolume() {
        return this.maxVolume;
    }

    public void setMaxVolume(Integer maxVolume) {
        this.maxVolume = maxVolume;
    }

    @Override
    public Dao<VehicleType, Object> getDao() {
        return MyHelper.getVehicleTypesDao();
    }

    public Integer getMaxTonnage() {
        return this.maxTonnage;
    }

    public void setMaxTonnage(Integer maxTonnage) {
        this.maxTonnage = maxTonnage;
    }


    @Override
    public void save() throws SQLException {
        MyHelper.getVehicleTypesDao().createOrUpdate(this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_ТипыТС";
    }

    @Override
    public String getRetroFilterString() {
        return "DeletionMark eq false";
    }


    @Override
    public String toString() {
        return getDescription();
    }

    public static void createStub() {
        VehicleType v = new VehicleType(Constants.ZERO_GUID);
        v.setDescription("");
        v.setCode("");
        v.setMaxTonnage(0);
        v.setMaxVolume(0);
        try {
            v.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static VehicleType getObjectByName(String mVehicleType) {
        try {
            List<VehicleType> list = MyHelper.getVehicleTypesDao().queryForEq("Description", mVehicleType);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
