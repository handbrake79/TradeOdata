package ru.sk42.tradeodata.Activities.ProductInfo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

//implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
public class ProductInfo_Adapter extends RecyclerView.Adapter<ProductInfo_Adapter.ViewHolder> {

    private final List<Stock> mValues;
    private Integer selectedItem;

    public ProductInfo_Adapter(List<Stock> items, InteractionInterface listener) {
        selectedItem = -1;

        mValues = items;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_info_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedItem == position);
        if (selectedItem == position)
            holder.itemView.setBackgroundColor(Color.CYAN);
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

        holder.mItem = mValues.get(position);

        //holder.mProduct.setText(product.getDescription());
        //holder.mProduct.setText(mValues.get(position).getProductInfo().getDescription());
        holder.mStore.setText(mValues.get(position).getStoreDescription());
        holder.mCharact.setText(mValues.get(position).getCharact_Description());
        holder.mPrice.setText(mValues.get(position).getPrice().toString());
        holder.mQty.setText(mValues.get(position).getQty().toString());
        holder.mUnit.setText(mValues.get(position).getUnit().getDescription());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
        public final TextView mStore;
        public final TextView mCharact;
        public final TextView mPrice;
        public final TextView mQty;
        public final TextView mUnit;
     //  public final TextView mProduct;
        public Stock mItem;

        public ViewHolder(final View view) {
            super(view);
            itemView.setClickable(true);
            //  mView = view;

            //mProduct = (TextView) view.findViewById(R.id.tv_ProductInfo_Product);
            mCharact = (TextView) view.findViewById(R.id.tv_ProductInfo_Charact);
            mStore = (TextView) view.findViewById(R.id.tv_ProductInfo_Store);
            mQty = (TextView) view.findViewById(R.id.tv_ProductInfo_Qty);
            mUnit = (TextView) view.findViewById(R.id.tv_ProductInfo_Unit);
            mPrice = (TextView) view.findViewById(R.id.tv_ProductInfo_Price);

            // Handle item click and set the selection
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                    //mListener.onItemSelected(mValues.get(selectedItem));
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCharact.getText() + "'";
        }
    }

}
