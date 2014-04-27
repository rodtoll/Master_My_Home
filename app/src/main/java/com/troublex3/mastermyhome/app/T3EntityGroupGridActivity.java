package com.troublex3.mastermyhome.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by rodtoll on 4/12/14.
 */
public class T3EntityGroupGridActivity extends FragmentActivity {

    private Boolean mBuiltFromISY;
    private ViewPager mViewPager;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public T3EntityGroupGridActivity() {
        mBuiltFromISY = false;
        mContext = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
        refreshDevices();
        mFragmentManager = getSupportFragmentManager();
    }

    private void setupPageAdapter() {
        mViewPager.setAdapter(new FragmentPagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return T3EntityGridFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return T3EntityDataStore.get(mContext).getGroups().size();
            }
        });
    }

    private void refreshDevices() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    ISYController.get(mContext).refreshNodeMap();
                } catch(Exception e) {
                    Log.i("TEST", "Had an exception! " + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if(!mBuiltFromISY) {
                    T3EntityDataStore.get(mContext).buildFromISYController(ISYController.get(mContext));
                }
                setupPageAdapter();
            }
        };

        task.execute();
    }


}
/*

public class T3EntityGroupGridActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }


}

 */