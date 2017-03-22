package ru.sk42.tradeodata.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by хрюн моржов on 16.02.2017.
 */

public class PrintResult {
    @JsonProperty("Result")
    String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
