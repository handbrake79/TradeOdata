package ru.sk42.tradeodata.Model.Catalogs.HelperLists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.Collection;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.Contract;

/**
 * Created by test on 04.03.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractsList {


    @JsonProperty("value")
    private Collection<Contract> values;

    public ContractsList() {}

    public Collection<Contract> getValues() {
        return values;
    }

    public void setValues(Collection<Contract> values) {
        this.values = values;
    }

    public void save() {
        try {
            MyHelper.getContractDao().create(this.values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
