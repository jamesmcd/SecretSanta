package com.jaymcd.secretsanta;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by James on 11/12/2014.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> values;
    Typeface face;

    public CustomArrayAdapter(Context context, ArrayList<String>  values) {
        super(context, R.layout.listview_layout, values);
        this.context = context;
        this.values = values;
        face =Typeface.createFromAsset(context.getAssets(),"santa.otf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listview_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.listTextView);
        textView.setText(getItem(position));
        textView.setTypeface(face);

        return rowView;
    }
}