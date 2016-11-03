package ru.sk42.tradeodata.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.sk42.tradeodata.Model.Catalogs.Product;

/**
 * Created by test on 18.03.2016.
 */
public class Constants {

    public static final String SHIPPING_GUID = "254bbb25-6395-422b-8f00-fd126ec82289";
    public static final String UNLOAD_GUID = "7ca2e35d-7f03-4869-9e54-eabe1af8eeba";

    public static  Product SHIPPING_SERVICE;
    public static  Product UNLOAD_SERVICE;



    public static final String NULL_GUID = "00000000-0000-0000-0000-000000000000";
    public static final String ORGANISATION_GUID = "2dbd363a-d053-4721-801b-c45a27de99a3";
    public static final String CUSTOMER_GUID = "b779d1b9-be4f-4bfa-a65b-10641c5d1370";
    public static final String CURRENCY_GUID = "e8a12aca-26fe-11e1-b72e-984be1645106";
    public static final String ACTION_DOCLIST_SET_FOREIGN_OBJECTS = "ACTION_DOCLIST_SET_FOREIGN_OBJECTS";
    public static int ModeNewOrder = 0;
    public static int ModeExistingOrder = 1;


    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now) + " : ";
        return strDate;
    }


    public enum DATALOADER_MODE {
        PRELOAD, LOAD_MISSING_FOR_LIST_OF_DOCUMENTS, REQUEST_DOCUMENTS, LOAD_MISSING_FOR_DOCUMENT
    }

    public class COLORS {
        public static final int SELECTED_COLOR = 15878042;
        public static final int DISABLED = 0x939393;
        public static final int ENABLED = 0xffff;
    }
}
