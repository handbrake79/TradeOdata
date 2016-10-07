package ru.sk42.tradeodata.Activities.Documents_List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.sk42.tradeodata.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DocList_ActivityFragment extends Fragment {

    public DocList_ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doc_list_fragment, container, false);
    }
}
