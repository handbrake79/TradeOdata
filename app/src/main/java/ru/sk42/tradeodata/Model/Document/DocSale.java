package ru.sk42.tradeodata.Model.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
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

/**
 * Created by test on 31.03.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocSale extends CDO {

    private static final String TAG = "DocSale class";

    @JsonProperty("Товары")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)

    private Collection<SaleRecordProduct> products;
    @JsonProperty("Услуги")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)

    private Collection<SaleRecordService> services;

    @JsonProperty("Number")
    @DatabaseField
    private String number;

    @JsonProperty("Date")
    @DatabaseField
    private Date date;

    @JsonProperty("Posted")
    @DatabaseField
    private Boolean posted;

    @JsonProperty("ФИОДляПропуска")
    @DatabaseField
    private String passPerson;

    @JsonProperty("СуммаДоставки")
    @DatabaseField
    private Integer shippingTotal;

    @JsonProperty("СтоимостьДоставки")
    @DatabaseField
    private Integer shippingCost;

    @JsonProperty("НужнаДоставка")
    @DatabaseField
    private Boolean needShipping;

    @JsonProperty("НужнаРазгрузка")
    @DatabaseField
    private Boolean needUnload;

    @JsonProperty("Ref_Key")
    @DatabaseField(id = true)
    private String ref_Key;

    @JsonProperty("ТипТС_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private VehicleType vehicleType;

    @JsonProperty("ВалютаДокумента_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Currency currency;

    @JsonProperty("АвтомобильДляПропуска")
    @DatabaseField
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
    private String comment;

    @JsonProperty("СуммаДокумента")
    @DatabaseField
    private Double total;

    @JsonProperty("ДисконтнаяКарта_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private DiscountCard discountCard;

    @JsonProperty("Организация_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Organisation organisation;

    @JsonProperty("Вес")
    @DatabaseField
    private Integer weight;


    @JsonProperty("Объем")
    @DatabaseField
    private Integer volume;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Маршрут_Key")
    private Route route;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("НачальнаяТочкаМаршрута_Key")
    private StartingPoint startingPoint;

    @JsonProperty("СтоимостьРазгрузки")
    @DatabaseField
    private Integer unloadCost;

    @JsonProperty("АдресДоставки")
    @DatabaseField
    private String shippingAddress;

    @JsonProperty("КоличествоГрузчиков")
    @DatabaseField
    private Integer workersCount;

    @JsonProperty("ДатаДоставки")
    @DatabaseField
    private Date shippingDate;

    @JsonProperty("ВремяДоставкиС")
    @DatabaseField
    private Date shippingTimeFrom;

    @JsonProperty("ВремяДоставкиПо")
    @DatabaseField
    private Date shippingTimeTo;


    public int getReferenceShipingCost() {
        return referenceShipingCost;
    }

    private int referenceShipingCost;

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

    public Integer getWorkersCount() {
        return workersCount;
    }

    public DocSale setWorkersCount(Integer workersCount) {
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

    public Boolean getNeedShipping() {
        return needShipping;
    }

    public void setNeedShipping(Boolean needShipping) {
        this.needShipping = needShipping;
    }

    public Boolean getNeedUnload() {
        return needUnload;
    }

    public void setNeedUnload(Boolean needUnload) {
        this.needUnload = needUnload;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }


    public String getCurrency_Key() {
        return Constants.CURRENCY_GUID;
    }


    public String getFormattedDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
        Calendar calendar = new GregorianCalendar(1900 + date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
        fmt.setCalendar(calendar);
        return fmt.format(calendar.getTime());
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

    public Integer getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Integer shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Integer getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(int shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public Integer getUnloadCost() {
        return unloadCost;
    }

    public void setUnloadCost(Integer unloadCost) {
        this.unloadCost = unloadCost;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getRef_Key() {
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
            List<SaleRecordProduct> list = MyHelper.getSaleRowProductDao().queryForEq("ref_Key", getRef_Key());

            MyHelper.getSaleRowProductDao().delete(list);
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

//            Unit unit = Unit.getObject(Unit.class, row.getProductUnit_Key());
//
//            if (unit != null) {
//                save = true;
//                row.setUnit(unit);
//            }
//
//            Store store = Store.getObject(Store.class, row.getStore_Key());
//            if (store != null) {
//                save = true;
//                row.setStore(store);
//            }
//
//            Charact charact = Charact.getObject(Charact.class, row.getCharact_Key());
//            if (charact != null) {
//                save = true;
//                row.setCharact(charact);
//            }

            if (save)
                row.update();
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

        //this.reload();
    }

    @Override
    public String toString() {
        return "Реализация № " + getNumber() + " от " + getFormattedDate();
    }

    public void setForeignObjectsInHeader() {
        //this.setAuthor(User.getList(User.class, getAuthorRefKey()));
        //this.setCustomer(Customer.getList(Customer.class, getCustomerRefKey()));
        //this.setContract(Contract.getList(Contract.class, getContractRefKey()));
        //this.setRoute(Route.getList(Route.class, getRoute_Key()));
        //this.setStartingPoint(StartingPoint.getList(StartingPoint.class, getStartingPoint_Key()));

        update();
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
            t+= rec.getTotal();
        }
        return t;
    }
    public double getServicesTotal() {
        double t = 0;
        for (SaleRecordService rec :
                getServices()) {
            t+= rec.getTotal();
        }
        return t;
    }
}
