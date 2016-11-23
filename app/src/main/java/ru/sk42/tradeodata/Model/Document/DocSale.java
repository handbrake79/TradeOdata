package ru.sk42.tradeodata.Model.Document;

import android.os.Bundle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.convert.Convert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Catalogs.Contract;
import ru.sk42.tradeodata.Model.Catalogs.Currency;
import ru.sk42.tradeodata.Model.Catalogs.Customer;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Catalogs.Organisation;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.InformationRegisters.ShippingRate;
import ru.sk42.tradeodata.Model.Settings;
import ru.sk42.tradeodata.Model.SettingsOld;
import ru.sk42.tradeodata.XML.DateConverter;

/**
 * Created by PostRaw on 31.03.2016.
 */

@DatabaseTable

@JsonIgnoreProperties(ignoreUnknown = true)
@Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
public class DocSale extends CDO {
    private static final String TAG = "DocSale class";

    public static DocSale newInstance() {
        DocSale docSale = new DocSale();
        docSale.setRef_Key(Constants.ZERO_GUID);
        docSale.setAuthor(SettingsOld.getCurrentUser());
        docSale.setDate(new GregorianCalendar().getTime());
        docSale.setCustomer(Customer.getObject(Customer.class,Constants.CUSTOMER_GUID));
        docSale.setContract(Contract.getObject(Contract.class, Constants.CONTRACT_GUID));
        docSale.setComment("Создано в андроиде");
        docSale.setShippingAddress("");
        docSale.setShippingDate(new Date());
        docSale.setShippingTimeFrom(new Date());
        docSale.setShippingTimeTo(new Date());
        docSale.products = new ArrayList<>();
        docSale.services = new ArrayList<>();
        docSale.setRoute(new Route());
        docSale.setVehicleType(new VehicleType());
        docSale.setStartingPoint(new StartingPoint());
        docSale.setNumber("");
        return docSale;
    }


