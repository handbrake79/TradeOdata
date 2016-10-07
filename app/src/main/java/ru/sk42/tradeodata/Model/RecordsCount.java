package ru.sk42.tradeodata.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by я on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordsCount {
    @JsonProperty("odata.count")
    Integer count;

    public RecordsCount() {
    }

    public Integer getCount() {
        return count;
    }

    public RecordsCount setCount(Integer count) {
        this.count = count;
        return this;
    }
}
