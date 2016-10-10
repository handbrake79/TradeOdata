package ru.sk42.tradeodata.RetroRequests;

import java.util.HashMap;
import java.util.Map;

import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by я on 19.09.2016.
 */
public class RetroConstants {

    public static Map<String, String> getMap(String filter){
        Map<String, String> map = new HashMap<>();
        map.put("$format", "json");
        //map.put("$odata","nometadata");
        map.put("$filter", filter);
        return map;
    }

    public class FILTERS {
        public static final String STORES = "Parent_Key eq guid'91650e3d-51b0-11e3-8235-984be1645107' or Parent_Key eq guid'91650e34-51b0-11e3-8235-984be1645107'";
        public static final String USERS = "Parent_Key eq guid'f08a5ced-c4fb-11e2-a542-984be1645107' or  Parent_Key eq guid'f08a5ced-c4fb-11e2-a542-984be1645107'";
        public static final String DISCOUNT_CARDS = "Parent_Key eq guid'e363fff3-384e-11e1-87d6-984be1645106' or Parent_Key eq guid'f77ab099-0749-11e4-9f4b-000c29984fcd'";
        public static final String CONTRACT = "Owner_Key eq guid'" + Constants.CUSTOMER_GUID + "'";
        public static final String CUSTOMER = "Ref_Key eq guid'" + Constants.CUSTOMER_GUID + "'";
    }

}
