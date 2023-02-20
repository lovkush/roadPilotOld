package com.lucky.roadpilot.Filters;

import android.widget.Filter;

import com.lucky.roadpilot.Adapter.JobAdapter;
import com.lucky.roadpilot.Models.JobModel;

import java.util.ArrayList;

public class JobFilter extends Filter {

    private JobAdapter adapter;
    private ArrayList<JobModel> filterList;

    public JobFilter(JobAdapter adapter, ArrayList<JobModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0){




            constraint = constraint.toString().toUpperCase();

            ArrayList<JobModel> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getActive().toUpperCase().contains(constraint) ||
                        filterList.get(i).getLocation().toUpperCase().contains(constraint) ||
                        filterList.get(i).getNumber().toUpperCase().contains(constraint)
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

        adapter.arrayList = (ArrayList<JobModel>) filterResults.values;

        adapter.notifyDataSetChanged();

        String fun= "";

    }
}
