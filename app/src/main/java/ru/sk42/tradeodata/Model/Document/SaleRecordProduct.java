package ru.sk42.tradeodata.Model.Document;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.Unit;

/**
 * Created by PostRaw on 31.03.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
@Element
public class SaleRecordProduct extends SaleRecord {

    @Element(name = "СпособСписанияОстаткаТоваров")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    static final String xmlsposob = "СоСклада";

    @Element(name = "Коэффициент")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    static final int xmlkoef = 1;

    @Element(name = "Качество_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    static final String xmlkachestvo = "d05404a0-6bce-449b-a798-41ebe5e5b977";

    static final String TAG = "SaleRecordProduct***";

    @DatabaseField
    @JsonProperty("ПроцентСкидкиНаценки")
    @Element(name = "ПроцентСкидкиНаценки")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int discountPercentManual;

    @DatabaseField
    @JsonProperty("ПроцентАвтоматическихСкидок")
    @Element(name = "ПроцентАвтоматическихСкидок")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int discountPercentAuto;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ЕдиницаИзмерения_Key")
    private Unit unit;

    @Element(name = "ЕдиницаИзмерения_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getUnit_Key(){
        return unit.getRef_Key();
    }

    @Element(name = "ЕдиницаИзмерения_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setUnit_Key(String s){
    }


    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ХарактеристикаНоменклатуры_Key")
    private Charact charact;

    @Element(name = "ХарактеристикаНоменклатуры_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getCharact_Key(){
        return charact.getRef_Key();
    }

    @Element(name = "ХарактеристикаНоменклатуры_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setCharact_Key(String s){
    }

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Склад_Key")
    private Store store;

    @Element(name = "Склад_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getStore_Key(){
        return store.getRef_Key();
    }

    @Element(name = "Склад_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setStore_Key(String s){
    }

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
            MyHelper.getSaleRecordProductDao().create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            MyHelper.getSaleRecordProductDao().update(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setQty(double qty) {
        qty = Math.round(qty * 1000d) / 1000d;
        this.qty = qty;
        this.total = this.qty * this.price;

        try {
            int discount = this.discountPercentManual + this.discountPercentAuto;
            if (discount > 0) {
                this.total = this.total / 100 * (100 - discount);
            }
        } catch (Exception e) {
            Log.d(TAG, "setQty: " + e.toString());
        }
    }
}
