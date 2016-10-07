package ru.sk42.tradeodata.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.Unit;


@DatabaseTable(tableName = "stock")
public class Stock extends CDO {

    @DatabaseField
    @JsonProperty("Charact_Description")
    public String charact_Description;

    @DatabaseField
    @JsonProperty("Store_Description")
    public String storeDescription;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    @JsonProperty("Unit_Key")
    private String unit_Key;

    @DatabaseField
    @JsonProperty("Unit_Description")
    private String unit_Description;

    @DatabaseField(foreign = true)
    private ProductInfo productInfo;

    @DatabaseField
    @JsonProperty("Store_Key")
    private String store_Key;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Store store;

    @DatabaseField
    @JsonProperty("Charact_Key")
    private String charact_Key;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonIgnore
    private Charact charact;

    @DatabaseField
    @JsonProperty("Price")
    private Float price;

    @DatabaseField
    @JsonProperty("Qty")
    private Float qty;

    @DatabaseField(foreign = true)
    private Unit unit;

    public Stock() {
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getQty() {
        return qty;
    }

    public void setQty(Float qty) {
        this.qty = qty;
    }

    public Charact getCharact() {
        return charact;
    }

    public void setCharact(Charact charact) {
        this.charact = charact;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {

        this.store = store;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getStoreDescription() {
        return this.store.getDescription();
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getCharact_Description() {
        if(this.charact == null)
            return "";
        else
            return this.charact.getDescription();
    }

    public void setCharact_Description(String charact_Description) {
        this.charact_Description = charact_Description;
    }

    public String getCharact_Key() {
        return charact_Key;
    }

    public void setCharact_Key(String charact_Key) {
        this.charact_Key = charact_Key;
    }


    public String getProductUnit_Key() {
        return unit_Key;
    }

    public String getUnit_Key() {
        return unit_Key;
    }

    public void setUnit_Key(String unit_Key) {
        this.unit_Key = unit_Key;
    }

    public String getUnit_Description() {
        return unit_Description;
    }

    public void setUnit_Description(String unit_Description) {
        this.unit_Description = unit_Description;
    }

    public String getStore_Key() {
        return store_Key;
    }

    public void setStore_Key(String store_Key) {
        this.store_Key = store_Key;
    }

    @Override
    public void save() {
        try {
            MyHelper.getStockDao().createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRef_Key() {
        return null;
    }

    @Override
    public void setRef_Key(String s) {

    }

    @Override
    public void setDescription(String s) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return null;
    }

    @Override
    public String getRetroFilterString() {
        return "";
    }

    @Override
    public <T> Dao<T, Object> getDao() {
        return null;
    }

}
