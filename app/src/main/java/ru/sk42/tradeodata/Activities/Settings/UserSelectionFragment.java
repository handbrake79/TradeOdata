package ru.sk42.tradeodata.Activities.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.sk42.tradeodata.Activities.Settings.adapters.UserRecyclerViewAdapter;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;

import java.sql.SQLException;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class UserSelectionFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private SettingsInterface mListener;
    boolean calledFromMenu;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserSelectionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserSelectionFragment newInstance(boolean calledFromMenu) {
        UserSelectionFragment fragment = new UserSelectionFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.CALLED_FROM_MENU, calledFromMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            calledFromMenu = getArguments().getBoolean(Constants.CALLED_FROM_MENU);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings__fragment_user_list, container, false);

        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        settingsActivity.setTitle("Пользователи", "выберите пользователя");
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            List<User> listUsers = null;
            try {
                listUsers = MyHelper.getUserDao().queryBuilder().orderBy("description", true).query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            recyclerView.setAdapter(new UserRecyclerViewAdapter(listUsers, mListener));
//            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
//            recyclerView.addItemDecoration(dividerItemDecoration);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsInterface) {
            mListener = (SettingsInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
