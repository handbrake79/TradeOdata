package ru.sk42.tradeodata.Model.Catalogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.NetworkRequests.RetroConstants;


/**
 * Created by PostRaw on 14.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable
public class DiscountCard extends CDO {

    public static DiscountCard newInstance() {
        DiscountCard card = DiscountCard.getObject(DiscountCard.class, Constants.ZERO_GUID);
        if(card == null) {
            card = new DiscountCard();
            card.setRef_Key(Constants.ZERO_GUID);
            card.setDescription("");
            card.save();
            return card;
        }
        else {
            return card;
        }
    }

    @DatabaseField(id = true, columnName = "Ref_Key")
    @JsonProperty("Ref_Key")
    private String ref_Key;

    @DatabaseField
    @JsonProperty("КодКарты")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty
    @DatabaseField
    private String Description;

    public DiscountCard(String ref_Key) {
        this.setRef_Key(ref_Key);
    }

    public DiscountCard() {
    }

    public String getDescription() {
        return Description;
    }

    @Override
    public void setDescription(String description) {
        Description = description;
    }

    public String getRef_Key() {
        return ref_Key;
    }

    @Override
    public void setRef_Key(String ref_Key) {
        this.ref_Key = ref_Key;
    }

    public String getMaintainedTableName() {
        return "Catalog_ИнформационныеКарты";
    }

    @Override
    public Dao<DiscountCard, Object> getDao() {
        return MyHelper.getDiscountCardDao();
    }

    @Override
    public String getRetroFilterString() {
        return RetroConstants.FILTERS.DISCOUNT_CARDS;
    }


    public boolean isEmpty() {
        return ref_Key.equals(Constants.ZERO_GUID);
    }

    @Override
    public void save() {

        try {
            MyHelper.getInstance().getDao(DiscountCard.class).createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.getDescription().toString();
    }

}
