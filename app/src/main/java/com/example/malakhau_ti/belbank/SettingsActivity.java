package com.example.malakhau_ti.belbank;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    String[] CodesArray = new String[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        for(int i = 0;i<40;i++){
            sPref = PreferenceManager.getDefaultSharedPreferences(this);
           CodesArray[i] = sPref.getString("code"+String.valueOf(i), "non on Settings Activity "+String.valueOf(i+1));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editorlogin = (EditText) findViewById(R.id.Login);
        editorpass = (EditText) findViewById(R.id.Pass);

        loadUserData();

        delete = (Button) findViewById(R.id.DeleteBtn);
        delete.setOnClickListener(this);

        codelist =(ListView) findViewById(R.id.codelist);

        MyAdapter adapter = new MyAdapter(this);

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

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.clear();
        ed.commit();
        this.finish();
//        overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);

        Intent myIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        SettingsActivity.this.startActivity(myIntent);

    }

    void saveUserData(){

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(LOGIN, editorlogin.getText().toString());
        ed.putString(PASSWORD, editorpass.getText().toString());
        ed.commit();

    }

    void loadUserData() {

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String login = sPref.getString(LOGIN, "");
        String pass = sPref.getString(PASSWORD, "");
        editorlogin.setText(login);
        editorpass.setText(pass);

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

