package ru.sk42.tradeodata.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;

@DatabaseTable
public class ProductInfo extends CDO {

    @DatabaseField(id = true)
    @JsonProperty("Ref_Key")
    private String ref_Key;

    @DatabaseField
    @JsonProperty("Description")
    private String description;

    @JsonProperty("Stock")
    @ForeignCollectionField(eager = true, maxEagerLevel = 9)
    private Collection<Stock> stocks;

    public ProductInfo() {
    }

    public Collection<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Collection<Stock> stocks) {
        this.stocks = stocks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String product_Name) {
        this.description = product_Name;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return null;
    }

    @Override
    public String getRetroFilterString() {
        return "";
    }

    @Override
    public Dao<ProductInfo, Object> getDao() {
        return MyHelper.getProductInfoDao();
    }

    public String getRef_Key() {
        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    @Override
    public void save() {
        Dao<ProductInfo, Object> daoInfo = MyHelper.getProductInfoDao();
        Dao<Stock, Object> daoStock = MyHelper.getStockDao();

        try {
            for (Stock stock :
                    this.getStocks()) {
                stock.setProductInfo(this);
                stock.save();
            }
            daoInfo.createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Stock> getArrayList() {

        ArrayList<Stock> stockArrayList;
        if (stocks == null) {
            return new ArrayList<Stock>();
        }
        stockArrayList = new ArrayList<>(stocks);
        return stockArrayList;
    }

}