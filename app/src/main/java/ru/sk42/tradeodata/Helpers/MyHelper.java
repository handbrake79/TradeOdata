package ru.sk42.tradeodata.Helpers;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.Contract;
import ru.sk42.tradeodata.Model.Catalogs.Currency;
import ru.sk42.tradeodata.Model.Catalogs.Customer;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ProductsList;
import ru.sk42.tradeodata.Model.Catalogs.Organisation;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.Unit;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.DocSaleList;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.Model.Document.SaleRecordProduct;
import ru.sk42.tradeodata.Model.Document.SaleRecordService;
import ru.sk42.tradeodata.Model.InformationRegisters.ShippingRate;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.Model.Stock;


public class MyHelper extends OrmLiteSqliteOpenHelper {

    private Context context;
    private static MyHelper instance;
    private static Dao<ProductsList, Object> productsListDao;

    public MyHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    public MyHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        this.context = context;
    }

    public MyHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, int configFileId) {
        super(context, databaseName, factory, databaseVersion, configFileId);
        this.context = context;
    }

    public MyHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, File configFile) {
        super(context, databaseName, factory, databaseVersion, configFile);
        this.context = context;
    }

    public MyHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, InputStream stream) {
        super(context, databaseName, factory, databaseVersion, stream);
        this.context = context;
    }


    public static MyHelper getInstance(Application application) {

        if (instance == null) {
            instance = new MyHelper(application, "PostRaw.db", 1);
        }
        return instance;
    }

    public static MyHelper getInstance() {
        return instance;
    }

    public static void createTables() {
        try {

            TableUtils.createTableIfNotExists(getInstance().connectionSource, Settings.class);

            //Справочники
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Charact.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Contract.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Currency.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Customer.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, DiscountCard.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Organisation.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Product.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Route.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, StartingPoint.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Store.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Unit.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, User.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, VehicleType.class);


            TableUtils.createTableIfNotExists(getInstance().connectionSource, DocSale.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, SaleRecordProduct.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, SaleRecordService.class);

            TableUtils.createTableIfNotExists(getInstance().connectionSource, ProductInfo.class);
            TableUtils.createTableIfNotExists(getInstance().connectionSource, Stock.class);

            TableUtils.createTableIfNotExists(getInstance().connectionSource, ShippingRate.class);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropAndCreateTables() {
        getInstance().context.deleteDatabase("PostRaw.db");
        createTables();
    }

    public static Dao<DocSale, Object> getDocSaleDao() {
        try {
            return getInstance().getDao(DocSale.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<DocSaleList, Object> getDocSaleListDao() {
        try {
            return getInstance().getDao(DocSaleList.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<SaleRecordProduct, Object> getSaleRecordProductDao() {
        try {
            return getInstance().getDao(SaleRecordProduct.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<SaleRecordService, Object> getSaleRowServiceDao() {
        try {
            return getInstance().getDao(SaleRecordService.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Store, Object> getStoreDao() {
        try {
            return getInstance().getDao(Store.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Charact, Object> getCharactDao() {
        try {
            return getInstance().getDao(Charact.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<User, Object> getUserDao() {
        try {
            return getInstance().getDao(User.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Customer, Object> getCustomerDao() {
        try {
            return getInstance().getDao(Customer.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Contract, Object> getContractDao() {
        try {
            return getInstance().getDao(Contract.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<ProductInfo, Object> getProductInfoDao() {
        try {
            return getInstance().getDao(ProductInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Stock, Object> getStockDao() {
        try {
            return getInstance().getDao(Stock.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Product, Object> getProductDao() {

        try {
            return getInstance().getDao(Product.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Dao<Unit, Object> getUnitDao() {
        try {
            return getInstance().getDao(Unit.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<VehicleType, Object> getVehicleTypesDao() {
        try {
            return getInstance().getDao(VehicleType.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Route, Object> getRouteDao() {
        try {
            return getInstance().getDao(Route.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<StartingPoint, Object> getStartingPointDao() {
        try {
            return getInstance().getDao(StartingPoint.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Currency, Object> getCurrencyDao() {
        try {
            return getInstance().getDao(Currency.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<Organisation, Object> getOrganisationDao() {
        try {
            return getInstance().getDao(Organisation.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<DiscountCard, Object> getDiscountCardDao() {
        try {
            return getInstance().getDao(DiscountCard.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public void deleteDocSaleList() {

        try {
            Dao<SaleRecordProduct, Object> daoSaleRecordProduct = getDao(SaleRecordProduct.class);
            daoSaleRecordProduct.delete(daoSaleRecordProduct.queryForAll());

            Dao<SaleRecordService, Object> daos = getDao(SaleRecordService.class);
            daos.delete(daos.queryForAll());

            Dao<DocSale, Object> daoD = getInstance().getDao(DocSale.class);
            daoD.delete(daoD.queryForAll());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Dao<Settings, Object> getSettingsDao() {
        try {
            return getInstance().getDao(Settings.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Dao<ShippingRate, Object> getShippingRouteDao() {
        try {
            return getInstance().getDao(ShippingRate.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static Dao<SaleRecord, Object> getSaleRecordDao() {

        try {
            return getInstance().getDao(SaleRecord.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static long getDocumentCountOnDate(Date dateBegin) {
        Date dateEnd = Uttils.getEndOfDay(dateBegin);

        try {
            Dao<DocSale, Object> dao = MyHelper.getDocSaleDao();
            QueryBuilder<DocSale, Object> queryBuilder = dao.queryBuilder();
            Where<DocSale, Object> where = queryBuilder.where();
            where.between("date", dateBegin, dateEnd);
            PreparedQuery<DocSale> preparedQuery = queryBuilder.prepare();
            List<DocSale> list = dao.query(preparedQuery);
            return list.size();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}


