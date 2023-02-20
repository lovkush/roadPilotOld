package com.lucky.roadpilot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lucky.roadpilot.Models.Location;
import com.lucky.roadpilot.R;

import java.util.ArrayList;
import java.util.List;

public class AutoFillAdapter extends ArrayAdapter<Location> {

    private List<Location> countryListFull;

    public AutoFillAdapter(@NonNull Context context, @NonNull List<Location> countryList) {
        super(context, 0, countryList);
        countryListFull = new ArrayList<>(countryList);
    }

    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.location_row,parent,false
            );
        }

        TextView textView = convertView.findViewById(R.id.inst);

        Location location = getItem(position);

        if(location != null){

            textView.setText(location.getLocation());

        }

        return convertView;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Location> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                suggestions.addAll(countryListFull);
            }else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for(Location item : countryListFull){
                    if(item.getLocation().toUpperCase().contains(filterPattern)){

                        suggestions.add(item);

                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Location) resultValue).getLocation();
        }
    };

}

