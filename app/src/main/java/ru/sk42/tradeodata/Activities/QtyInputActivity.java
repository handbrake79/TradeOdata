package ru.sk42.tradeodata.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.SQLException;

import ru.sk42.tradeodata.Activities.Document.QtyPickerFragment;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.R;

public class QtyInputActivity extends AppCompatActivity implements QtyPickerFragment.OnQtyFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qty_input);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if (id != -1) {

            SaleRecord record = null;
            try {
                record = MyHelper.getSaleRecordDao().queryForId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (record != null) {
                QtyPickerFragment qtyPickerFragment = QtyPickerFragment.newInstance(record);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_qty_input, qtyPickerFragment, qtyPickerFragment.getClass().getName())
                        .addToBackStack(qtyPickerFragment.getClass().getName())
                        .commit();

            }

        }
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