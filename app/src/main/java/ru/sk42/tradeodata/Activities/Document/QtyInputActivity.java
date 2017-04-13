package ru.sk42.tradeodata.Activities.Document;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;

public class QtyInputActivity extends AppCompatActivity implements QtyPickerFragment.OnQtyFragmentInteractionListener {

    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc__qty_input);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        id = intent.getLongExtra(Constants.ID, -1);
        double qty = intent.getDoubleExtra(Constants.QUANTITY, -1);
        double price = intent.getDoubleExtra(Constants.PRICE, -1);
        String descr = intent.getStringExtra(Constants.DESCRIPTION);
        QtyPickerFragment qtyPickerFragment = QtyPickerFragment.newInstance(descr, price, qty);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_qty_input, qtyPickerFragment, qtyPickerFragment.getClass().getName())
                .addToBackStack(qtyPickerFragment.getClass().getName())
                .commit();

    }


    public void onBackPressed() {
        finish();
    }

    @Override
    public void onQtyFragmentInteraction(double qty) {
        Intent intent = getIntent();
        intent.putExtra("qty", qty);
        intent.putExtra(Constants.ID, id);
        setResult(0, intent);
        finish();
    }
}