    @JsonProperty("Ref_Key")
    @DatabaseField(id = true)
    @Element(name = "Ref_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private String ref_Key;



    @Element(name = "ВидОперации")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private static final String xmlvidoperacii = "ПродажаКомиссия";

    @Element(name = "ВидПередачи")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private static final String xmlvidperedachi = "СоСклада";


    @Element(name = "КурсВзаиморасчетов")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private static final int xmlkurs = 1;

    @Element(name = "КратностьВзаиморасчетов")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private static final int xmlkratnost = 1;

    @Element(name = "ОтражатьВУправленческомУчете")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private static final boolean xmlupr = true;

    @JsonProperty("Number")
    @DatabaseField
    @Element(name = "Number")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private String number;

    @Element(name = "Date")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    @Convert(DateConverter.class)
    @JsonProperty("Date")
    @DatabaseField
    private Date date;

    @JsonProperty("Posted")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    @DatabaseField
    @Element
    private boolean posted;

    @JsonProperty("ФИОДляПропуска")
    @DatabaseField
    @Element(name = "ФИОДляПропуска")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private String passPerson;

    @DatabaseField
    @JsonProperty("СуммаДоставки")
    @Element(name = "СуммаДоставки")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int shippingTotal;

    @JsonProperty("СтоимостьДоставки")
    @DatabaseField
    @Element(name = "СтоимостьДоставки")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int shippingCost;

    @JsonProperty("НужнаДоставка")
    @DatabaseField
    @Element(name = "НужнаДоставка")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private boolean needShipping;

    @JsonProperty("НужнаРазгрузка")
    @DatabaseField
    @Element(name = "НужнаРазгрузка")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private boolean needUnload;

    @JsonProperty("ТипТС_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private VehicleType vehicleType;

    @JsonProperty("ВалютаДокумента_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Currency currency;

    @JsonProperty("АвтомобильДляПропуска")
    @DatabaseField
    @Element(name = "АвтомобильДляПропуска")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private String passVehicle;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Ответственный_Key")
    private User author;


    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ДоговорКонтрагента_Key")
    private Contract contract;


    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Контрагент_Key")
    private Customer customer;

    @JsonProperty("Комментарий")
    @DatabaseField
    @Element(name = "Комментарий")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private String comment;

    @JsonProperty("СуммаДокумента")
    @DatabaseField
    @Element(name = "СуммаДокумента")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private double total;

    @JsonProperty("ДисконтнаяКарта_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private DiscountCard discountCard;

    @JsonProperty("Организация_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Organisation organisation;

    @JsonProperty("Вес")
    @DatabaseField
    @Element(name = "Вес")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int weight;


    @JsonProperty("Объем")
    @DatabaseField
    @Element(name = "Объем")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int volume;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Маршрут_Key")
    private Route route;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("НачальнаяТочкаМаршрута_Key")
    private StartingPoint startingPoint;

    @JsonProperty("СтоимостьРазгрузки")
    @DatabaseField
    @Element(name = "СтоимостьРазгрузки")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int unloadCost;

    @JsonProperty("АдресДоставки")
    @DatabaseField
    @Element(name = "АдресДоставки")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private String shippingAddress;

    @JsonProperty("КоличествоГрузчиков")
    @DatabaseField
    @Element(name = "КоличествоГрузчиков")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private int workersCount;

    @JsonProperty("ДатаДоставки")
    @DatabaseField
    @Element(name = "ДатаДоставки")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    @Convert(DateConverter.class)
    private Date shippingDate;

    @JsonProperty("ВремяДоставкиС")
    @DatabaseField
    @Element(name = "ВремяДоставкиС")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    @Convert(DateConverter.class)
    private Date shippingTimeFrom;

    @JsonProperty("ВремяДоставкиПо")
    @DatabaseField
    @Element(name = "ВремяДоставкиПо")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    @Convert(DateConverter.class)
    private Date shippingTimeTo;

    @Attribute(name = "type")
    @Namespace(reference="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata", prefix = "m")
    @Path("d:Товары")
    private String docsaleType = "Collection(StandardODATA.Document_РеализацияТоваровУслуг_Товары_RowType)";

    @JsonProperty("Товары")
    @Path("d:Товары")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)
    @ElementList(inline = true, entry = "d:element")
    private Collection<SaleRecordProduct> products;





    @JsonProperty("Услуги")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)
//    @ElementList(name = "Услуги", entry = "element", inline = true)
//    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    private Collection<SaleRecordService> services;


    private String contract_Key;
    private String customer_Key;
    private String organisation_Key;
    private String discountCard_Key;
    private String route_Key;
    private String startingPoint_Key;
    private String vehicleType_Key;
    private String currency_Key;
    private String user_key;

    @Element(name = "Ответственный_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getUser_key() {
        return getAuthor().getRef_Key();
    }

    @Element(name = "Ответственный_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    @Element(name = "ДоговорКонтрагента_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getContract_Key() {
        return getContract().getRef_Key();
    }

    @Element(name = "ДоговорКонтрагента_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setContract_Key(String contract_Key) {
        this.contract_Key = contract_Key;
    }

    @Element(name = "Контрагент_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getCustomer_Key() {
        return getCustomer().getRef_Key();
    }

    @Element(name = "Контрагент_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setCustomer_Key(String customer_Key) {
        this.customer_Key = customer_Key;
    }

    @Element(name = "Организация_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getOrganisation_Key() {
        return Constants.ORGANISATION_GUID;
    }

    @Element(name = "Организация_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setOrganisation_Key(String organisation_Key) {
        this.organisation_Key = organisation_Key;
    }

    @Element(name = "ДисконтнаяКарта_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getDiscountCard_Key() {
        if (discountCard == null) {
            return Constants.ZERO_GUID;
        } else {
            return getDiscountCard().getRef_Key();
        }
    }

    @Element(name = "ДисконтнаяКарта_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setDiscountCard_Key(String discountCard_Key) {
        this.discountCard_Key = discountCard_Key;
    }

    @Element(name = "Маршрут_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getRoute_Key() {
        return getRoute().getRef_Key();
    }

    @Element(name = "Маршрут_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setRoute_Key(String route_Key) {
        this.route_Key = route_Key;
    }

    @Element(name = "НачальнаяТочкаМаршрута_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getStartingPoint_Key() {
        return getStartingPoint().getRef_Key();
    }

    @Element(name = "НачальнаяТочкаМаршрута_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setStartingPoint_Key(String startingPoint_Key) {
        this.startingPoint_Key = startingPoint_Key;
    }

    @Element(name = "ТипТС_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getVehicleType_Key() {
        return getVehicleType().getRef_Key();
    }

    @Element(name = "ТипТС_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setVehicleType_Key(String vehicleType_Key) {
        this.vehicleType_Key = vehicleType_Key;
    }

    @Element(name = "ВалютаДокумента_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public void setCurrency_Key(String currency_Key) {
        this.currency_Key = currency_Key;
    }

    @Element(name = "ВалютаДокумента_Key")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    public String getCurrency_Key() {
        return Constants.CURRENCY_GUID;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setReferenceShipingCost(int referenceShipingCost) {
        this.referenceShipingCost = referenceShipingCost;
    }

    @JsonIgnore
    private int referenceShipingCost;

    public int getReferenceShipingCost() {
        return referenceShipingCost;
    }


    public DocSale() {
    }

    public static DocSale getDocument(String ref_Key) {
        try {
            List<DocSale> list = MyHelper.getInstance().getDao(DocSale.class).queryForEq("ref_key", ref_Key);
            if (list.size() > 0)
                return list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DocSale getStub() {
        return new DocSale();
    }

    public Currency getCurrency() {
        return currency;
    }

    public DocSale setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public DocSale setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
        return this;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public DocSale setOrganisation(Organisation organisation) {
        this.organisation = organisation;
        return this;
    }

    public int getWorkersCount() {
        return workersCount;
    }

    public DocSale setWorkersCount(int workersCount) {
        this.workersCount = workersCount;
        return this;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public DocSale setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public boolean getNeedShipping() {
        return needShipping;
    }

    public void setNeedShipping(boolean needShipping) {
        this.needShipping = needShipping;
    }

    public boolean getNeedUnload() {
        return needUnload;
    }

    public void setNeedUnload(boolean needUnload) {
        this.needUnload = needUnload;
    }

    public boolean getPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
    }

    public int getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(int shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public int getUnloadCost() {
        return unloadCost;
    }

    public void setUnloadCost(int unloadCost) {
        this.unloadCost = unloadCost;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getRef_Key() {
        if(ref_Key == null)
        {
            ref_Key = Constants.ZERO_GUID;
        }
        if(ref_Key.equals(""))
        {
            ref_Key = Constants.ZERO_GUID;
        }

        return ref_Key;
    }

    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public DocSale setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public Route getRoute() {
        return route;
    }

    public DocSale setRoute(Route route) {
        this.route = route;
        return this;
    }

    public StartingPoint getStartingPoint() {
        return startingPoint;
    }

    public DocSale setStartingPoint(StartingPoint startingPoint) {
        this.startingPoint = startingPoint;
        return this;
    }

    @Override
    public void save() throws SQLException {
        //для строки товаров и для строки услуг 1с не предоставляет уникальный ключ строки
        //ключ состоит из номера строки  + гуид документа
        //в ормлайте мы не можем сделать составной ключ
        //так что для товаров и услуг в ормлайте устанавливаем авто-генерируемый айди
        //перед сохранением удаляем все строки
        //делаем новые записи в базу

        deleteProductRecords();
        deleteServiceRecords();


        for (SaleRecordProduct row :
                this.getProducts()) {
            row.setDocSale(this);
            row.save();
        }
        for (SaleRecordService row :
                this.getServices()) {
            row.setDocSale(this);
            row.save();
        }

        MyHelper.getDocSaleDao().createOrUpdate(this);

    }

    private void deleteProductRecords() {
        try {
            List<SaleRecordProduct> list = MyHelper.getSaleRecordProductDao().queryForEq("ref_Key", getRef_Key());

            MyHelper.getSaleRecordProductDao().delete(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteServiceRecords() {
        try {
            MyHelper.getSaleRowServiceDao().delete(MyHelper.getSaleRowServiceDao().queryForEq("ref_Key", this.getRef_Key()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDescription(String s) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getMaintainedTableName() {
        return null;
    }

    @Override
    public String getRetroFilterString() {
        return "";
    }

    @Override
    public Dao<DocSale, Object> getDao() {
        return MyHelper.getDocSaleDao();
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassPerson() {
        return passPerson;
    }

    public void setPassPerson(String passPerson) {
        this.passPerson = passPerson;
    }

    public String getPassVehicle() {
        return passVehicle;
    }

    public void setPassVehicle(String passVehicle) {
        this.passVehicle = passVehicle;
    }


    public Collection<SaleRecordProduct> getProducts() {
        return products;
    }

    public void setProducts(Collection<SaleRecordProduct> products) {
        this.products = products;
    }

    public ArrayList<SaleRecordProduct> getProductsList() {
        ArrayList<SaleRecordProduct> list = new ArrayList<>();
        list.addAll(products);
        return list;
    }

    public ArrayList<SaleRecordService> getServicesList() {
        ArrayList<SaleRecordService> list = new ArrayList<>();
        list.addAll(services);
        return list;
    }


    public Collection<SaleRecordService> getServices() {
        return services;
    }

    public void setServices(Collection<SaleRecordService> services) {
        this.services = services;
    }

    public void setForeignObjectsInProductsAndServices() {
        Iterator<SaleRecordProduct> iterator = getProducts().iterator();
        while (iterator.hasNext()) {
            boolean save = false;
            SaleRecordProduct row = iterator.next();
            Product product = Product.getObject(Product.class, row.getProduct_Key());
            if (product != null) {
                save = true;
                row.setProduct(product);
            }

            if (save) {
                row.update();
            }
        }

        for (SaleRecordService saleRecordService : this.getServices()
                ) {
            if (saleRecordService.getProduct() == null) {
                saleRecordService.setProduct(Product.getObject(Product.class, saleRecordService.getProduct_Key()));
                saleRecordService.update();
            }

        }
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPostedDescr() {
        return posted ? "Проведен" : "Не проведен";
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public DocSale setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
        return this;
    }

    public Date getShippingTimeFrom() {
        return shippingTimeFrom;
    }

    public DocSale setShippingTimeFrom(Date shippingTimeFrom) {
        this.shippingTimeFrom = shippingTimeFrom;
        return this;
    }

    public Date getShippingTimeTo() {
        return shippingTimeTo;
    }

    public DocSale setShippingTimeTo(Date shippingTimeTo) {
        this.shippingTimeTo = shippingTimeTo;
        return this;
    }

    public void setForeignObjects() {
        this.setForeignObjectsInProductsAndServices();
        update();
    }

    @Override
    public String toString() {
        return "Реализация № " + getNumber() + " от " + Uttils.DATE_FORMATTER.format(getDate());
    }


    void update() {
        try {
            MyHelper.getDocSaleDao().update(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeShippingFromServicesCollection() {
        for (SaleRecordService rec :
                this.getServices()) {
            String ref_key = rec.getProduct_Key();
            if (ref_key == null) {
                continue;
            }
            if (ref_key.equals(Constants.SHIPPING_GUID)) {
                this.getServices().remove(rec);
            }
        }
    }

    private void removeUnloadFromServicesCollection() {
        for (SaleRecordService rec :
                this.getServices()) {
            String ref_key = rec.getProduct_Key();
            if (ref_key == null) {
                continue;
            }
            if (ref_key.equals(Constants.UNLOAD_GUID)) {
                this.getServices().remove(rec);
            }
        }

    }

    private void addShippingToServicesCollection() {

        removeShippingFromServicesCollection();

        SaleRecordService rec = new SaleRecordService();
        rec.setRef_Key(this.getRef_Key());
        rec.setProduct_Key(Constants.SHIPPING_GUID);
        rec.setDocSale(this);
        rec.setLineNumber(getServices().size() + 1);
        rec.setPrice(this.shippingCost);
        rec.setQty(1);
        rec.setProduct(Constants.SHIPPING_SERVICE);
        this.getServices().add(rec);
    }

    private void addUnloadToServicesCollection() {

        removeUnloadFromServicesCollection();

        SaleRecordService rec = new SaleRecordService();
        rec.setDocSale(this);
        rec.setRef_Key(this.getRef_Key());
        rec.setProduct_Key(Constants.UNLOAD_GUID);
        rec.setLineNumber(getServices().size() + 1);
        rec.setPrice(this.unloadCost);
        rec.setQty(1);
        rec.setProduct(Constants.UNLOAD_SERVICE);
        this.getServices().add(rec);
    }

    public void reCalculateTotal() {
        if (needShipping) {

            int routeCost = ShippingRate.getCost(this.getStartingPoint(), route, this.getVehicleType());
            this.referenceShipingCost = routeCost;
            this.setShippingCost(routeCost);

            addShippingToServicesCollection();

        } else {

            removeShippingFromServicesCollection();

            setShippingCost(0);
            setUnloadCost(0);
            setNeedUnload(false);
        }
        if (needUnload) {

            addUnloadToServicesCollection();

        } else {
            setUnloadCost(0);

            removeUnloadFromServicesCollection();
        }

        setShippingTotal(shippingCost + unloadCost);

        this.weight = 0;
        this.volume = 0;
        this.total = 0d;
        //вес, объем
        for (SaleRecordProduct record : getProducts()
                ) {

            this.weight += (int) (record.getQty() * record.getUnit().getWeight());
            this.volume += (int) (record.getQty() * record.getUnit().getVolume());

            double recTotal = record.getQty() * record.getPrice();
            int discount = record.getDiscountPercentAuto() + record.getDiscountPercentManual();
            if (discount > 0) {
                recTotal = recTotal / 100 * (100 - discount);
            }
            record.setTotal(recTotal);
            this.total += recTotal;
        }
        for (SaleRecordService record : getServices()
                ) {
            record.setTotal(record.getQty() * record.getPrice());
            this.total += record.getTotal();
        }

        this.total = Math.round(this.total * 100.0) / 100.0;
    }

    public double getProductsTotal() {
        double t = 0;
        for (SaleRecordProduct rec :
                getProducts()) {
            t += rec.getTotal();
        }
        return t;
    }

    public double getServicesTotal() {
        double t = 0;
        for (SaleRecordService rec :
                getServices()) {
            t += rec.getTotal();
        }
        return t;
    }
}
