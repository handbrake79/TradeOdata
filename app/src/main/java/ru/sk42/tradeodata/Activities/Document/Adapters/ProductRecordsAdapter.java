package ru.sk42.tradeodata.Activities.Document.Adapters;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.SaleRecordInterface;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.SaleRecordProduct;
import ru.sk42.tradeodata.R;

/**
 * Created by PostRaw on 21.04.2016.
 */
public class ProductRecordsAdapter extends RecyclerView.Adapter<ProductRecordsAdapter.ViewHolder> {

    private int selectedItem;
    private ArrayList<SaleRecordProduct> mValues;

    private SaleRecordInterface mListener;

    public ProductRecordsAdapter(ArrayList<SaleRecordProduct> mValues, SaleRecordInterface mListener) {
        this.mListener = mListener;
        this.mValues = mValues;
    }

    private SaleRecordProduct getSelectedObject() {
        return mValues.get(selectedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc__product_row_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedItem == position);
        if (selectedItem == position)
            holder.itemView.setBackgroundColor(Constants.COLORS.SELECTED_COLOR); //without theme);
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

        holder.mItem = mValues.get(position);

        if (holder.mItem.getProduct() == null)
            return;

        holder.tvDocSaleProductsProduct.setText(holder.mItem.getProduct().getDescription());
        holder.tvDocSaleProductsCharact.setText(holder.mItem.getCharact().getDescription());
        if(holder.mItem.getCharact().isEmpty())
        {
            holder.tvDocSaleProductsCharact.setVisibility(View.GONE);
        }


        holder.tvDocSaleProductsQty.setText(Uttils.formatDoubleToQty(holder.mItem.getQty()));
        holder.tvDocSaleProductsPrice.setText(Uttils.formatDoubleToMoney(holder.mItem.getPrice()));
        holder.tvDocSaleProductsTotal.setText(Uttils.formatDoubleToMoney(holder.mItem.getTotal()));
        holder.tvDocSaleProductsUnit.setText(holder.mItem.getUnit().getDescription());
        holder.tvDocSaleProductsStore.setText(holder.mItem.getStore().getDescription());
        holder.tvDocSaleProductsDiscountPercentAuto.setText("Авт. скидка %" + holder.mItem.getDiscountPercentAuto().toString());
        holder.tvDocSaleProductsDiscountPercentManual.setText("Руч. скидка %" + holder.mItem.getDiscountPercentManual().toString());

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        @Bind(R.id.tv_DocSale_Products_Product)
        TextView tvDocSaleProductsProduct;

        @Bind(R.id.tv_DocSale_Products_Charact)
        TextView tvDocSaleProductsCharact;

        @Bind(R.id.tv_DocSale_Products_Store)
        TextView tvDocSaleProductsStore;

        @Bind(R.id.tv_DocSale_Products_Qty)
        TextView tvDocSaleProductsQty;

        @Bind(R.id.tv_DocSale_Products_Unit)
        TextView tvDocSaleProductsUnit;

        @Bind(R.id.product_record_price)
        TextView tvDocSaleProductsPrice;

        @Bind(R.id.tv_DocSale_Products_Total)
        TextView tvDocSaleProductsTotal;


        @Bind(R.id.tv_DocSale_Products_DiscountPercentAuto)
        TextView tvDocSaleProductsDiscountPercentAuto;

        @Bind(R.id.tv_DocSale_Products_DiscountPercentManual)
        TextView tvDocSaleProductsDiscountPercentManual;

        @Bind(R.id.product_record_plus)
        TextView tvPlus;

        @Bind(R.id.product_record_minus)
        TextView tvMinus;

        @Bind(R.id.order_product_row_delete_button)
        ImageButton mDeleteButton;

        public SaleRecordProduct mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setClickable(true);
            itemView.setOnCreateContextMenuListener(this);

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
            tvDocSaleProductsQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                    mListener.onRecordSelected(getSelectedObject(), Constants.SELECT_RECORD_FOR_CHANGE);
                }
            });

            // Handle item click and set the selection
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                    mListener.onRecordSelected(getSelectedObject(), Constants.SELECT_RECORD_FOR_VIEW_PRODUCT);
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItem = getLayoutPosition();
                    mListener.deleteRecord(getSelectedObject());
                }
            });
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            String[] menuItems = {"Удалить товар", "2", "3"};
            for (int i = 0; i < menuItems.length; i++) {
                contextMenu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }

    }


}
