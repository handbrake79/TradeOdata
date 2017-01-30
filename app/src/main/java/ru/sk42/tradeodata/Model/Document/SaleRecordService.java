package ru.sk42.tradeodata.Model.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Product;


/**
 * Created by PostRaw on 31.03.2016.
 */
@Element
@DatabaseTable(tableName = "SaleRecordService")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleRecordService extends SaleRecord {

    @Attribute(name = "type")
    @Namespace(reference="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata", prefix = "m")
    public static final String saleRecordServiceAttribute = "StandardODATA.Document_РеализацияТоваровУслуг_Услуги_RowType";

    @DatabaseField
    @JsonProperty("Содержание")
    @Element(name = "Содержание")
    private static String productDescription = "услуга";

    public SaleRecordService() {
    }
    public String getProductDescription() {
        return this.getProduct().getDescription();
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
