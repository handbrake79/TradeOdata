package ru.sk42.tradeodata.Model;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;

/**
 * Created by я on 05.08.2016.
 */
public abstract class CDO {

    public CDO() {

    }

    public static <T> T getObject(Class<T> clazz, String key) {
        if (key == null)
            Log.d("CDO", "getObject: ");
        try {
            List<T> list =
                    MyHelper.getInstance().getDao(clazz).queryForEq(Constants.REF_KEY_LABEL, key);
            if (list.size() > 0)
                return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    abstract public void save() throws SQLException;

    abstract public String getRef_Key();

    abstract public void setRef_Key(String s);

    abstract public void setDescription(String s);

    abstract public boolean isEmpty();

    public abstract String getMaintainedTableName();

    public abstract String getRetroFilterString();

    public abstract <T> Dao<T, Object> getDao();


}
