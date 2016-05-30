package com.example.malakhau_ti.belbank;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private ListView codelist;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        delete = (Button) findViewById(R.id.DeleteBtn);
        delete.setOnClickListener(this);

        String[] Codes = new String[40];
        for(int i = 0;i<40;i++){
            Codes[i] = "Код №"+String.valueOf(i+1);
        }
        codelist =(ListView) findViewById(R.id.codelist);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, R.id.codeLabel, Codes);
        codelist.setAdapter(adapter);
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
//        overridePendingTransition(R.anim.alpha,R.anim.diagonaltranslate);
        this.finish();
    }

}
