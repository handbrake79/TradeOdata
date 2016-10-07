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
import ru.sk42.tradeodata.R;

/**
 * Created by test on 21.04.2016.
 */
public class SaleRowProductAdapter extends RecyclerView.Adapter<SaleRowProductAdapter.ViewHolder> {

    private int selectedItem;
    private ArrayList<SaleRowProduct> mValues;

    private MyActivityFragmentInteractionInterface mListener;

    public SaleRowProductAdapter(ArrayList<SaleRowProduct> mValues, MyActivityFragmentInteractionInterface mListener) {
        this.mListener = mListener;
        this.mValues = mValues;
    }

    private SaleRowProduct getSelectedObject() {
        return mValues.get(selectedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_product_row_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedItem == position);
        if (selectedItem == position)
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

        holder.mItem = mValues.get(position);

        if(holder.mItem.getProduct() == null)
            return;

        holder.tvProduct.setText(holder.mItem.getProduct().getDescription());
        holder.tvCharact.setText(holder.mItem.getCharact().getDescription());
        holder.tvQty.setText(holder.mItem.getQty().toString());
        holder.tvPrice.setText(holder.mItem.getPrice().toString());
        holder.tvTotal.setText(holder.mItem.getTotal().toString());
        holder.tvUnit.setText(holder.mItem.getUnit().getDescription());
        holder.tvStore.setText(holder.mItem.getStore().getDescription());
        holder.tvDiscountPercentAuto.setText("Авт. скидка %" + holder.mItem.getDiscountPercentAuto().toString());
        holder.tvDiscountPercentManual.setText("Руч. скидка %" + holder.mItem.getDiscountPercentManual().toString());

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
        public final TextView tvProduct;
        public final TextView tvCharact;
        public final TextView tvQty;
        public final TextView tvPrice;
        public final TextView tvStore;
        public final TextView tvUnit;
        public final TextView tvTotal;
        public final TextView tvDiscountPercentManual;
        public final TextView tvDiscountPercentAuto;
//        public final TextView tvTotal;


        public SaleRowProduct mItem;

        public ViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            //  mView = view;

            tvProduct = (TextView) view.findViewById(R.id.tv_DocSale_Products_Product);
            tvCharact = (TextView) view.findViewById(R.id.tv_DocSale_Products_Charact);
            tvQty = (TextView) view.findViewById(R.id.tv_DocSale_Products_Qty);
            tvUnit = (TextView) view.findViewById(R.id.tv_DocSale_Products_Unit);
            tvPrice = (TextView) view.findViewById(R.id.tv_DocSale_Products_Price);
            tvStore = (TextView) view.findViewById(R.id.tv_DocSale_Products_Store);
            tvTotal = (TextView) view.findViewById(R.id.tv_DocSale_Products_Total);

            tvDiscountPercentManual = (TextView) view.findViewById(R.id.tv_DocSale_Products_DiscountPercentManual);
            tvDiscountPercentAuto = (TextView) view.findViewById(R.id.tv_DocSale_Products_DiscountPercentAuto);


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