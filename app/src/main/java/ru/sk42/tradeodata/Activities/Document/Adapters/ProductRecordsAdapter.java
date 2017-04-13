package ru.sk42.tradeodata.Activities.Document.Adapters;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.SaleRecordInterface;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.Model.Document.SaleRecordProduct;
import ru.sk42.tradeodata.R;

/**
 * Created by PostRaw on 21.04.2016.
 */
public class ProductRecordsAdapter extends RecyclerView.Adapter<ProductRecordsAdapter.ViewHolder> {

    static final String TAG = "ProductRecordsAdapter";
    private int selectedItem;
    private ArrayList<SaleRecordProduct> mValues;

    private SaleRecordInterface mListener;

    public ProductRecordsAdapter(ArrayList<SaleRecordProduct> mValues, SaleRecordInterface mListener) {
        this.mListener = mListener;
        this.mValues = mValues;
    }

    public void setValues(ArrayList<SaleRecordProduct> values) {
        mValues = values;
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
        holder.mItem = mValues.get(position);

        if (holder.mItem.getProduct() == null)
            return;

        holder.tvDocSaleProductsProduct.setText(holder.mItem.getProduct().getDescription());
        holder.tvDocSaleProductsCharact.setText(holder.mItem.getCharact().getDescription());
        if (holder.mItem.getCharact().isEmpty()) {
            holder.tvDocSaleProductsCharact.setVisibility(View.GONE);
        }


        holder.tvDocSaleProductsQty.setText(Uttils.formatDoubleToQty(holder.mItem.getQty()));
        holder.tvDocSaleProductsPrice.setText(Uttils.formatDoubleToMoney(holder.mItem.getPrice()));
        holder.tvDocSaleProductsTotal.setText(Uttils.formatDoubleToMoney(holder.mItem.getTotal()));
        holder.tvDocSaleProductsUnit.setText(holder.mItem.getUnit().getDescription());
        holder.tvDocSaleProductsStore.setText(holder.mItem.getStore().getDescription());
        if (holder.mItem.getDiscountPercentAuto() > 0) {
            holder.tvDocSaleProductsDiscountPercentAuto.setText("скидка %" + holder.mItem.getDiscountPercentAuto().toString());
        } else {
            holder.tvDocSaleProductsDiscountPercentAuto.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateValue(SaleRecord record) {
        int pos = -1;
        Iterator<SaleRecordProduct> it = mValues.iterator();
        while (it.hasNext()) {
            SaleRecord currentRecord = it.next();
            pos++;
            if (currentRecord.getId() == record.getId()) {
                currentRecord.setQty(record.getQty());
                currentRecord.setTotal(record.getTotal());
                currentRecord.setPrice(record.getPrice());
                this.notifyItemChanged(pos);
                break;
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        @Bind(R.id.doc__product_card_product)
        TextView tvDocSaleProductsProduct;

        @Bind(R.id.doc__product_card_charact)
        TextView tvDocSaleProductsCharact;

        @Bind(R.id.doc__product_card_store)
        TextView tvDocSaleProductsStore;

        @Bind(R.id.tv_DocSale_Products_Qty)
        EditText tvDocSaleProductsQty;

        @Bind(R.id.tv_DocSale_Products_Unit)
        TextView tvDocSaleProductsUnit;

        @Bind(R.id.doc__product_card_price)
        TextView tvDocSaleProductsPrice;

        @Bind(R.id.doc__product_card_total)
        TextView tvDocSaleProductsTotal;


        @Bind(R.id.tv_DocSale_Products_DiscountPercentAuto)
        TextView tvDocSaleProductsDiscountPercentAuto;

        @Bind(R.id.product_record_plus)
        TextView tvPlus;

        @Bind(R.id.product_record_minus)
        TextView tvMinus;

        @Bind(R.id.doc__product_card_delete_button)
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

            tvDocSaleProductsQty.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == 0) {
                        String text = tvDocSaleProductsQty.getText().toString();
                        Log.d(TAG, "onKey: text entered=" + text);
                        double qty = Double.valueOf(text);
                        if (qty > 0) {
                            notifyItemChanged(selectedItem);
                            selectedItem = getLayoutPosition();
                            notifyItemChanged(selectedItem);
                            getSelectedObject().setQty(qty);
                            mListener.onQtyChanged(getSelectedObject(), Constants.QTY_CHANGED);
                            return true;
                        } else {
                            return false;
                        }

                    } else {
                        return false;
                    }
                }
            });

            // Handle item click and set the selection
            tvDocSaleProductsProduct.setOnClickListener(new View.OnClickListener() {
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
                    mListener.removeRecord(getSelectedObject());
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            String[] menuItems = {"Удалить товар"};
            for (int i = 0; i < menuItems.length; i++) {
                contextMenu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }

    }


}
