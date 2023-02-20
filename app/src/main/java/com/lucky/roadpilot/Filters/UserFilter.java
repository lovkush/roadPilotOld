package com.lucky.roadpilot.Filters;

import android.widget.Filter;

import com.lucky.roadpilot.Adapter.UserAdapter;
import com.lucky.roadpilot.Models.UserModel;

import java.util.ArrayList;

public class UserFilter extends Filter {

    private UserAdapter adapter;
    private ArrayList<UserModel> filterList;

    public UserFilter(UserAdapter adapter, ArrayList<UserModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0){




            constraint = constraint.toString().toUpperCase();

            ArrayList<UserModel> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getLocation().toUpperCase().contains(constraint) ||
                        filterList.get(i).getName().toUpperCase().contains(constraint) ||
                        filterList.get(i).getCat().toUpperCase().contains(constraint)
                ){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.models = (ArrayList<UserModel>) filterResults.values;

        adapter.notifyDataSetChanged();

        String fun= "";


    }
}
