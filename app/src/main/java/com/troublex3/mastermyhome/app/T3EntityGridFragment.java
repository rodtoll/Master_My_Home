package com.troublex3.mastermyhome.app;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

public class T3EntityGridFragment extends Fragment {

    private static final Integer INTENT_EDIT_ENTITY = 100;
    private static final String ARG_GROUP_INDEX = "groupIndex";

    private GridView mDeviceGridView;
    private boolean mBuiltFromISY;
    private Integer mGroupIndex;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param groupIndex Parameter 1.
     * @return A new instance of fragment T3EntityGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static T3EntityGridFragment newInstance(Integer groupIndex) {
        T3EntityGridFragment fragment = new T3EntityGridFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GROUP_INDEX, groupIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public T3EntityGridFragment() {
        // Required empty public constructor
        mBuiltFromISY = false;
        mGroupIndex = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mGroupIndex = getArguments().getInt(ARG_GROUP_INDEX);
        }

    }

/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_t3_entity_grid, menu);
    }*/

    protected void launchEditor(UUID id) {
        Intent i = new Intent(getActivity(), T3EntityEditorActivity.class);
        i.putExtra(T3EntityEditorActivity.ARG_ENTITY_ID, id);
        startActivityForResult(i, INTENT_EDIT_ENTITY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_refresh:
                //refreshDevices();
                return true;
            case R.id.menu_item_new_group:
                T3EntityGroup group = T3EntityDataStore.get(getActivity()).newGroup("New Group");
                this.mAdapter.add(group);
                this.mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_add_device:
                T3Entity entity = mGroup.newSubEntity("New Entity", null);
                launchEditor(entity.getId());
                return true;
            case R.id.menu_item_test:
                UUID id = this.mAdapter.getItem(0).getId();
                launchEditor(id);
                return true;
            case R.id.menu_item_settings:
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INTENT_EDIT_ENTITY) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_t3_entity_grid, container, false);

        mDeviceGridView = (GridView) v.findViewById(R.id.entityGridView);

        mDeviceGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ISYNode node = T3EntityDataStore.get(getActivity()).getGroups().get(mGroupIndex).getSubEntities().get(i).getNode();
                sendCommand(node, "DOF");
            }
        });
        setupPageAdapter();
        return v;
    }

    protected T3EntityItemAdapter mAdapter;
    protected T3EntityGroup mGroup;

    private void setupPageAdapter() {
        mGroup = T3EntityDataStore.get(getActivity()).getGroups().get(mGroupIndex);
        mAdapter = new T3EntityItemAdapter(mGroup.getSubEntities());
        mDeviceGridView.setAdapter(mAdapter);
    }

    protected String mCommand;
    protected ISYNode mSelectedDevice;

    private void sendCommand(ISYNode node, String command) {

        mCommand = command;
        mSelectedDevice = node;

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    ISYController.get(getActivity()).executeCommand(mSelectedDevice, mCommand);
                } catch(Exception e) {
                    Log.i("TEST", "Had an exception! " + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if(!mBuiltFromISY) {
                    T3EntityDataStore.get(getActivity()).buildFromISYController(ISYController.get(getActivity()));
                }
                //setupPageAdapter();
            }
        };

        task.execute();
    }

    private int GetImageResourceForEntityType(T3Entity entity) {
        if(entity.getNode() == null ) {
            return R.drawable.ic_launcher;
        }
        if(entity.getNode().getNodeType() == ISYNode.ISYNodeType.DEVICE) {
            ISYDevice device = (ISYDevice) entity.getNode();
            Boolean isOff = device.getStatus().equals("Off");
            switch(device.getDeviceType()) {
                case LIGHT_SWITCH:
                    return (isOff) ? R.drawable.light_switch_off : R.drawable.light_switch_on;
                case DOOR_SENSOR_SENSE:
                    return (isOff) ? R.drawable.motion_sensor_off : R.drawable.motion_sensor_on;
                case DOOR_SENSOR_LOW_BATTERY:
                    return (isOff) ? R.drawable.motion_sensor_off : R.drawable.motion_sensor_on;
                case MOTION_SENSOR_MOVEMENT:
                    return (isOff) ? R.drawable.motion_sensor_off : R.drawable.motion_sensor_on;
                case MOTION_SENSOR_LIGHT:
                    return (isOff) ? R.drawable.motion_sensor_off : R.drawable.motion_sensor_on;
                case MOTION_SENSOR_LOW_BATTERY:
                    return (isOff) ? R.drawable.motion_sensor_off : R.drawable.motion_sensor_on;
                case FAN_LIGHT:
                    return (isOff) ? R.drawable.light_switch_off : R.drawable.light_switch_on;
                case FAN_MOTOR:
                    return (isOff) ? R.drawable.fan_control_off : R.drawable.fan_control_on;
                case INLINE_SWITCH:
                    return (isOff) ? R.drawable.light_switch_off : R.drawable.light_switch_on;
                case MOTION_LIGHT:
                    return (isOff) ? R.drawable.light_switch_off : R.drawable.light_switch_on;
                case IO_SENSE:
                    return (isOff) ? R.drawable.motion_sensor_off : R.drawable.motion_sensor_on;
                case KEYPAD_KEY:
                    return (isOff) ? R.drawable.keypad_button_off : R.drawable.keypad_button_on;
                case LAMP_MODULE:
                    return (isOff) ? R.drawable.light_switch_off : R.drawable.light_switch_on;
                case REMOTE_BUTTON:
                    return (isOff) ? R.drawable.keypad_button_off : R.drawable.keypad_button_on;
                case APPLIANCE_MODULE:
                    return (isOff) ? R.drawable.light_switch_off : R.drawable.light_switch_on;
                case DOOR_LOCK_ON:
                    return (isOff) ? R.drawable.lock_off : R.drawable.lock_on;
                case DOOR_LOCK_OFF:
                    return (isOff) ? R.drawable.lock_on : R.drawable.lock_off;
                case IO_RELAY:
                    return (isOff) ? R.drawable.garage_door_closed : R.drawable.garage_door_open;
                default:
                    return R.drawable.ic_launcher;
            }
        }
        return 0;
    }

    private class T3EntityItemAdapter extends ArrayAdapter<T3Entity> {
        public T3EntityItemAdapter(ArrayList<T3Entity> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.t3entity_item, parent, false);
            }

            T3Entity entity = this.getItem(position);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.t3entity_item_imageView);
            imageView.setImageResource(GetImageResourceForEntityType(entity));

            TextView textView = (TextView) convertView.findViewById(R.id.t3entity_item_textView);
            textView.setText(entity.getName());

            return convertView;
        }

    }

}
