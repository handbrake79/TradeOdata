package ru.sk42.tradeodata.Model.Catalogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Constants;


/**
 * Created by PostRaw on 31.03.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageProduct extends CDO {
    final private static String TAG = "ImageProduct";
    @JsonProperty("Объект")
    @DatabaseField(id = true)
    private String ref_Key;

    @JsonProperty("Хранилище_Base64Data")
    @DatabaseField
    private String base64Data;

    public ImageProduct() {
    }

    public Bitmap getBitMap() {

        return decodeBase64(base64Data);

    }

    public static Bitmap getBitMapByRefKey(String ref_Key) {
        ImageProduct imageProduct = ImageProduct.getObject(ImageProduct.class, ref_Key);
        if (imageProduct == null) {
            return null;
        } else {
            return imageProduct.getBitMap();
        }
    }

    @Override
    public void save() throws SQLException {
        if (base64Data.length() > Constants.MAX_IMG_LENGTH_BYTES) {
            base64Data = "";
        }
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


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Nullable
    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        if (decodedBytes.length > Constants.MAX_IMG_LENGTH_BYTES) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            Log.d(TAG, "decodeBase64: " + String.valueOf(decodedBytes.length));
            if (decodedBytes.length > Constants.MAX_IMG_LENGTH_BYTES) {
                return null;
            }
            bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
