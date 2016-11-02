package ru.sk42.tradeodata.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;

/**
 * Created by хрюн моржов on 20.10.2016.
 */

@DatabaseTable
public class Settings {

    public Settings() {
    }

    @DatabaseField(id = true)
    int id;

    @DatabaseField
    String lastViewedProductGroup;

    @DatabaseField
    Date shippingRatesDate;

    public Date getShippingRatesDate() {
        return shippingRatesDate;
    }

    public void setShippingRatesDate(Date shippingRatesDate) {
        this.shippingRatesDate = shippingRatesDate;
    }

    public int getId() {
        return 0;
    }

    public void setId(int id) {
        this.id = 0;
    }

    public String getLastViewedProductGroup() {
        return lastViewedProductGroup;
    }

    public void setLastViewedProductGroup(String lastViewedProductGroup) {
        this.lastViewedProductGroup = lastViewedProductGroup;
    }


    public static String getLastViewedProductGroupStatic(){
        String mLastViewedProductGroup = Constants.NULL_GUID;
        try {
            List<Settings> list = MyHelper.getSettingsDao().queryForAll();
            mLastViewedProductGroup = list.get(0).getLastViewedProductGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLastViewedProductGroup;
    }

    public static void setLastViewedProductGroupStatic(String ref_Key){

            Settings settings = new Settings();
            settings.setLastViewedProductGroup(ref_Key);
            settings.save();

    }

    private void save() {
        try {
            MyHelper.getSettingsDao().createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
