package com.example.malakhau_ti.belbank;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private ListView codelist;
    Button delete;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] Codes = new String[40];


        delete = (Button) findViewById(R.id.DeleteBtn);
        delete.setOnClickListener(this);

        String[] CodesLabels = new String[40];
        for(int i = 0;i<40;i++){
            CodesLabels[i] = "Код №"+String.valueOf(i+1);
        }
        codelist =(ListView) findViewById(R.id.codelist);

        MyAdapter adapter = new MyAdapter(this, CodesLabels );
        codelist.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right );
//                overridePendingTransition(R.anim.alpha,R.anim.center_to_left);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DeleteBtn:
                Delete();
                break;
        }
    }

    private void Delete(){
        //Clear data in pref
        String[] Codes = new String[40];
        for(int i = 0;i<40;i++){
            Codes[i] = "Код № "+String.valueOf(i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, R.id.code, Codes);
        codelist.setAdapter(adapter);

    }

}
