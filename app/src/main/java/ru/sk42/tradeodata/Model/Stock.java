package ru.sk42.tradeodata.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock extends CDO {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private ProductInfo productInfo;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Store_Key")
    private Store store;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Charact_Key")
    private Charact charact;

    @DatabaseField
    @JsonProperty("Price")
    private Float price;

    @DatabaseField
    @JsonProperty("Qty")
    private Float qty;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Unit_Key")
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


    public String getCharact_Description() {
        if (this.charact == null)
            return "";
        else
            return this.charact.getDescription();
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
