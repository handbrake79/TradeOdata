package ru.sk42.tradeodata.Model.Document;

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
public class SaleRecordService extends SaleRecord {

    @DatabaseField
    @JsonProperty("Содержание")
    private String productDescription;

    public SaleRecordService() {
    }
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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
