package ru.sk42.tradeodata.Activities.ProductInfo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

//implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
public class ProductInfo_Adapter extends RecyclerView.Adapter<ProductInfo_Adapter.ViewHolder> {

    private final List<Stock> mValues;
    private final MyActivityFragmentInteractionInterface mListener;
    private Integer selectedItem;
    public ProductInfo_Adapter(List<Stock> items, MyActivityFragmentInteractionInterface listener) {
        selectedItem = -1;

        mValues = items;
        mListener = listener;
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
            holder.itemView.setBackgroundColor(Color.RED);
        else
            holder.itemView.setBackgroundColor(Color.WHITE);

        holder.mItem = mValues.get(position);

        //holder.mProduct.setText(product.getDescription());
        holder.mProduct.setText(mValues.get(position).getProductInfo().getDescription());
        holder.mStore.setText(mValues.get(position).getStoreDescription());
        holder.mCharact.setText(mValues.get(position).getCharact_Description());
        holder.mPrice.setText(mValues.get(position).getPrice().toString());
        holder.mQty.setText(mValues.get(position).getQty().toString());
        holder.mUnit.setText(mValues.get(position).getUnit_Description());

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
        public final TextView mProduct;
        public Stock mItem;

        public ViewHolder(final View view) {
            super(view);
            itemView.setClickable(true);
            //  mView = view;

            mProduct = (TextView) view.findViewById(R.id.tv_ProductInfo_Product);
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
                    Toast.makeText(view.getContext(), selectedItem.toString() + " position selected", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCharact.getText() + "'";
        }
    }

}