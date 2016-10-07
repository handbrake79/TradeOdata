package ru.sk42.tradeodata.Model;

import java.sql.SQLException;
import java.util.List;

import ru.sk42.tradeodata.Activities.MyCallBackInterface;
import ru.sk42.tradeodata.Helpers.Helper;

/**
 * Created by я on 05.08.2016.
 */
public abstract class CDO {
    public MyCallBackInterface getCallBackInterface() {
        return callBackInterface;
    }

    public CDO() {

    }

    private MyCallBackInterface callBackInterface;

    public static final <T> T getObject(Class<T> clazz, String key) {
        try {
            List<T> list =
                    Helper.getInstance().getDao(clazz).queryForEq("ref_Key", key);
            if (list.size() > 0)
                return list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showMessage(String text) {
        if (callBackInterface != null)
            callBackInterface.showMessage(getClass().getCanonicalName(), text);
    }

    public void setCallBackInterface(MyCallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }

    abstract public void save() throws SQLException;

    abstract public void setRef_Key(String s);

    abstract public String getRef_Key();

    abstract public void setDescription(String s);

    abstract public boolean isEmpty();

    public abstract String getMaintainedTableName();

    abstract public void setForeignObjects();

}
