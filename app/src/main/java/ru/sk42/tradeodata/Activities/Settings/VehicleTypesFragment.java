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

import java.sql.SQLException;
import java.util.List;

import ru.sk42.tradeodata.Activities.Settings.adapters.VehicleTypesRecyclerViewAdapter;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class VehicleTypesFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private SettingsInterface mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VehicleTypesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VehicleTypesFragment newInstance(int columnCount) {
        VehicleTypesFragment fragment = new VehicleTypesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings__fragment_vehicletypes, container, false);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        settingsActivity.setTitle("Типы транспорта", "будут использоваться только выбранные");

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            List<VehicleType> vehicleTypes = null;
            try {
                vehicleTypes = MyHelper.getVehicleTypesDao().queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            recyclerView.setAdapter(new VehicleTypesRecyclerViewAdapter(vehicleTypes, mListener));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
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
