package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by PostRaw on 18.03.2016.
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "zProductsList")
public class ProductsList implements DataList {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @com.fasterxml.jackson.annotation.JsonProperty("value")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)
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
            product.setRef_Key(Constants.ZERO_GUID);
            product.setParent_key(Constants.ZERO_GUID);
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
            MyHelper.getProductsListDao().createOrUpdate(this);
            Iterator<Product> it = getValues().iterator();
            while (it.hasNext()) {
                Product product = it.next();
                product.setProductsList(this);
                MyHelper.getInstance().getDao(Product.class).createOrUpdate(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
