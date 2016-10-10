package ru.sk42.tradeodata.Model.DocumentRequisites;

import java.util.ArrayList;

import ru.sk42.tradeodata.Model.Documents.DocSale;

/**
 * Created by test on 21.04.2016.
 */
public class Requisites {
    private ArrayList<Requisite> requisites;

    public Requisites() {
        this.requisites = new ArrayList<>();
    }

    public static Requisites NewInstance(DocSale docSale) {
        Requisites rq = new Requisites();

        rq.add("Номер", docSale.getNumber());
        rq.add("Дата", docSale.getFormattedDate());
        rq.add(docSale.getbPosted() ? "Проведен" : "Не проведен", "");
        rq.add("Покупатель", docSale.getCustomer().toString());
        rq.add("Договор", docSale.getContract().toString());
        rq.add("Автор", docSale.getAuthor().toString());
        rq.add("Товаров", docSale.getProducts().size());
        rq.add("Услуг", docSale.getServices().size());
        rq.add("На сумму", docSale.getTotal());
        rq.add("Доставка", docSale.getShippingDescription());


        return rq;
    }

    private void add(String description, Object object) {
        Requisite r = new Requisite(description, object);
        this.requisites.add(r);
    }

    public ArrayList<Requisite> getRequisites() {
        return requisites;
    }

    public void setRequisites(ArrayList<Requisite> requisites) {
        this.requisites = requisites;
    }

    public ArrayList<Requisite> getArrayList() {
        return requisites;
    }
}
