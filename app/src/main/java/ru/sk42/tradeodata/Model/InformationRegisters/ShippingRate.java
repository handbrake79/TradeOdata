package ru.sk42.tradeodata.Model.InformationRegisters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;

/**
 * Created by хрюн моржов on 21.10.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingRate extends CDO {

    @DatabaseField(generatedId = true)
    int id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @DatabaseField
    Date date;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ТипТС_Key")
    VehicleType vehicleType;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("НачальнаяТочкаМаршрута_Key")
    StartingPoint startingPoint;

    @DatabaseField
    @JsonProperty("Стоимость")
    int cost;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("Маршрут_Key")
    Route route;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public StartingPoint getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(StartingPoint startingPoint) {
        this.startingPoint = startingPoint;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public void save() throws SQLException {

        try {
            MyHelper.getInstance().getDao(ShippingRate.class).create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRef_Key() {

        throw new RuntimeException("method getRef_Key() not supported, becaouse there is no such field!");
    }

    @Override
    public void setRef_Key(String s) {
        throw new RuntimeException("method setRef_Key() not supported, becaouse there is no such field!");

    }

    @Override
    public void setDescription(String s) {
        throw new RuntimeException("method setDescription() not supported, becaouse there is no such field!");

    }

    @Override
    public boolean isEmpty() {
        throw new RuntimeException("method isEmpty() not supported, becaouse there is no such field!");
    }

    @Override
    public String getMaintainedTableName() {
        return "InformationRegister_ТарифыПеревозкиПоМаршрутам/SliceLast()";
    }

    @Override
    public String getRetroFilterString() {
        return "";
    }

    @Override
    public Dao<ShippingRate, Object> getDao() {
        return MyHelper.getShippingRouteDao();
    }

    public static int getCost(StartingPoint startingPoint, Route route, VehicleType vehicleType) {

        try {
            Dao<ShippingRate, Object> dao = MyHelper.getShippingRouteDao();
            QueryBuilder<ShippingRate, Object> queryBuilder = dao.queryBuilder();
            Where<ShippingRate, Object> where = queryBuilder.where();
            where.eq("startingPoint_id", startingPoint);
            where.and();
            where.eq("route_id", route);
            where.and();
            where.eq("vehicleType_id", vehicleType);
            PreparedQuery<ShippingRate> preparedQuery = queryBuilder.prepare();
            List<ShippingRate> list = dao.query(preparedQuery);
            if(list.isEmpty()){
                return 0;
            }
            else {
                return list.get(0).getCost();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0;
    }
}
