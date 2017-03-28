package ru.sk42.tradeodata.Helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.Contract;
import ru.sk42.tradeodata.Model.Catalogs.Customer;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.Unit;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.DocSaleList;
import ru.sk42.tradeodata.Model.Document.SaleRecordProduct;
import ru.sk42.tradeodata.Model.Document.SaleRecordService;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;

/**
 * Created by я on 01.08.2016.
 */
public class CheckRelatedDataToLoad {

    static String TAG = "CheckRelatedDataToLoad";

    static HashMap<Class<?>, ArrayList<String>> guidsToLoadMap;

    public static HashMap<Class<?>, ArrayList<String>> checkObject(Object obj) {
        guidsToLoadMap = new HashMap<>();
        guidsToLoadMap.put(Charact.class, new ArrayList<String>());
        guidsToLoadMap.put(Customer.class, new ArrayList<String>());
        guidsToLoadMap.put(Contract.class, new ArrayList<String>());
        guidsToLoadMap.put(Product.class, new ArrayList<String>());
        guidsToLoadMap.put(Store.class, new ArrayList<String>());
        guidsToLoadMap.put(Unit.class, new ArrayList<String>());
        guidsToLoadMap.put(User.class, new ArrayList<String>());

        if (obj instanceof DocSaleList) {
            Integer size = 0, current = 0;
            size = ((DocSaleList) obj).getValues().size();
            for (DocSale docSale : ((DocSaleList) obj).getValues()
                    ) {
                //и так, проверяем наличие в локальной базе данных объектов, на которые есть ссылки из текущего объекта
                //в классе "Документ" есть стринги, содержащие guid из 1С, а так же объекты соответствующих классов, для этих ссылок
                //если объект не указан, ищем в соответствующей таблице по ссылке
                //если нашли - считываем объект в память
                //если не нашли - добавляем в мапу для дальнейшей загрузки с сервера
                if (docSale == null)
                    continue;
                current++;

                if (current % 20 == 0) {
                    //callBackInterface.showProgress(current.toString() + " from " + size.toString() + " completed", "Processing " + docSale.getNumber());
                }

                checkDocCustomer(docSale);
                checkDocContract(docSale);
                checkDocAuthor(docSale);
                checkDocProductsAndServices(docSale);
                //callBackInterface.showProgress(current.toString() + " from " + size.toString() + " completed", "processed " + docSale.getNumber());

            }
        }


        if (obj instanceof DocSale) {
            //итак, проверяем наличие в локальной базе данных объектов, на которые есть ссылки из текущего объекта
            //в классе "Документ" есть стринги, содержащие guid из 1С, а так же объекты соответствующих классов, для этих ссылок
            //если объект не указан, ищем в соответствующей таблице по ссылке
            //если нашли - считываем объект в память
            //если не нашли - добавляем в мапу для дальнейшей загрузки с сервера
            DocSale docSale = (DocSale) obj;
            if (docSale != null) {
                checkDocCustomer(docSale);
                checkDocContract(docSale);
                checkDocAuthor(docSale);
                checkDocProductsAndServices(docSale);
            }

        }

        if (obj instanceof ProductInfo) {
            ArrayList<String> listUnits = guidsToLoadMap.get(Unit.class);
            ArrayList<String> listStores = guidsToLoadMap.get(Store.class);
            ArrayList<String> listCharact = guidsToLoadMap.get(Charact.class);

            String productGuid = ((ProductInfo) obj).getRef_Key();
            Product p = Product.getObject(Product.class, productGuid);
            if(p == null){
                guidsToLoadMap.get(Product.class).add(productGuid);
            }

            Iterator<Stock> it = ((ProductInfo) obj).getStocks().iterator();
            while (it.hasNext()) {
                Stock row = it.next();

                String guid = row.getCharact_Key();
                if (!listCharact.contains(guid) && row.getCharact() == null) {
                    listCharact.add(guid);
                }

                guid = row.getStore().getRef_Key();
                if (!listStores.contains(guid) && row.getStore() == null) {
                    listStores.add(guid);
                }

                guid = row.getUnit_Key();
                if (!listUnits.contains(guid) && row.getUnit() == null) {
                    listUnits.add(guid);
                }


            }

        }



        return guidsToLoadMap;
    }

