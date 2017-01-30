package ru.sk42.tradeodata.XML;

import android.util.Log;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.sk42.tradeodata.Model.Document.DocSale;


/**
 * Created by PostRaw on 14.03.2016.
 */
@Root(name = "entry", strict = false)
@Default(DefaultType.FIELD)
@Order(elements = {
        "category",
        "title",
        "updated",
        "author",
        "summary",
        "content"
})
public class WrapperXML_DocSale {

    @Element(required = true, name = "category")
    String category = "";

    @Attribute
    String term = "StandardODATA.Catalog_Товары";

    @Attribute
    String scheme = "http://schemas.microsoft.com/ado/2007/08/dataservices/scheme";

    @Element(required = true, name = "title")
    String title = "";
    @Attribute
    String type = "text";

    @Element
    @Convert(DateConverter.class)
    Date updated = GregorianCalendar.getInstance().getTime();

    @Element
    String author = "";
    @Element
    String summary = "";

    Content content;

    public WrapperXML_DocSale(DocSale docSale) {
        this.content = new Content(docSale);
    }

    public WrapperXML_DocSale() {
        this.content = new Content();
    }

    public static String writeDocSaleToXMLString(DocSale docSale) {
        String xmlString;

        Registry registry = new Registry();
        Strategy strategy = new RegistryStrategy(registry);
        DateConverter dateConverter = new DateConverter();
        try {
            registry.bind(Date.class, dateConverter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Persister persister = new Persister(strategy);
        StringWriter writer = new StringWriter();

        WrapperXML_DocSale XMLDocSaleWrapper = new WrapperXML_DocSale(docSale);


        try {
            persister.write(XMLDocSaleWrapper, writer);
        } catch (Exception e) {
            Log.d("*** 1c communication", "writeDocSaleToXMLString: " + e.getLocalizedMessage().toString());
            //e.printStackTrace();

        }

        xmlString = "<?xml  version=\"1.0\" encoding=\"utf-8\"?>\n" + writer.toString();
        return xmlString;
    }
}

@Default(DefaultType.FIELD)
class Content {

    public Content() {
    }


    @Attribute
    String type = "application/xml";

    @Element(name = "properties")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata", prefix = "m")
    DocSale docSale;

    public Content(DocSale docSale) {
        this.docSale = docSale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DocSale getDocSale() {
        return docSale;
    }

    public void setDocSale(DocSale docSale) {
        this.docSale = docSale;
    }
}

@Default(DefaultType.FIELD)
@Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
class Properties {
    public Properties() {
        Calendar c = GregorianCalendar.getInstance();
        c.add(Calendar.YEAR, 10);
        date = c.getTime();
    }

    @Element(name = "Date")
    @Namespace(reference = "http://schemas.microsoft.com/ado/2007/08/dataservices", prefix = "d")
    @Convert(DateConverter.class)
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
