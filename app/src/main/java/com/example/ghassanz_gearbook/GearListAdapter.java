package com.example.ghassanz_gearbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/* listadapter class connects the information related to each gear to the list view of gears
* Reference: https://www.youtube.com/watch?v=E6vE8fqQPTE created by Mitch Tabian on March 14, 2017. I started using this tutorial for creating GearListAdapter on September 21, 2020.  */
public class GearListAdapter extends ArrayAdapter<Gear> {
    private Context context;
    int mResource;

    public GearListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Gear> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // gets the view and attach it to the listview
        String date = getItem(position).getDate();
        String maker = getItem(position).getMaker();
        String description = getItem(position).getDescription();
        String price = getItem(position).getPrice();
        String comment = getItem(position).getComment();

        // instantiate a new Gear object
        Gear gear = new Gear(date, maker, description, price, comment);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(mResource,parent,false);

        // instantiates each TextView on the adapter view layout
        TextView txDate = (TextView) convertView.findViewById(R.id.date);
        TextView txDescription = (TextView) convertView.findViewById(R.id.description);
        TextView txPrice = (TextView) convertView.findViewById(R.id.amount);

        // sets the information of each gear in the gear list
        txDate.setText(date);
        txDescription.setText(description);
        txPrice.setText(price);
        return convertView;
    }
}
