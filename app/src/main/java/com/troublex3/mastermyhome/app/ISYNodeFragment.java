package com.troublex3.mastermyhome.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 */
public class ISYNodeFragment extends ListFragment {

    public static final String ARG_SELECTED_DEVICE = "selectedDevice";

    // TODO: Rename and change types of parameters

    public static ISYNodeFragment newInstance() {
        ISYNodeFragment fragment = new ISYNodeFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ISYNodeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedDeviceAddress = getArguments().getString(ARG_SELECTED_DEVICE);

        ArrayAdapter<ISYNode> adapter =
                new ArrayAdapter<ISYNode>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        ISYController.get(getActivity()).getNodeMap().values().toArray(new ISYNode[0])
                );

        // TODO: Change Adapter to display your content
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ISYNode node = ((ArrayAdapter<ISYNode>) getListAdapter()).getItem(position);

        Intent i = new Intent();
        i.putExtra(ARG_SELECTED_DEVICE, node.getNodeAddress());
        getActivity().setResult(Activity.RESULT_OK, i );
        getActivity().finish();
    }


}
