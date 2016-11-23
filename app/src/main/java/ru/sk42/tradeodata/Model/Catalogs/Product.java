package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by PostRaw on 16.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Products")
public class Product extends CDO {


    @DatabaseField(id = true)
    @com.fasterxml.jackson.annotation.JsonProperty("Ref_Key")
    private String ref_Key;
    @DatabaseField
    @com.fasterxml.jackson.annotation.JsonProperty("Parent_Key")
    private String parent_key;
    @DatabaseField(columnName = "Code")
    @com.fasterxml.jackson.annotation.JsonProperty("Code")
    private String code;
    @DatabaseField(columnName = "Description")
    @com.fasterxml.jackson.annotation.JsonProperty("Description")
    private String Description;
    @DatabaseField(columnName = "IsFolder")
    @com.fasterxml.jackson.annotation.JsonProperty("IsFolder")
    private Boolean isFolder;
    @DatabaseField(columnName = "IsService")
    @com.fasterxml.jackson.annotation.JsonProperty("Услуга")
    private Boolean isService;
    public Product(String ref_Key) {
        this.ref_Key = ref_Key;
        updateFromDB();
    }


    public Product() {
    }

    public static Product getStub() {
        Product p = new Product();
        p.setRef_Key(Constants.ZERO_GUID);
        p.setDescription("Заглушка, товар не определен!");
        return p;
    }

    @Override
    public Dao<Product, Object> getDao() {
        return MyHelper.getProductDao();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getService() {
        return isService;
    }

    public void setService(Boolean service) {

        this.isService = service;
    }

    public Boolean getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Boolean folder) {
        isFolder = folder;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void copyProperties(Product source) {
        this.setDescription(source.getDescription());
        this.setRef_Key(source.getRef_Key());
        this.setCode(source.getCode());
        this.setFolder(source.getFolder());
        this.setParent_key(source.getParent_key());
        this.setService(source.getService());
    }

    private void updateFromDB() {
        if (ref_Key != null) {
            try {
                Product source = MyHelper.getInstance().getDao(Product.class).queryForEq("Ref_Key", ref_Key).get(0);
                copyProperties(source);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public Boolean getFolder() {
        return isFolder;
    }

    public void setFolder(Boolean folder) {
        isFolder = folder;
    }

    public String getRef_Key() {
        if (ref_Key == null) return Constants.ZERO_GUID;
        return ref_Key;
    }

    public void setRef_Key(String mref_Key) {
        ref_Key = mref_Key;
    }

    public String getParent_key() {
        return parent_key;
    }

    public void setParent_key(String parent_key) {
        this.parent_key = parent_key;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isFirstLevelCategory() {
        return parent_key.equals(Constants.ZERO_GUID);
    }

    public boolean isTopCategory() {
        return ref_Key.equals(Constants.ZERO_GUID);
    }

    public boolean isLowerThanFirstLevel() {
        return !parent_key.equals(Constants.ZERO_GUID);
    }

    public boolean isEmpty() {
        return ref_Key.equals(Constants.ZERO_GUID);
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_Номенклатура";
    }

    @Override
    public String getRetroFilterString() {
        return "DeletionMark eq false";
    }


    //
    public void save() {

        try {
            MyHelper.getInstance().getDao(Product.class).createIfNotExists(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
