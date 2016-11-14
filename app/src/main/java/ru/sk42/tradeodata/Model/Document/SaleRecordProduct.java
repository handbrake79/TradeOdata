package ru.sk42.tradeodata.Model.Document;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.Unit;

/**
 * Created by test on 31.03.2016.
 */
@DatabaseTable(tableName = "SaleRecordProduct")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleRecordProduct extends SaleRecord {

    static final String TAG = "SaleRecordProduct***";

    @DatabaseField
    @JsonProperty("ПроцентСкидкиНаценки")
    private int discountPercentManual;

    @DatabaseField
    @JsonProperty("ПроцентАвтоматическихСкидок")
    private int discountPercentAuto;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ЕдиницаИзмерения_Key")
    private Unit unit;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ХарактеристикаНоменклатуры_Key")
    private Charact charact;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Склад_Key")
    private Store store;

    public SaleRecordProduct() {
    }

    public long getId() {
        return id;

    }

    public void setId(long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Integer getDiscountPercentAuto() {
        return discountPercentAuto;
    }

    public void setDiscountPercentAuto(int discountPercentAuto) {
        this.discountPercentAuto = discountPercentAuto;
    }

    public DocSale getDocSale() {
        return docSale;
    }

    public void setDocSale(DocSale docSale) {
        this.docSale = docSale;
    }

    public Charact getCharact() {
        return charact;
    }

    public void setCharact(Charact charact) {
        this.charact = charact;
    }

    public Integer getDiscountPercentManual() {
        return discountPercentManual;
    }

    public void setDiscountPercentManual(int discountPercentManual) {
        this.discountPercentManual = discountPercentManual;
    }

    public Unit getUnit() {
        return unit;
    }


    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void save() {
        try {
            MyHelper.getSaleRowProductDao().create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            MyHelper.getSaleRowProductDao().update(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getActualPrice() {
        return Math.round(this.total / this.qty * 100) / 100;
    }

    @Override
    public void setQty(double qty){
        this.qty = qty;
        this.total = this.qty * this.price;

        try {
            int discount = this.discountPercentManual + this.discountPercentAuto;
            if(discount > 0){
                this.total = this.total / 100 * (100 - discount);
            }
        } catch (Exception e) {
            Log.d(TAG, "setQty: " + e.toString());
        }
    }
}
