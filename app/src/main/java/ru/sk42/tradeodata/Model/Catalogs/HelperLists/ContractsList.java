package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.sk42.tradeodata.Model.Catalogs.Contract;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Helpers.Helper;

/**
 * Created by test on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractsList {


    @JsonProperty("value")
    private Collection<Contract> values;

    public ContractsList() {}

    public void setValues(Collection<Contract> values) {
        this.values = values;
    }

    public Collection<Contract> getValues() {
        return values;
    }


    public void save() {
        try {
            Helper.getContractDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
