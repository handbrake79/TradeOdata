package ru.sk42.tradeodata.Model.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.j256.ormlite.field.DatabaseField;

import ru.sk42.tradeodata.Model.Catalogs.Product;

/**
 * Created by хрюн моржов on 02.11.2016.
 */

//@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleRecord {

    public SaleRecord() {
    }

    @DatabaseField(foreign = true)
    DocSale docSale;

    @DatabaseField(generatedId = true)
    @JsonIgnore
    long id;

    @DatabaseField
    @JsonProperty("Сумма")
    protected double total;

    @DatabaseField
    @JsonProperty("Ref_Key")
    protected String ref_Key;

    @DatabaseField
    @JsonProperty("LineNumber")
    protected int lineNumber;

    @DatabaseField
    @JsonProperty("Количество")
    protected double qty;

    @JsonProperty("Номенклатура_Key")
    @DatabaseField
    protected String product_Key;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    protected Product product;

    @DatabaseField
    @JsonProperty("Цена")
    protected double price;

    public DocSale getDocSale() {
        return docSale;
    }

    public void setDocSale(DocSale docSale) {
        this.docSale = docSale;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getRef_Key() {
        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
        this.total = this.qty * this.price;
    }

    public String getProduct_Key() {
        return product_Key;
    }

    public void setProduct_Key(String product_Key) {
        this.product_Key = product_Key;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
