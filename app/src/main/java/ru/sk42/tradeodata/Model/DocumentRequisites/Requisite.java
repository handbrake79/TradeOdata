package ru.sk42.tradeodata.Model.DocumentRequisites;

/**
 * Created by test on 21.04.2016.
 */
public class Requisite {
    String description;
    Object object;

    public Requisite(String description, Object object) {
        this.description = description;
        this.object = object;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValueString() {
        return object.toString();
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
