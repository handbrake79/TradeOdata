package ru.sk42.tradeodata.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.R;

/**
 * Created by PostRaw on 18.03.2016.
 */
public class Constants {

    public static final int SCANNER_EVENT = 654;
    public static final String SCANNER_DATA_LABEL = "scanner_data";
    public static final String BARCODE_LABEL = "barcode";
    public static final String REF_KEY_LABEL = "ref_Key";
    public static final String ID = "id";
    public static final String DOC_NUMBER = "docNumber";
    public static final String PRINTER_NAME = "printerName";
    public static final int PRODUCT_INFO_REQUEST_FINISHED = 656;
    public static final int PRINT_REQUEST_FINISHED = 17;
    public static ArrayList<String> DOCUMENT_ACTIONS;

    static {
        DOCUMENT_ACTIONS = new ArrayList<>();
        DOCUMENT_ACTIONS.add(St.getApp().getResources().getString(R.string.ACTION_SAVE));
        DOCUMENT_ACTIONS.add(St.getApp().getResources().getString(R.string.ACTION_SAVE_1C));
        DOCUMENT_ACTIONS.add(St.getApp().getResources().getString(R.string.ACTION_POST_1C));
        DOCUMENT_ACTIONS.add(St.getApp().getResources().getString(R.string.ACTION_PRINT));
        DOCUMENT_ACTIONS.add(St.getApp().getResources().getString(R.string.ACTION_CLOSE));
    }

    public static ArrayList<String> SETTINGS_ACTIONS;

    static {
        SETTINGS_ACTIONS = new ArrayList<>();
        SETTINGS_ACTIONS.add(St.getApp().getString(R.string.CONNECTION_SETTINGS));
        SETTINGS_ACTIONS.add("Пользователь");
        SETTINGS_ACTIONS.add("Принтер");
        SETTINGS_ACTIONS.add("Настройки сканера");
        SETTINGS_ACTIONS.add("Прочие настройки");
    }

    public static final String SHIPPING_GUID = "254bbb25-6395-422b-8f00-fd126ec82289";
    public static final String UNLOAD_GUID = "7ca2e35d-7f03-4869-9e54-eabe1af8eeba";

    public static final int SAVE_DOCUMENT_RESULT = 100;
    public static final int FEEDBACK = 0;
    public static final int LOAD_FINISHED = 1;
    public static final String CONTRACT_GUID = "e8a12ae1-26fe-11e1-b72e-984be1645106";

    public static Product SHIPPING_SERVICE;
    public static Product UNLOAD_SERVICE;

    public static final String ZERO_GUID = "00000000-0000-0000-0000-000000000000";
    public static final String ORGANISATION_GUID = "2dbd363a-d053-4721-801b-c45a27de99a3";
    public static final String CUSTOMER_GUID = "b779d1b9-be4f-4bfa-a65b-10641c5d1370";
    public static final String CURRENCY_GUID = "e8a12aca-26fe-11e1-b72e-984be1645106";
    public static int ModeNewOrder = 0;
    public static int ModeExistingOrder = 1;

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now) + " : ";
        return strDate;
    }


    public enum SERVICE_REQUEST {
        PRELOAD,
        LOAD_MISSING_FOR_LIST_OF_DOCUMENTS,
        REQUEST_DOCUMENTS,
        SAVE_TO_1C,
        REQUEST_SINGLE_DOCUMENT,
        LOAD_MISSING_FOR_DOCUMENT,
        POST_IN_1C,
        REQUEST_BARCODE,
        REQUEST_PRODUCT_INFO,
        PRINT_DOCUMENT;
    }

    public class COLORS {

        public static final int SELECTED_COLOR = 4370350;
        public static final int REGULAR_COLOR = 13882323;
        public static final int LIGHT_YELLOW = 0xffff66;


        public static final int DISABLED = 0x939393;
        public static final int ENABLED = 0xffff;
    }

}
