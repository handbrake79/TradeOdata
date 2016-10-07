package ru.sk42.tradeodata.Model.Documents;

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
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.Unit;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by test on 31.03.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocSale extends CDO {
    private static final String TAG = "DocSale class";
    @JsonProperty("Товары")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)
    private Collection<SaleRowProduct> products;
    @JsonProperty("Услуги")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)
    private Collection<SaleRowService> services;
    @JsonProperty("Number")
    @DatabaseField
    private String sNumber;
    @JsonProperty("Date")
    @DatabaseField
    private Date dDate;
    @JsonProperty("Posted")
    @DatabaseField
    private Boolean bPosted;
    @JsonProperty("ФИОДляПропуска")
    @DatabaseField
    private String sPassPerson;
    @JsonProperty("СуммаДоставки")
    @DatabaseField
    private Float fShippingTotal;
    @JsonProperty("СтоимостьДоставки")
    @DatabaseField
    private Float fShippingCost;
    @JsonProperty("НужнаДоставка")
    @DatabaseField
    private Boolean bNeedShipping;
    @JsonProperty("НужнаРазгрузка")
    @DatabaseField
    private Boolean bNeedUnload;
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
    private String PassVehicle;
    //@JsonIgnore
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Ответственный_Key")
    private User author;
//    @JsonIgnore
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ДоговорКонтрагента_Key")
    private Contract contract;
