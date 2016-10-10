package ru.sk42.tradeodata.Activities.Document;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.DocumentRequisites.Requisite;
import ru.sk42.tradeodata.R;

/**
 * Created by test on 21.04.2016.
 */
public class Requisites_Adapter extends RecyclerView.Adapter<Requisites_Adapter.ViewHolder> {

    @Bind(R.id.poc_page_requisites_title)
    TextView mTitleText;
    @Bind(R.id.doc_page_requisites_value)
    TextView mValueText;

    private int selectedItem;
    private ArrayList<Requisite> mValues;

    private MyActivityFragmentInteractionInterface mListener;

    public Requisites_Adapter(ArrayList<Requisite> mValues, MyActivityFragmentInteractionInterface mListener) {
        this.mListener = mListener;
        this.mValues = mValues;
    }

    private Requisite getSelectedObject() {
        return mValues.get(selectedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_requisites, parent, false);
        ButterKnife.bind(this, view);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        mTitleText.setText(holder.mItem.getDescription());
        mValueText.setText(holder.mItem.getValueString());

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
//        public final TextView mTitle;
//        public final TextView mValue;
        public Requisite mItem;

        public ViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            //ButterKnife.bind(this, view);
            //  mView = view;

//            mTitle = (TextView) view.findViewById(R.id.docReq_Name);
//            mValue = (TextView) view.findViewById(R.id.docReq_Value);


            // Handle item click and set the selection
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                    mListener.onItemSelection(getSelectedObject());
                }
            });
        }


    }

}
