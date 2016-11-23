package ru.sk42.tradeodata.XML;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.sk42.tradeodata.Model.Document.DocSale;


/**
 * Created by PostRaw on 14.03.2016.
 */
@Root(name = "entry",strict=false)
@Default(DefaultType.FIELD)
public class WrapperXML_DocSale {

    Content content;

    public WrapperXML_DocSale(DocSale docSale) {
        this.content = new Content(docSale);
    }

    public WrapperXML_DocSale() {
        this.content = new Content();
    }
}

@Default(DefaultType.FIELD)
class Content{

    public Content() {
    }

    @Attribute
    String type = "application/xml";

    @Element(name = "properties")
    @Namespace(reference="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata", prefix = "m")
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
@Namespace(reference="http://schemas.microsoft.com/ado/2007/08/dataservices", prefix="d")
class Properties{
    public Properties() {
        Calendar c = GregorianCalendar.getInstance();
        c.add(Calendar.YEAR,10);
        date = c.getTime();
    }

    @Element(name = "Date")
    @Namespace(reference="http://schemas.microsoft.com/ado/2007/08/dataservices", prefix="d")
    @Convert(DateConverter.class)
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
