package com.mid.myplan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OptimusV5 on 2014/12/7.
 */
public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] mGroupStrings = {"学习","工作","生活","娱乐","运动"};
    private List<List<Item>> mData;
    private ArrayList<String> subjects;
    public SQLiteDatabase db;
    public Cursor cursor;
    public String table;
    public ExpandAdapter(Context ctx,int flag) {
        mContext = ctx;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = new ArrayList<>(5);
        subjects = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            mData.add(new ArrayList<Item>());
        db = new DBHelper(ctx).getReadableDatabase();
//        db.execSQL("INSERT INTO todos VALUES (?,?,?,?)", new Object[]{"nihao" + flag,1,"1993-5-7 15:05:36","haha"});
//        db.execSQL("INSERT INTO done VALUES (?,?,?,?)", new Object[]{"nihao" + flag,2,"1993-5-7 15:05:36","haha"});
        if (flag == 1) {
            cursor = db.rawQuery("SELECT * FROM todos", null);
            table = "todos";
        } else {
            cursor = db.rawQuery("SELECT * FROM done", null);
            table = "done";
        }
        while (cursor.moveToNext()) {
            String subject = cursor.getString(cursor.getColumnIndex("subject"));
            int Class = cursor.getInt(cursor.getColumnIndex("class"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int position = mData.get(Class).size();
            int alarm = cursor.getInt(cursor.getColumnIndex("alarm"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            MainActivity.id = MainActivity.id > id ? MainActivity.id : id;
            mData.get(Class).add(new Item(subject, time,table,position,alarm));
            subjects.add(subject);
        }
        cursor.close();
        db.close();
    }
    public void setData(List<List<Item>> data) { mData = data;}
    public List<List<Item>> getData(){
        return mData;
    }
    public ArrayList<String> getSubjects(){return subjects;}
    public ArrayList<Item> getDatas() {
        ArrayList<Item> arrayList = new ArrayList<>();
        for (List<Item> array : mData) {
            arrayList.addAll(array);
        }
        return arrayList;
    }
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition).size();
    }

    @Override
    public List<Item> getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition);
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.group_image);
        if (isExpanded) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.down));
            imageView.setPadding(10,0,0,0);
        } else {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.up));
            imageView.setPadding(13,0,0,0);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = (TextView) convertView.findViewById(R.id.group_name);
        holder.mGroupName.setText(mGroupStrings[groupPosition]);
        holder.mGroupCount = (TextView) convertView.findViewById(R.id.group_count);
        holder.mGroupCount.setText( "" + mData.get(groupPosition).size());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item_layout, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
        holder.mChildName.setText(getChild(groupPosition, childPosition)
                .getName());
        holder.mTime = (TextView) convertView.findViewById(R.id.item_detail);
        holder.mTime.setText(getChild(groupPosition, childPosition)
                .getTime());
        holder.mImage = (ImageView) convertView.findViewById(R.id.isAlarm);
        if (getChild(groupPosition,childPosition).isAlarm() == 0
                || getChild(groupPosition,childPosition).getTable().equals("done"))
            holder.mImage.setVisibility(View.INVISIBLE);
        else holder.mImage.setVisibility(View.VISIBLE);
//        mData.get(groupPosition).get(childPosition).setPosition(childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        /* 很重要：实现ChildView点击事件，必须返回true */
        return true;
    }

    private class GroupViewHolder {
        TextView mGroupName;
        TextView mGroupCount;
    }

    private class ChildViewHolder {
        TextView mChildName;
        TextView mTime;
        ImageView mImage;
    }
}
