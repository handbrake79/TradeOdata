package ru.sk42.tradeodata.Model.Documents;

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
@DatabaseTable(tableName = "SaleRowProduct")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleRowProduct {

    @DatabaseField( generatedId = true)
    @JsonIgnore
    Long id;
    @DatabaseField(foreign = true)
    DocSale docSale;
    @DatabaseField
    @JsonProperty("ПроцентСкидкиНаценки")
    private Integer discountPercentManual;
    @DatabaseField
    @JsonProperty("ПроцентАвтоматическихСкидок")
    private Integer discountPercentAuto;
    @DatabaseField
    @JsonProperty("Склад_Key")
    private String store_Key;
    @DatabaseField
    @JsonProperty("Сумма")
    private Float total;
    @DatabaseField
    @JsonProperty("Ref_Key")
    private String ref_Key;
    @DatabaseField()
    @JsonProperty("LineNumber")
    private String lineNumber;
    @DatabaseField
    @JsonProperty("Количество")
    private Float qty;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonIgnore
    private Unit unit;
    @DatabaseField
    @JsonProperty("Номенклатура_Key")
    private String product_Key;
    @DatabaseField
    @JsonProperty("ХарактеристикаНоменклатуры_Key")
    private String charact_Key;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonIgnore
    private Charact charact;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Store store;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Product product;
    @DatabaseField
    @JsonProperty("Цена")
    private Float price;
    @DatabaseField
    @JsonProperty("ЕдиницаИзмерения_Key")
    private String productUnit_Key;

    public SaleRowProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getDiscountPercentAuto() {
        return discountPercentAuto;
    }

    public void setDiscountPercentAuto(Integer discountPercentAuto) {
        this.discountPercentAuto = discountPercentAuto;
    }

    public DocSale getDocSale() {
        return docSale;
    }

    public void setDocSale(DocSale docSale) {
        this.docSale = docSale;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getQty() {
        return qty;
    }

    public void setQty(Float qty) {
        this.qty = qty;
    }

    public String getRef_Key() {
        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public String getStore_Key() {
        return store_Key;
    }

    public void setStore_Key(String store_Key) {
        this.store_Key = store_Key;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getProductUnit_Key() {
        return productUnit_Key;
    }

    public void setProductUnit_Key(String productUnit_Key) {
        this.productUnit_Key = productUnit_Key;
    }

    public String getProduct_Key() {
        return product_Key;
    }

    public void setProduct_Key(String product_Key) {
        this.product_Key = product_Key;
    }

    public String getCharact_Key() {
        return charact_Key;
    }

    public void setCharact_Key(String charact_Key) {
        this.charact_Key = charact_Key;
    }

    public Charact getCharact() {
        if (charact == null)
            charact = Charact.getObjectOrStub(charact_Key);
        return charact;
    }

    public void setCharact(Charact charact) {
        this.charact = charact;
    }

    public Integer getDiscountPercentManual() {
        return discountPercentManual;
    }

    public void setDiscountPercentManual(Integer discountPercentManual) {
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
}
