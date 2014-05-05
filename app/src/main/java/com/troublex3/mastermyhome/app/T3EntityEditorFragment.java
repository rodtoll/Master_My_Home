package com.troublex3.mastermyhome.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by rodtoll on 5/4/14.
 */
public class T3EntityEditorFragment extends Fragment {

    public static final String EXTRA_ENTITY_ID = "EntityId";

    public T3EntityEditorFragment() {

    }

    public static T3EntityEditorFragment newInstance(UUID entityId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ENTITY_ID, entityId);

        T3EntityEditorFragment entityFragment = new T3EntityEditorFragment();
        entityFragment.setArguments(args);
        return entityFragment;
    }

    protected T3Entity mEntity;
    protected Button mSelectButton;
    protected EditText mEditTextEntityTitle;
    protected EditText mEditTextDeviceAddress;
    protected EditText mEditTextDeviceName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_t3_entity_editor, container, false);

        UUID deviceId = (UUID) this.getArguments().getSerializable(EXTRA_ENTITY_ID);

        mEntity = T3EntityDataStore.get(getActivity()).getEntity(deviceId);

        mSelectButton = (Button) rootView.findViewById(R.id.entity_isy_device_select_button);

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ISYDeviceSelectorActivity.class);
                if(mEntity.getNode() != null) {
                    i.putExtra(ISYDeviceSelectorActivity.ARG_SELECTED_DEVICE, mEntity.getNode().getNodeAddress());
                }
                startActivityForResult(i, 100);
            }
        });

        mEditTextEntityTitle = (EditText) rootView.findViewById(R.id.entity_title);
        mEditTextEntityTitle.setText(mEntity.getName());
        mEditTextEntityTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mEntity.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditTextDeviceAddress = (EditText) rootView.findViewById(R.id.entity_isy_device_address);
        mEditTextDeviceAddress.setEnabled(false);

        mEditTextDeviceName = (EditText) rootView.findViewById(R.id.entity_isy_device_name);
        mEditTextDeviceName.setEnabled(false);

        setDeviceDetails();

        return rootView;
    }

    protected void setDeviceDetails() {
        if(mEntity.getNode() == null) {
            mEditTextDeviceAddress.setText(R.string.device_name_none_selected);
            mEditTextDeviceName.setText(R.string.device_address_none_selected);
        } else {
            mEditTextDeviceAddress.setText(mEntity.getNode().getNodeAddress());
            mEditTextDeviceName.setText(mEntity.getNode().getNodeName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100) {
            String selectedDevice = data.getStringExtra(ISYNodeFragment.ARG_SELECTED_DEVICE);
            ISYNode node = ISYController.get(getActivity()).getNodeMap().get(selectedDevice);
            mEntity.setNode(node);
            setDeviceDetails();
        }
    }
}
