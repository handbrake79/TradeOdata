package ru.sk42.tradeodata.Model;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import ru.sk42.tradeodata.Activities.MyCallBackInterface;
import ru.sk42.tradeodata.Helpers.MyHelper;

/**
 * Created by я on 05.08.2016.
 */
public abstract class CDO {
    private MyCallBackInterface callBackInterface;

    public CDO() {

    }

    public static final <T> T getObject(Class<T> clazz, String key) {
        try {
            List<T> list =
                    MyHelper.getInstance().getDao(clazz).queryForEq("ref_Key", key);
            if (list.size() > 0)
                return list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MyCallBackInterface getCallBackInterface() {
        return callBackInterface;
    }

    public void setCallBackInterface(MyCallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }

    public void showMessage(String text) {
        if (callBackInterface != null)
            callBackInterface.showMessage(getClass().getCanonicalName(), text);
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
