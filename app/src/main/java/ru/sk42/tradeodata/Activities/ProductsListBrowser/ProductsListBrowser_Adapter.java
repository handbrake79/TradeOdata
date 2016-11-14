package ru.sk42.tradeodata.Activities.ProductsListBrowser;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;


public class ProductsListBrowser_Adapter extends RecyclerView.Adapter<ProductsListBrowser_Adapter.ViewHolder>{
// implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
    private final List<Product> items;
    private final InteractionInterface mListener;
    private Product parentProduct;
    private ProductsListFragment owner_fragment;

    public ProductsListBrowser_Adapter(List<Product> items, InteractionInterface listener, final ProductsListFragment ownerfragment) {
        this.items = items;
        mListener = listener;
        owner_fragment = ownerfragment;

    }

    public void setParentProduct(Product parentProduct) {
        this.parentProduct = parentProduct;
    }


    public Product getItem(int position) {
        return items.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_products_browser_category_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = items.get(position);
        holder.tvCode.setText(items.get(position).getCode());
        holder.tvDescription.setText(items.get(position).getDescription());
        if (holder.mItem.isFolder()) {
            holder.mView.setBackgroundColor(Constants.COLORS.SELECTED_COLOR);
        } else {
            holder.mView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != owner_fragment) {
                    owner_fragment.onItemSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvCode;
        public final TextView tvDescription;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvCode = (TextView) view.findViewById(R.id.tvCode);
            tvDescription = (TextView) view.findViewById(R.id.tvProduct);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvDescription.getText() + "'";
        }
    }
}
