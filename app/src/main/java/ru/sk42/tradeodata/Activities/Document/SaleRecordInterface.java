package ru.sk42.tradeodata.Activities.Document;

import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.Model.Document.SaleRecordProduct;

/**
 * Created by хрюн моржов on 15.11.2016.
 */

public interface SaleRecordInterface {
    void plus(SaleRecord record);

    void minus(SaleRecord record);

    void removeRecord(SaleRecord record);

    void onRecordSelected(SaleRecord record, int action);

    void onQtyChanged(SaleRecord record, int action);

}
