package ru.sk42.tradeodata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

import ru.sk42.tradeodata.Model.Catalogs.Contract;
import ru.sk42.tradeodata.Model.Documents.DocSale;

/**
 * Created by я on 06.10.2016.
 */
@DatabaseTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tttest {

    public Tttest(){}

    @DatabaseField(generatedId = true)
    Integer id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("ДоговорКонтрагента_Key")
    Contract contract;

    public Integer getId() {
        return id;
    }

    public Tttest setId(Integer id) {
        this.id = id;
        return this;
    }

    public Contract getContract() {
        return contract;
    }

    public Tttest setContract(Contract contract) {
        this.contract = contract;
        return this;
    }
}
