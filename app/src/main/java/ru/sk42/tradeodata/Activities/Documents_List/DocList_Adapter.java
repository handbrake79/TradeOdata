package ru.sk42.tradeodata.Activities.Documents_List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;

/**
 * Created by test on 28.04.2016.
 */
public class DocList_Adapter extends RecyclerView.Adapter<DocList_Adapter.ViewHolder> {
    private List<DocSale> mValues;

    private MyActivityFragmentInteractionInterface mListener;
    private int selectedItem;

    public DocList_Adapter(ArrayList<DocSale> items, MyActivityFragmentInteractionInterface listenerInterface) {
        mValues = items;
        mListener = listenerInterface;
    }


    private DocSale getSelectedObject() {
        return mValues.get(selectedItem);
    }

    public void setmValues(List<DocSale> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_list_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mDate.setText(holder.mItem.getFormattedDate());
        holder.mNumber.setText(holder.mItem.getNumber());
        holder.mAuthor.setText(holder.mItem.getAuthor().getDescription());
        holder.mTotal.setText(Uttils.fd(holder.mItem.getTotal()));
        holder.mPosted.setText(holder.mItem.getPostedDescr());
        holder.mContract.setText(holder.mItem.getContract().getDescription());
        Integer productsCount, servicesCount;
        productsCount = holder.mItem.getProducts().size();
        servicesCount = holder.mItem.getServices().size();
        holder.mProductCount.setText("Товаров " + productsCount.toString() + ", услуг " + servicesCount.toString());


        // Handle item click and set the selection
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelection(holder.mItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDate;
        public final TextView mNumber;
        public final TextView mAuthor;
        public final TextView mTotal;
        public final TextView mPosted;
        public final TextView mContract;
        public final TextView mProductCount;
        public DocSale mItem;

        public ViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            mView = view;

            mNumber = (TextView) view.findViewById(R.id.docList_Number);
            mDate = (TextView) view.findViewById(R.id.docList_Date);
            mAuthor = (TextView) view.findViewById(R.id.docList_Author);
            mTotal = (TextView) view.findViewById(R.id.docList_Total);
            mPosted = (TextView) view.findViewById(R.id.docList_Posted);
            mContract = (TextView) view.findViewById(R.id.docList_Contract);
            mProductCount = (TextView) view.findViewById(R.id.docList_ProductCount);

        }


    }


}
