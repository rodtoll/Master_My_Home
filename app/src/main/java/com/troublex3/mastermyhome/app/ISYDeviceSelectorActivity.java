package com.troublex3.mastermyhome.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by rodtoll on 5/4/14.
 */
public class ISYDeviceSelectorActivity extends FragmentActivity {

    public static final String ARG_SELECTED_DEVICE = "selectedDevice";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        String deviceAddress = getIntent().getStringExtra(ARG_SELECTED_DEVICE);

        if(fragment == null) {
            fragment = createFragment(deviceAddress);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    protected Fragment createFragment(String deviceAddress) {

        Bundle args = new Bundle();
        args.putString(ISYNodeFragment.ARG_SELECTED_DEVICE, deviceAddress);

        ISYNodeFragment newFragment = new ISYNodeFragment();
        newFragment.setArguments(args);

        return newFragment;
    }
}
