package ru.sk42.tradeodata.Activities.Settings.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.sk42.tradeodata.Activities.Settings.SettingsInterface;
import ru.sk42.tradeodata.Model.Printer;
import ru.sk42.tradeodata.Model.St;
import ru.sk42.tradeodata.R;

public class PrintersRecyclerViewAdapter extends RecyclerView.Adapter<PrintersRecyclerViewAdapter.ViewHolder> {

    private final List<Printer> mValues;
    private final SettingsInterface mListener;
    private int selected;

    public PrintersRecyclerViewAdapter(List<Printer> items, SettingsInterface listener) {
        mValues = items;
        mListener = listener;
        selected = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings__printers_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (selected == position) {
            holder.itemView.setBackgroundColor(St.getApp().getResources().getColor(R.color.bpRed));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
        holder.tvPrinter.setText(mValues.get(position).getPrinterName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvPrinter;
        public final ImageView ivIcon;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(selected);
                    selected = getLayoutPosition();
                    notifyItemChanged(selected);
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onValueSelected(tvPrinter.getText());
                    }
                }
            });
            tvPrinter = (TextView) view.findViewById(R.id.list_item_text);
            ivIcon = (ImageView) view.findViewById(R.id.list_item_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvPrinter.getText() + "'";
        }
    }
}
