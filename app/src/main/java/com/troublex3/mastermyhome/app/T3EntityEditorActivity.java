package com.troublex3.mastermyhome.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.UUID;


public class T3EntityEditorActivity extends ActionBarActivity {

    public static final String ARG_ENTITY_ID = "entityId";

    private UUID mEntityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEntityId = (UUID)getIntent().getSerializableExtra(ARG_ENTITY_ID);

        setContentView(R.layout.activity_t3_entity_editor);

        T3EntityEditorFragment editorFragment = T3EntityEditorFragment.newInstance(mEntityId);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, editorFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.t3_entity_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.entity_save) {
            return true;
        } else if (id == R.id.entity_cancel) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