//    @JsonIgnore
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Контрагент_Key")
    private Customer customer;
    @JsonProperty("Комментарий")
    @DatabaseField
    private String Comment;
    @JsonProperty("СуммаДокумента")
    @DatabaseField
    private Float Total;
    @JsonProperty("ДисконтнаяКарта_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private DiscountCard discountCard;
    @JsonProperty("Организация_Key")
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Organisation organisation;

//    @JsonProperty("Контрагент_Key")
//    @DatabaseField
//    private String customer_Key;

    //    @JsonProperty("Ответственный_Key")
//    @DatabaseField
//    private String author_Key;
    @JsonProperty("Вес")
    @DatabaseField
    private Integer Weight;


    //    @JsonProperty("ДоговорКонтрагента_Key")
//    @DatabaseField
//    private String contractRefKey;
    @JsonProperty("Объем")
    @DatabaseField
    private Integer Volume;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Маршрут_Key")
    private Route route;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("НачальнаяТочкаМаршрута_Key")
    private StartingPoint startingPoint;
    @JsonProperty("СтоимостьРазгрузки")
    @DatabaseField
    private Float unloadCost;
    @JsonProperty("АдресДоставки")
    @DatabaseField
    private String shippingAddress;
    @JsonProperty("КоличествоГрузчиков")
    @DatabaseField
    private Integer workers;
    @JsonProperty("ДатаДоставки")
    @DatabaseField
    private Date shippingDate;
    @JsonProperty("ВремяДоставкиС")
    @DatabaseField
    private Date shippingTimeFrom;
    @JsonProperty("ВремяДоставкиПо")
    @DatabaseField
    private Date shippingTimeTo;

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

    public Integer getWorkers() {
        return workers;
    }

    public DocSale setWorkers(Integer workers) {
        this.workers = workers;
        return this;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public DocSale setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public Boolean getbNeedShipping() {
        return bNeedShipping;
    }

    public void setbNeedShipping(Boolean bNeedShipping) {
        this.bNeedShipping = bNeedShipping;
    }

    public Boolean getbNeedUnload() {
        return bNeedUnload;
    }

    public void setbNeedUnload(Boolean bNeedUnload) {
        this.bNeedUnload = bNeedUnload;
    }

    public Boolean getbPosted() {
        return bPosted;
    }

    public void setbPosted(Boolean bPosted) {
        this.bPosted = bPosted;
    }


    public String getCurrency_Key() {
        return Constants.CURRENCY_GUID;
    }


    public String getFormattedDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
        Calendar calendar = new GregorianCalendar(1900 + dDate.getYear(), dDate.getMonth(), dDate.getDate(), dDate.getHours(), dDate.getMinutes(), dDate.getSeconds());
        fmt.setCalendar(calendar);
        return fmt.format(calendar.getTime());
    }

    public Date getdDate() {
        return dDate;
    }

    public void setdDate(Date dDate) {
        this.dDate = dDate;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float total) {
        this.Total = total;
    }

    public Float getfShippingCost() {
        return fShippingCost;
    }

    public void setfShippingCost(Float fShippingCost) {
        this.fShippingCost = fShippingCost;
    }

    public Float getfShippingTotal() {
        return fShippingTotal;
    }

    public void setfShippingTotal(Float fShippingTotal) {
        this.fShippingTotal = fShippingTotal;
    }

    public Float getUnloadCost() {
        return unloadCost;
    }

    public void setUnloadCost(Float unloadCost) {
        this.unloadCost = unloadCost;
    }

    public Integer getVolume() {
        return Volume;
    }

    public void setVolume(Integer volume) {
        this.Volume = volume;
    }

    public Integer getWeight() {
        return Weight;
    }

    public void setWeight(Integer weight) {
        this.Weight = weight;
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
    public void save() throws SQLException{
        for (SaleRowProduct row :
                this.getProducts()) {
            row.setDocSale(this);
            row.save();
        }
        for (SaleRowService row :
                this.getServices()) {
            row.setDocSale(this);
            row.save();
        }

        MyHelper.getDocSaleDao().create(this);

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
        return Comment;
    }

    public void setComment(String comment) {
        this.Comment = comment;
    }

    public String getsNumber() {
        return sNumber;
    }

    public void setsNumber(String sNumber) {
        this.sNumber = sNumber;
    }

    public String getsPassPerson() {
        return sPassPerson;
    }

    public void setsPassPerson(String sPassPerson) {
        this.sPassPerson = sPassPerson;
    }

    public String getPassVehicle() {
        return PassVehicle;
    }

    public void setPassVehicle(String passVehicle) {
        this.PassVehicle = passVehicle;
    }

    public Collection<SaleRowProduct> getProducts() {
        return products;
    }

    public void setProducts(Collection<SaleRowProduct> products) {
        this.products = products;
    }

    public ArrayList<SaleRowProduct> getProductsList() {
        ArrayList<SaleRowProduct> list = new ArrayList<>();
        list.addAll(products);
        return list;
    }

    public ArrayList<SaleRowService> getServicesList() {
        ArrayList<SaleRowService> list = new ArrayList<>();
        list.addAll(services);
        return list;
    }


    public Collection<SaleRowService> getServices() {
        return services;
    }

    public void setServices(Collection<SaleRowService> services) {
        this.services = services;
    }

    public void setForeignObjectsInProductsAndServices() {
        Iterator<SaleRowProduct> iterator = getProducts().iterator();
        while (iterator.hasNext()) {
            boolean save = false;
            SaleRowProduct row = iterator.next();
            Product product = Product.getObject(Product.class, row.getProduct_Key());
            if (product != null) {
                save = true;
                row.setProduct(product);
            }

            Unit unit = Unit.getObject(Unit.class, row.getProductUnit_Key());

            if (unit != null) {
                save = true;
                row.setUnit(unit);
            }

            Store store = Store.getObject(Store.class, row.getStore_Key());
            if (store != null) {
                save = true;
                row.setStore(store);
            }

            if (save)
                row.update();
        }

        for (SaleRowService saleRowService : this.getServices()
                ) {
            if (saleRowService.getProduct() == null) {
                saleRowService.setProduct(Product.getObject(Product.class, saleRowService.getProduct_Key()));
                saleRowService.update();
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
        return bPosted ? "Проведен" : "Не проведен";
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

    public String getShippingDescription() {
        if (bNeedShipping)
            return getfShippingTotal().toString() + "руб. " + route.getDescription();
        else
            return "Нет";
    }

    public void setForeignObjects() {
        this.setForeignObjectsInProductsAndServices();
        update();

        //this.reload();
    }

    @Override
    public String toString() {
        return "Реализация № " + getsNumber() + " от " + getFormattedDate();
    }

    public void setForeignObjectsInHeader() {
        //this.setAuthor(User.getList(User.class, getAuthorRefKey()));
        //this.setCustomer(Customer.getList(Customer.class, getCustomerRefKey()));
        //this.setContract(Contract.getList(Contract.class, getContractRefKey()));
        //this.setRoute(Route.getList(Route.class, getRoute_Key()));
        //this.setStartingPoint(StartingPoint.getList(StartingPoint.class, getStartingPoint_Key()));

        update();
    }

    void update(){
        try {
            MyHelper.getDocSaleDao().update(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
