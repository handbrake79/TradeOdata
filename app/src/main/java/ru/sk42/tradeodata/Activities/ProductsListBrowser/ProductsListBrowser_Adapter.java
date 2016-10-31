package ru.sk42.tradeodata.Activities.ProductsListBrowser;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;
import java.util.List;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;


public class ProductsListBrowser_Adapter extends RecyclerView.Adapter<ProductsListBrowser_Adapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private final List<Product> items;
    private final MyActivityFragmentInteractionInterface mListener;
    private Product parentProduct;
    private ProductsList_Fragment owner_fragment;

    public ProductsListBrowser_Adapter(List<Product> items, MyActivityFragmentInteractionInterface listener, final ProductsList_Fragment ownerfragment) {
        this.items = items;
        mListener = listener;
        owner_fragment = ownerfragment;

    }

    public void setParentProduct(Product parentProduct) {
        this.parentProduct = parentProduct;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_browser_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public long getHeaderId(int position) {
//        Product product = getItem(position);
//        if (product.isFirstLevelCategory() || product.isTopCategory())
//            return -1;
//        else
        return 0;
        //   return getItem(position).getDescription().charAt(0);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        Product product = getItem(position);
        holder.itemView.setBackgroundColor(Constants.COLORS.ENABLED);

        if (product.isFirstLevelCategory()) {
            textView.setText("Номенклатура");
            return;
        }

        if (product.isTopCategory()) {
            textView.setText("Top category!");
            return;
        }

        if (product.isLowerThanFirstLevel()) {
            if (parentProduct != null) {
                textView.setText(parentProduct.getDescription());
                return;
            } else {
                textView.setText("Вышестоящая группа");
                return;
            }
        }

        textView.setText("Заголовок по умолчанию");

        //textView.setText(String.valueOf(product.getDescription().charAt(0)));
    }

//    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
//    }

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
                    owner_fragment.onItemSelection(holder.mItem);
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
