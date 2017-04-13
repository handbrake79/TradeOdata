package ru.sk42.tradeodata.Activities.Settings.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.sk42.tradeodata.Activities.Settings.SettingsInterface;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.St;
import ru.sk42.tradeodata.R;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final SettingsInterface mListener;
    private int selected;

    public UserRecyclerViewAdapter(List<User> items, SettingsInterface listener) {
        mValues = items;
        mListener = listener;
        selected = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings__users_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mUser = mValues.get(position);
        holder.tvUser.setText(holder.mUser.getDescription());
        if (selected == position) {
            holder.itemView.setBackgroundColor(St.getApp().getResources().getColor(R.color.bpRed));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvUser;
        public final ImageView ivIcon;
        public User mUser;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(selected);
                    selected = getLayoutPosition();
                    notifyItemChanged(selected);
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onValueSelected(mUser);
                    }
                }
            });
            tvUser = (TextView) view.findViewById(R.id.list_item_text);
            ivIcon = (ImageView) view.findViewById(R.id.list_item_icon);
            ivIcon.setImageResource(R.drawable.ic_account_box_black_24dp);
        }
    }
}
