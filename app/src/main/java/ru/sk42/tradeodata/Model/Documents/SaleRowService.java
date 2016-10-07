package ru.sk42.tradeodata.Model.Documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Helpers.Helper;


/**
 * Created by test on 31.03.2016.
 */
@DatabaseTable(tableName = "SaleRowService")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleRowService {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @DatabaseField(foreign = true)
    DocSale docSale;

    @DatabaseField(generatedId = true)
    @JsonIgnore
    Long id;


    @DatabaseField
    @JsonProperty("Сумма")
    private Float total;

    @DatabaseField
    @JsonProperty("Ref_Key")
    private String ref_Key;

    @DatabaseField
    @JsonProperty("LineNumber")
    private String lineNumber;

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }


    @DatabaseField
    @JsonProperty("Количество")
    private Float qty;

    @DatabaseField
    @JsonProperty("Номенклатура_Key")
    private String product_Key;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Product product;

    @DatabaseField
    @JsonProperty("Цена")
    private Float price;
    @DatabaseField
    @JsonProperty("Содержание")
    private String productDescription;

    public SaleRowService() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }


    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public DocSale getDocSale() {
        return docSale;
    }

    public void setDocSale(DocSale docSale) {
        this.docSale = docSale;
    }

    public String getProduct_Key() {
        return product_Key;
    }

    public void setProduct_Key(String product_Key) {
        this.product_Key = product_Key;
    }

    public void save() {
        try {
            Helper.getSaleRowServiceDao().create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        try {
            Helper.getSaleRowServiceDao().update(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
