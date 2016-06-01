package com.example.malakhau_ti.belbank;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private ListView codelist;
    Button delete;
    final String LOG_TAG = "myLogs";
    SharedPreferences sPref;
    private static final String LOGIN  = "Saved_Login";
    private static final String PASSWORD = "Saved_Pass";
    EditText editorlogin;
    EditText editorpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        String[] Codes = new String[40];
        editorlogin = (EditText) findViewById(R.id.Login);
        editorpass = (EditText) findViewById(R.id.Pass);

        loadUserData();

        delete = (Button) findViewById(R.id.DeleteBtn);
        delete.setOnClickListener(this);

        List_item_class[] CodesLabels = new List_item_class[40];
        for(int i = 0;i<40;i++){
            CodesLabels[i] = new List_item_class();
            CodesLabels[i].CodeLabel = "Код №"+String.valueOf(i+1);
//            CodesLabels[i].Code = "";
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

                saveUserData();

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

    void saveUserData(){

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(LOGIN, editorlogin.getText().toString());
        ed.putString(PASSWORD, editorpass.getText().toString());
        ed.commit();
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();

    }

    void loadUserData() {

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String login = sPref.getString(LOGIN, "");
        String pass = sPref.getString(PASSWORD, "");
        editorlogin.setText(login);
        editorpass.setText(pass);
        Toast.makeText(this, "Settings loaded successfully", Toast.LENGTH_SHORT).show();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == (KeyEvent.KEYCODE_BACK)) {
            saveUserData();
            this.finish();
            overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right );
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

