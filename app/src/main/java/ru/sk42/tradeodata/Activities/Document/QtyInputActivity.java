package ru.sk42.tradeodata.Activities.Document;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.SQLException;

import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.R;

public class QtyInputActivity extends AppCompatActivity implements QtyPickerFragment.OnQtyFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc__qty_input);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        double qty = intent.getDoubleExtra("qty", -1);
        if (id != -1) {

            SaleRecord record = null;
            try {
                record = MyHelper.getSaleRecordProductDao().queryForId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (record != null) {
                record.setQty(qty);
                QtyPickerFragment qtyPickerFragment = QtyPickerFragment.newInstance(record);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_qty_input, qtyPickerFragment, qtyPickerFragment.getClass().getName())
                        .addToBackStack(qtyPickerFragment.getClass().getName())
                        .commit();

            }

        }
    }

    public void onBackPressed(){
        finish();
    }

    @Override
    public void onQtyFragmentInteraction(SaleRecord record) {
        Intent intent = new Intent();
        intent.putExtra("qty", record.getQty());
        intent.putExtra("id", record.getId());
        setResult(0, intent);
        finish();
    }
}