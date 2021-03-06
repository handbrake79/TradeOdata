package ru.sk42.tradeodata.Model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;

@DatabaseTable
public class ProductInfo extends CDO {

    static final String TAG = "***ProductInfo";

    @DatabaseField(id = true)
    @JsonProperty("Ref_Key")
    private String ref_Key;

    @DatabaseField
    @JsonProperty("Barcode")
    private String barcode;


    @DatabaseField
    @JsonProperty("Description")
    private String description;

    @DatabaseField
    @JsonProperty("Price")
    private double price;

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @DatabaseField
    private Date requestDate;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public void save() {
        this.setRequestDate(GregorianCalendar.getInstance().getTime());
        Dao<ProductInfo, Object> daoProductInfo = MyHelper.getProductInfoDao();

        try {
            daoProductInfo.createOrUpdate(this);
            List<Stock> stocks = MyHelper.getStockDao().queryForEq("productInfo_id", this.getRef_Key());
            MyHelper.getStockDao().delete(stocks);

            for (Stock stock :
                    this.getStocks()) {
                stock.setProductInfo(this);
                stock.save();
            }
//            daoProductInfo.createOrUpdate(this);
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

    public static ProductInfo getByBarcode(String barcode) {
        try {
            List<ProductInfo> list = MyHelper.getProductInfoDao().queryForEq("barcode", barcode);
            if (list.isEmpty()) {
                return null;
            } else {
                return list.get(0);
            }
        } catch (SQLException e) {
            Log.d(TAG, "getByBarcode: ");
            e.printStackTrace();
            return null;
        }
    }
}