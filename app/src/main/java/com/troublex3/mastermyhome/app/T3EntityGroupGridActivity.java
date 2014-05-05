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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
        setupPageAdapter();
        refreshDevices();
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_t3_entity_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_refresh:
                //refreshDevices();
                return true;
            case R.id.menu_item_new_group:
                T3EntityGroup group = T3EntityDataStore.get(this).newGroup("New Group");
                mFragmentPager.notifyDataSetChanged();
/*                T3EntityGroup group = T3EntityDataStore.get(getActivity()).newGroup("New Group");
                this.mAdapter.add(group);
                this.mAdapter.notifyDataSetChanged(); */
                return true;
            case R.id.menu_item_test:
                return false;
            case R.id.menu_item_add_device:
                return false;
/*                mGroup.newSubEntity("New Entity", ISYController.get(this.getActivity()).getNodeMap().get("1A 11 98 1"));
                this.mAdapter.notifyDataSetChanged();*/
                //return true;
            case R.id.menu_item_settings:
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    protected FragmentPagerAdapter mFragmentPager;

    private void setupPageAdapter() {
        mFragmentPager = new FragmentPagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return T3EntityGridFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return T3EntityDataStore.get(mContext).getGroups().size();
            }
        };
        mViewPager.setAdapter(mFragmentPager);
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
                mFragmentPager.notifyDataSetChanged();
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