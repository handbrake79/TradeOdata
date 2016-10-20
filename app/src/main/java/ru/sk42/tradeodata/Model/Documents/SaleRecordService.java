package ru.sk42.tradeodata.Model.Documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Product;


/**
 * Created by test on 31.03.2016.
 */
@DatabaseTable(tableName = "SaleRecordService")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleRecordService {

    @DatabaseField(foreign = true)
    DocSale docSale;

    @DatabaseField(generatedId = true)
    @JsonIgnore
    long id;

    @DatabaseField
    @JsonProperty("Сумма")
    private Float total;

    @DatabaseField
    @JsonProperty("Ref_Key")
    private String ref_Key;

    @DatabaseField
    @JsonProperty("LineNumber")
    private int lineNumber;

    @DatabaseField
    @JsonProperty("Количество")
    private Float qty;

    @JsonProperty("Номенклатура_Key")
    @DatabaseField
    private String product_Key;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Product product;

    @DatabaseField
    @JsonProperty("Цена")
    private Float price;

    @DatabaseField
    @JsonProperty("Содержание")
    private String productDescription;

    public SaleRecordService() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
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
            MyHelper.getSaleRowServiceDao().create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        try {
            MyHelper.getSaleRowServiceDao().update(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
