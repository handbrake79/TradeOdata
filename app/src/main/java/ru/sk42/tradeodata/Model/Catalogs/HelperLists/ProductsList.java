package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Helpers.Helper;

/**
 * Created by test on 18.03.2016.
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
//@DatabaseTable(tableName = "zProductsList")
public class ProductsList {


    @com.fasterxml.jackson.annotation.JsonProperty("value")

    private Collection<Product> values;

    public ProductsList() {
    }


    public Collection<Product> getValues() {
        return values;
    }

    public void setValues(Collection<Product> values) {
        this.values = values;
    }

    public List<Product> getArrayList() {

        ArrayList<Product> productArrayList;
        if (values == null) {

            productArrayList = new ArrayList<Product>();
            Product product = new Product();
            product.setRef_Key(Constants.NULL_GUID);
            product.setParent_key(Constants.NULL_GUID);
            product.setDescription("Номенклатура");
            product.setCode("");
            product.setFolder(false);
            product.setService(false);
            productArrayList.add(0, product);
            return productArrayList;

        }
        productArrayList = new ArrayList<Product>(values);
        return productArrayList;
    }

    public void save() {
        try {
            Iterator<Product> it = getValues().iterator();
            while (it.hasNext()) {
                Helper.getInstance().getDao(Product.class).createOrUpdate(it.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