    private static void checkDocProductsAndServices(DocSale docSale) {
        //если такой гуид уже есть в списке - возврат
        ArrayList<String> listProducts = guidsToLoadMap.get(Product.class);
        ArrayList<String> listUnits = guidsToLoadMap.get(Unit.class);
        ArrayList<String> listStores = guidsToLoadMap.get(Store.class);
        ArrayList<String> listCharact = guidsToLoadMap.get(Charact.class);

        for (SaleRecordProduct row : docSale.getProducts()) {
            Product product = Product.getObject(Product.class, row.getProduct_Key());

            String guid = row.getProduct_Key();
            if (!listProducts.contains(guid) && product == null) {
                listProducts.add(guid);
            }

            guid = row.getCharact_Key();
            if (guid == null) {
                Log.d(TAG, "checkDocProductsAndServices: NULL CHARACT!");
            }
            if (!listCharact.contains(guid) && row.getCharact() == null) {
                listCharact.add(guid);
            }

            guid = row.getStore().getRef_Key();
            if (!listStores.contains(guid) && row.getStore() == null) {
                listStores.add(guid);
            }

            guid = row.getUnit_Key();
            if (guid == null) {
                Log.d(TAG, "checkDocProductsAndServices: NULL UNIT!");
            }
            if (!listUnits.contains(guid) && row.getUnit() == null) {
                listUnits.add(guid);
            }


        }
        for (SaleRecordService row : docSale.getServices()) {
            String guid = row.getProduct_Key();
            Product product = Product.getObject(Product.class, guid);
            if (!listProducts.contains(guid) && product == null) {
                listProducts.add(guid);
            }
        }
        guidsToLoadMap.put(Product.class, listProducts);
        guidsToLoadMap.put(Store.class, listStores);
        guidsToLoadMap.put(Unit.class, listUnits);
        guidsToLoadMap.put(Charact.class, listCharact);


    }


    private static void checkDocAuthor(DocSale docSale) {
        //если такой гуид уже есть в списке - возврат
        String guid = docSale.getAuthor().getRef_Key();
        ArrayList<String> list = guidsToLoadMap.get(User.class);
        if (list.contains(guid))
            return;

        if (User.getObject(User.class, guid) != null) {
            return;
        }
        list.add(guid);
        guidsToLoadMap.put(User.class, list);
    }

    public static ArrayList<String> generateURL(Class clazz, ArrayList<String> refs) {
        final int maxUrls = 30;
        CDO obj = null;
        String tableName = "";
        try {
            obj = (CDO) clazz.newInstance();
            tableName = obj.getMaintainedTableName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //получим имя таблицы в 1С, которой соответствует текущий класс
        ArrayList<String> urls = new ArrayList<>();
        StringBuilder url = new StringBuilder();
        //String sConstUrl = St.getServerAddress() + St.getInfoBaseName() + "/odata/standard.odata/" + tableName + "?$format=json&$filter=";
        String sConstUrl = "";
        url.append(sConstUrl);
        int iUrlCount = 0;
        for (String ref : refs
                ) {
            if (iUrlCount == 0)
                url.append("Ref_Key eq guid'" + ref + "'");
            else
                url.append(" or Ref_Key eq guid'" + ref + "'");

            iUrlCount++;
            if (iUrlCount % maxUrls == 0 && iUrlCount > 0) {
                urls.add(url.toString());
                url = new StringBuilder();
                url.append(sConstUrl);
                iUrlCount = 0;
            }
        }
        if (!url.toString().isEmpty())
            urls.add(url.toString());
        return urls;
    }

    private static void checkDocContract(DocSale docSale) {
        //если такой гуид уже есть в списке - возврат
        if (docSale.getContract() == null) {
            Log.d(TAG, "checkDocContract: жопа какая-то!");
        }
        String guid = docSale.getContract().getRef_Key();
        ArrayList<String> list = guidsToLoadMap.get(Contract.class);
        if (list.contains(guid))
            return;

        if (Contract.getObject(Contract.class, guid) != null) {
            return;
        }
        list.add(guid);
        guidsToLoadMap.put(Contract.class, list);

    }

    private static void checkDocCustomer(DocSale docSale) {
        //если такой гуид уже есть в списке - возврат
        String guid = docSale.getCustomer().getRef_Key();
        ArrayList<String> list = guidsToLoadMap.get(Customer.class);
        if (list.contains(guid))
            return;


        Customer customer = Customer.getObject(Customer.class, guid);
        if (customer != null) {
            return;
        }
        list.add(guid);
        guidsToLoadMap.put(Customer.class, list);
    }


}
