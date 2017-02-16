package ru.sk42.tradeodata.Activities.Document.Adapters;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.SaleRecordInterface;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.SaleRecordService;
import ru.sk42.tradeodata.R;

/**
 * Created by PostRaw on 21.04.2016.
 */
public class ServicesRecordsAdapter extends RecyclerView.Adapter<ServicesRecordsAdapter.ViewHolder> {

    private int selectedItem;
    private ArrayList<SaleRecordService> mValues;

    private SaleRecordInterface mListener;

    public ServicesRecordsAdapter(ArrayList<SaleRecordService> mValues, SaleRecordInterface mListener) {
        this.mListener = mListener;
        this.mValues = mValues;
    }

    private SaleRecordService getSelectedObject() {
        return mValues.get(selectedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc__service_row_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedItem == position);
        if (selectedItem == position)
            holder.itemView.setBackgroundColor(Constants.COLORS.SELECTED_COLOR);
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

        holder.mItem = mValues.get(position);

        holder.tvProduct.setText(holder.mItem.getProduct().getDescription());
        holder.tvQty.setText(Uttils.formatDoubleToQty(holder.mItem.getQty()));
        holder.tvPrice.setText(Uttils.formatDoubleToMoney(holder.mItem.getPrice()));
        holder.tvTotal.setText(Uttils.formatDoubleToMoney(holder.mItem.getTotal()));

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
        public final TextView tvProduct;
        public final TextView tvQty;
        public final TextView tvPrice;
        public final TextView tvTotal;
        public SaleRecordService mItem;

        @Bind(R.id.doc__servicecard_plus)
        TextView tvPlus;

        @Bind(R.id.doc__servicecard_minus)
        TextView tvMinus;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            itemView.setClickable(true);
            //  mView = view;

            tvProduct = (TextView) view.findViewById(R.id.doc__servicecard_product);
            tvQty = (TextView) view.findViewById(R.id.doc__servicecard_qty);
            tvPrice = (TextView) view.findViewById(R.id.doc__servicecard_price);
            tvTotal = (TextView) view.findViewById(R.id.tv_DocSale_Services_Total);

            tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItem = getLayoutPosition();
                    mListener.plus(getSelectedObject());
                }
            });

            tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItem = getLayoutPosition();
                    mListener.minus(getSelectedObject());
                }
            });

            // Handle item click and set the selection
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Uttils.isPredefined(getSelectedObject().getProduct())){
                        return;
                    }
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                    mListener.onRecordSelected(getSelectedObject(), Constants.SELECT_RECORD_FOR_VIEW_PRODUCT);
                }
            });
        }


    }

}
