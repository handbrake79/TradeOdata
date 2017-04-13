package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by PostRaw on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersList implements DataList {

    @JsonProperty("value")
    private Collection<User> values;

    public UsersList(){}

    public static CharSequence[] getValuesAsCharSequence() {
        List<User> list;
        try {
            list = MyHelper.getInstance().getDao(User.class).queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            list = new ArrayList<>();

        }


            final CharSequence[] charSequenceItems = new CharSequence[list.size()];

            int i = 0;
            for (User user : list
                    ) {
                charSequenceItems[i] = user.getDescription();
                i++;
            }

            return charSequenceItems;

    }


    public List<User> getArrayList() {

        ArrayList<User> usersArrayList;
        if (values == null) {

            usersArrayList = new ArrayList<User>();
            User user = new User();
            user.setRef_Key(Constants.ZERO_GUID);
            user.setDescription("Нет данных сотрудника");
            usersArrayList.add(0, user);
            return usersArrayList;

        }
        usersArrayList = new ArrayList<User>(values);
        return usersArrayList;
    }

    public Collection<User> getValues() {
        return values;
    }

    public void setValues(Collection<User> values) {
        this.values = values;
    }

    public void save(){
        try {
            MyHelper.getUserDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
