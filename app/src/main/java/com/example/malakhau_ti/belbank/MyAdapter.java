package com.example.malakhau_ti.belbank;
import java.util.ArrayList;
import android.content.Context;
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

public class MyAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    String[] codes;

    MyAdapter(Context context, String[] codes) {
        ctx = context;
        this.codes = codes;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return codes.length;
    }

    // элемент по позиции
    @Override
    public String getItem(int position) {
        return codes[position];
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        // используем созданные, но не используемые view

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.list_item, parent, false);
            mHolder = new ViewHolder();
            mHolder.codelabel = (TextView) convertView.findViewById(R.id.codeLabel);
            mHolder.code = (EditText) convertView.findViewById(R.id.code);
            convertView.setTag(mHolder);

        }

        //Product p = getProduct(position);
        String p = GetElement(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
//        ((EditText) view.findViewById(R.id.code)).setText(p.name);
        ((TextView) convertView.findViewById(R.id.codeLabel)).setText(p);

        return convertView;
    }

    // товар по позиции
    String GetElement(int position) {
        return getItem(position);
    }

    static class ViewHolder {
        TextView codelabel;
        EditText code;
        int position;
    }



}