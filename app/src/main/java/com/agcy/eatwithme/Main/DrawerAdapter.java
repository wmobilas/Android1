package com.agcy.eatwithme.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agcy.eatwithme.R;

/**
 * Created by Freeman on 12.10.2014.
 */
public class DrawerAdapter extends BaseAdapter {
    protected final String[] arrayList;
    protected final Context context;

    public DrawerAdapter(Context context, String[] arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.length;
    }

    @Override
    public String getItem(int position) {
        return arrayList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //todo сделать динамическую загрузку
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view;
        if (convertView == null) {
            view = (ViewGroup) inflater.inflate(R.layout.nav_drawer_item, parent, false);
        } else {
            view = (ViewGroup) convertView;
        }
        TextView text = (TextView) view.findViewById(R.id.text1);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);

        text.setText(arrayList[position]);
        switch (position) {
            case 0:
                icon.setImageResource(R.drawable.divider_black);
                break;
            case 1:
                icon.setImageResource(R.drawable.divider_black);
                break;
            case 2:
                icon.setImageResource(R.drawable.divider_black);
                break;
            default:
                icon.setImageResource(R.drawable.ic);
                break;
        }
        return view;
    }
}