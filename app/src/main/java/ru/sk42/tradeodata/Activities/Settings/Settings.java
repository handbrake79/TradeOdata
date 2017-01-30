package ru.sk42.tradeodata.Activities.Settings;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;

/**
 * Created by хрюн моржов on 20.10.2016.
 */

@DatabaseTable
public class Settings {

    @DatabaseField
    private int scannerStartDelayMillis;

    @DatabaseField
    private String serverAddress;

    @DatabaseField
    private String baseName;

    @DatabaseField
    private String login;

    @DatabaseField
    private String password;

    public int getScannerStartDelayMillis() {
        return scannerStartDelayMillis;
    }

    public void setScannerStartDelayMillis(int scannerStartDelayMillis) {
        this.scannerStartDelayMillis = scannerStartDelayMillis;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public static User getCurrentUserStatic() {
        User user = getSettings().getCurrentUser();
        if (user == null) {
            return User.getInstance();
        } else {
            return getSettings().getCurrentUser();
        }
    }

    public static void setCurrentUserStatic(User user) {
        Settings settings = getSettings();
        settings.currentUser = user;
        settings.save();
    }

    @DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private VehicleType defaultVehicleType;

    public VehicleType getDefaultVehicleType() {
        return defaultVehicleType;
    }

    public void setDefaultVehicleType(VehicleType defaultVehicleType) {
        this.defaultVehicleType = defaultVehicleType;
    }

    public Settings() {
    }

    @DatabaseField(id = true)
    int id;

    @DatabaseField
    String lastViewedProductGroup;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    StartingPoint defaultStartingPoint;

    public String getLastViewedProductGroup() {
        return lastViewedProductGroup;
    }

    public void setLastViewedProductGroup(String lastViewedProductGroup) {
        this.lastViewedProductGroup = lastViewedProductGroup;
    }

    public StartingPoint getDefaultStartingPoint() {
        return defaultStartingPoint;
    }

    public void setDefaultStartingPoint(StartingPoint defaultStartingPoint) {
        this.defaultStartingPoint = defaultStartingPoint;
    }

    public static VehicleType getDefaultVehicleTypeStatic() {
        return getSettings().defaultVehicleType;
    }

    public static void setDefaultVehicleTypeStatic(VehicleType val) {
        Settings settings = getSettings();
        settings.defaultVehicleType = val;
        settings.save();
    }

    public int getId() {
        return 0;
    }

    public void setId(int id) {
        this.id = 0;
    }

    public static StartingPoint getDefaultStartingPointStatic() {
        return getSettings().defaultStartingPoint;
    }

    public static void setDefaultStartingPointStatic(StartingPoint defaultStartingPoint) {
        Settings settings = getSettings();
        settings.defaultStartingPoint = defaultStartingPoint;
        settings.save();
    }

    public static String getLastViewedProductGroupStatic() {
        String val = getSettings().lastViewedProductGroup;
        if (val == null) {
            return Constants.ZERO_GUID;
        } else {
            return val;
        }
    }

    public static void setLastViewedProductGroupStatic(String val) {
        Settings settings = getSettings();
        settings.lastViewedProductGroup = val;
        settings.save();
    }

    protected static Settings getSettings() {
        Settings settings = null;
        try {
            settings = MyHelper.getSettingsDao().queryForAll().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (settings == null) {
            settings = new Settings();
            settings.setScannerStartDelayMillis(500);
            settings.setBaseName("ut836");
            settings.setServerAddress("192.168.0.171");
            settings.setLogin("kogan");
            settings.setPassword("1");
            settings.save();
        }
        return settings;
    }

    protected void save() {
        try {
            MyHelper.getSettingsDao().createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getScannerStartDelayMillisStatic(){
        return getSettings().getScannerStartDelayMillis();
    }

    public static String getServerAddressStatic(){
        return "http://" + getSettings().serverAddress + "/";
    }
    public static String getInfoBaseNameStatic(){
        return getSettings().baseName;
    }
    public static String getLoginStatic(){
        return getSettings().login;
    }
    public static String getPasswordStatic(){
        return getSettings().password;
    }


}
