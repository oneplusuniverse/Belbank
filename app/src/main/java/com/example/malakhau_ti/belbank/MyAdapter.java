package com.example.malakhau_ti.belbank;
import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    EditText code;
    TextView codelabel;
    SharedPreferences sPref;
    SharedPreferences sPref_1;

    MyAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sPref_1 = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return 40;
    }

    // элемент по позиции
    @Override
    public String getItem(int position) {
        return sPref_1.getString("code"+String.valueOf(position),"код №"+String.valueOf(position+1));
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        // используем созданные, но не используемые view

            convertView = lInflater.inflate(R.layout.list_item, parent, false);
            mHolder = new ViewHolder();
            mHolder.codelabel = (TextView) convertView.findViewById(R.id.codeLabel);
            mHolder.code = (EditText) convertView.findViewById(R.id.code);
            convertView.setTag(mHolder);


        code = ((EditText) convertView.findViewById(R.id.code));

        codelabel = ((TextView) convertView.findViewById(R.id.codeLabel));

        codelabel.setText("Код № " + String.valueOf(position + 1));

        if(CheckInputData(position, sPref_1)){
            code.setText(sPref_1.getString("code"+String.valueOf(position),"Код №"+String.valueOf(position+1)));
        }else {
            code.setHint("code №"+String.valueOf(position+1));
        }

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                sPref = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("code" + String.valueOf(position), mHolder.code.getText().toString());
                ed.commit();

            }
        });
//    }
        convertView.setTag(mHolder);
        return convertView;
    }

    private boolean CheckInputData (int position, SharedPreferences sPref_1){
        String inputData = sPref_1.getString("code"+String.valueOf(position),"none");
        if((inputData!="none")&&(inputData!="")&&!(inputData.length()<4)&&!(inputData.length()>4)){
        return true;
        }else{
            return false;
        }

    }

    static class ViewHolder {
        TextView codelabel;
        EditText code;
    }



}