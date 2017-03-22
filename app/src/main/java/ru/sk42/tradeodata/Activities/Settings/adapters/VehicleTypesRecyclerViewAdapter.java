package ru.sk42.tradeodata.Activities.Settings.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.List;

import ru.sk42.tradeodata.Activities.Settings.SettingsInterface;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.St;
import ru.sk42.tradeodata.R;

public class VehicleTypesRecyclerViewAdapter extends RecyclerView.Adapter<VehicleTypesRecyclerViewAdapter.ViewHolder> {

    private final List<VehicleType> mValues;
    private final SettingsInterface mListener;
    private int selected;

    public VehicleTypesRecyclerViewAdapter(List<VehicleType> items, SettingsInterface listener) {
        mValues = items;
        mListener = listener;
        selected = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings__vehicletypes_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mVehicleType = mValues.get(position);
        holder.tvVehicleType.setText(holder.mVehicleType.getDescription());
        holder.tvVehicleType.setEnabled(holder.mVehicleType.isEnabled());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckedTextView tvVehicleType;
        public VehicleType mVehicleType;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected = getLayoutPosition();
                    notifyItemChanged(selected);
                    mVehicleType.setEnabled(!mVehicleType.isEnabled());
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onValueSelected(mVehicleType);
                    }
                }
            });
            tvVehicleType = (CheckedTextView) view.findViewById(R.id.list_item_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvVehicleType.getText() + "'";
        }
    }
}
