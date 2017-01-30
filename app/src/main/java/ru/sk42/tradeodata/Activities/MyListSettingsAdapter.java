package ru.sk42.tradeodata.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;

/**
 * Created by хрюн моржов on 30.11.2016.
 */
public class MyListSettingsAdapter<T> extends ArrayAdapter<String> {
    Context context;

    public MyListSettingsAdapter(Context context, int layout, ArrayList list) {
        super(context, layout, list);
        this.context = context;
    }

    @Override
    public View getView(int position, View coverView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_layout,
                parent, false);

        String action =  Constants.SETTINGS_ACTIONS.get(position);
        TextView tvAction = (TextView)rowView.findViewById(R.id.list_item_text);
        tvAction.setText(action);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_item_icon);
        switch (position){
            case 1:
                imageView.setImageResource(R.drawable.ic_settings_black_24dp);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_account_box_black_24dp);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_print_black_24dp);
                break;
            case 4:
                imageView.setImageResource(R.drawable.barcode_scan);
                break;
            case 5:
                break;
        }

        return rowView;

    }

}
