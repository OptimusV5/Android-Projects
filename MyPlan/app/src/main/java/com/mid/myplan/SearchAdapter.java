package com.mid.myplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by OptimusV5 on 2014/12/9.
 */
public class SearchAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Item> storeList;
    private ArrayList<Item> arrayList;
    private SearchFilter searchFilter;
    public SearchAdapter(Context context_, ArrayList<Item> items1, ArrayList<Item> items2) {
        context = context_;
        arrayList = new ArrayList<>();
        storeList = new ArrayList<>();
        arrayList.addAll(items1);
        arrayList.addAll(items2);
        storeList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Item getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.search_item_layout,null);
        TextView subject = (TextView)convertView.findViewById(R.id.searchItemSubject);
        subject.setText(arrayList.get(position).getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return searchFilter == null ? searchFilter = new SearchFilter() : searchFilter;
    }
    private class SearchFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Item> list = new ArrayList<>();
            for (int i = 0; i < storeList.size(); i++) {
                if (storeList.get(i).getName().contains(constraint))
                    list.add(storeList.get(i));
            }
            filterResults.values = list;
            filterResults.count = list.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList = (ArrayList<Item>)results.values;
            notifyDataSetChanged();
        }
    }
}
