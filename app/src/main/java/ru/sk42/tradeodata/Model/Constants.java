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

    public static final int SCANNER_EVENT = 10101;
    public static final String SCANNER_DATA_LABEL = "scanner_data";
    public static final String BARCODE_LABEL = "barcode";
    public static final String REF_KEY_LABEL = "ref_Key";
    public static final String ID = "id";
    public static final String DOC_NUMBER = "docNumber";
    public static final String PRINTER_NAME = "printerName";
    public static final int PRODUCT_INFO_REQUEST_FINISHED = 12;
    public static final int PRINT_REQUEST_FINISHED = 13;
    public static final int PRODUCT_SELECTED = 14;
    public static final int RECORD_SELECTED_IN_DOCUMENT = 15;
    public static final int SHOW_PRODUCTS_LIST = 16;
    public static final int SELECT_RECORD_FOR_CHANGE = 17;
    public static final int SELECT_RECORD_FOR_VIEW_PRODUCT = 18;
    public static final int IMAGE_LOADED = 19;
    public static final String OPERATION_SUCCESS_LABEL = "success";
    public static final int SAVE_COMPLETE = 20;
    public static final int POST_COMPLETE = 21;
    public static final String MESSAGE_LABEL = "message";
    public static final String RESULT_CODE_LABEL = "result_code";
    public static final String MODE_LABEL = "mode";
    public static final int MAX_IMG_LENGTH_BYTES = 500000;
    public static final String LINE_NUMBER = "line_number";
    public static final int PRODUCT_INFO_EXPIRATION_TIME_SECONDS = 10;
    public static final int REQUEST_SETTINGS_USER = 22;
    public static final String REQUEST_SETTINGS_USER_LABEL = "REQUEST_SETTINGS_USER";
    public static ArrayList<String> DOCUMENT_ACTIONS;
    public static String QUANTITY = "qty";
    public static String DESCR = "descr";
    public static String CHARACT = "charact";
    public static String PRICE = "price";

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
        SETTINGS_ACTIONS.add("Типы транспорта");
        SETTINGS_ACTIONS.add("Прочие настройки");
    }

    public static final String SHIPPING_GUID = "254bbb25-6395-422b-8f00-fd126ec82289";
    public static final String UNLOAD_GUID = "7ca2e35d-7f03-4869-9e54-eabe1af8eeba";

    public static final int FEEDBACK = 98765;
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


    public enum REQUESTS {
        PRELOAD,
        LOAD_MISSING_FOR_LIST_OF_DOCUMENTS,
        LOAD_DOCUMENTS,
        SAVE_DOCUMENT,
        SINGLE_DOCUMENT,
        LOAD_MISSING_FOR_DOCUMENT,
        POST_DOCUMENT,
        BARCODE,
        PRODUCT_INFO,
        PRINT_DOCUMENT,
        DISCOUNT_CARD,
        LOAD_IMAGE,
        NONE
    }


}
