package ru.sk42.tradeodata.Activities.Document;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.Documents.SaleRowProduct;
import ru.sk42.tradeodata.Model.Documents.SaleRowService;
import ru.sk42.tradeodata.R;

/**
 * Created by test on 21.04.2016.
 */
public class SaleRowServiceAdapter extends RecyclerView.Adapter<SaleRowServiceAdapter.ViewHolder> {

    private int selectedItem;
    private ArrayList<SaleRowService> mValues;

    private MyActivityFragmentInteractionInterface mListener;

    public SaleRowServiceAdapter(ArrayList<SaleRowService> mValues, MyActivityFragmentInteractionInterface mListener) {
        this.mListener = mListener;
        this.mValues = mValues;
    }

    private SaleRowService getSelectedObject() {
        return mValues.get(selectedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_service_row_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedItem == position);
        if (selectedItem == position)
            holder.itemView.setBackgroundColor(Color.RED);
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

        holder.mItem = mValues.get(position);

        holder.tvProduct.setText(holder.mItem.getProduct().getDescription());
        holder.tvQty.setText(holder.mItem.getQty().toString());
        holder.tvPrice.setText(holder.mItem.getPrice().toString());
        holder.tvTotal.setText(holder.mItem.getTotal().toString());

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
        public SaleRowService mItem;

        public ViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            //  mView = view;

            tvProduct = (TextView) view.findViewById(R.id.tv_DocSale_Services_Product);
            tvQty = (TextView) view.findViewById(R.id.tv_DocSale_Services_Qty);
            tvPrice = (TextView) view.findViewById(R.id.tv_DocSale_Services_Price);
            tvTotal = (TextView) view.findViewById(R.id.tv_DocSale_Services_Total);


            // Handle item click and set the selection
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                    mListener.onItemSelection(getSelectedObject());
                }
            });
        }


    }

}
