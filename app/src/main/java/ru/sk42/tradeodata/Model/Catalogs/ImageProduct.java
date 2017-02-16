package ru.sk42.tradeodata.Model.Catalogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;


/**
 * Created by PostRaw on 31.03.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageProduct extends CDO {
    @JsonProperty("Объект")
    @DatabaseField(id = true)
    private String ref_Key;

    @JsonProperty("Хранилище_Base64Data")
    @DatabaseField
    private String base64Data;

    public ImageProduct() {
    }

    public Bitmap getBitMap(){
        byte[] decodedString = Base64.decode(this.base64Data, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Bitmap getBitMapByRefKey(String ref_Key){
        ImageProduct imageProduct = ImageProduct.getObject(ImageProduct.class, ref_Key);
        if(imageProduct == null){
            return null;
        }
        else {
            return imageProduct.getBitMap();
        }
    }

    @Override
    public void save() throws SQLException {
        MyHelper.getProductImageDao().createOrUpdate(this);
    }

    @Override
    public Dao<ImageProduct, Object> getDao() {
        return MyHelper.getProductImageDao();
    }


    public String getRef_Key() {
        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    @Override
    public void setDescription(String s) {

    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return "Catalog_Склады";
    }

    @Override
    public String getRetroFilterString() {
        return null;
    }


}
