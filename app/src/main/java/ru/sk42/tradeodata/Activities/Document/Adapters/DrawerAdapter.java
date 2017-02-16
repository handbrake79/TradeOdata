package ru.sk42.tradeodata.Activities.Document.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sk42.tradeodata.Model.St;
import ru.sk42.tradeodata.R;

/**
 * Created by хрюн моржов on 30.11.2016.
 */
public class DrawerAdapter<T> extends ArrayAdapter<String> {
    Context context;
    private ArrayList<String> list = new ArrayList<>();

    public DrawerAdapter(Context context, int layout, ArrayList list) {
        super(context, layout, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View coverView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.drawer_list_item_layout,
                parent, false);

        String action =  list.get(position);
        TextView tvAction = (TextView)rowView.findViewById(R.id.list_item_text);
        tvAction.setText(action);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_item_icon);
        if(St.getApp().getResources().getString(R.string.ACTION_SAVE).equals(action)){
            imageView.setImageResource(R.drawable.ic_save_black_24dp);
        }
        if(St.getApp().getResources().getString(R.string.ACTION_SAVE_1C).equals(action)){
            imageView.setImageResource(R.drawable.ic_done_black_24dp);
        }
        if(St.getApp().getResources().getString(R.string.ACTION_POST_1C).equals(action)){
            imageView.setImageResource(R.drawable.ic_done_all_black_24dp);
        }
        if(St.getApp().getResources().getString(R.string.ACTION_PRINT).equals(action)){
            imageView.setImageResource(R.drawable.ic_print_black_24dp);
        }

        return rowView;

    }

}
