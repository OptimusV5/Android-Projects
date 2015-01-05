package com.mid.myplan;

import android.content.Intent;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import java.util.List;

/**
 * Created by OptimusV5 on 2014/12/7.
 */
public class OnTouch {
    public LinearLayout undo_linearLayout;
    public LinearLayout done_linearLayout;
    public LinearLayout setting_linearLayout;
    public LinearLayout searchLayout;
    public ImageView undo_image;
    public ImageView done_image;
    public ImageView setting_image;
    public TextView undo_text;
    public TextView done_text;
    public TextView setting_text;
    public TextView titleName;
    public Activity activity;
    public ExpandAdapter expandAdapter1;
    public ExpandAdapter expandAdapter2;
    public ExpandableListView expandableListView;
    public OnTouch(Activity activity_,ExpandAdapter expandAdapter1_, ExpandAdapter expandAdapter2_) {
        activity = activity_;
        undo_linearLayout = (LinearLayout)activity.findViewById(R.id.layout_undo);
        done_linearLayout = (LinearLayout)activity.findViewById(R.id.layout_done);
        //setting_linearLayout = (LinearLayout)activity.findViewById(R.id.layout_setting);
        undo_image = (ImageView)activity.findViewById(R.id.image_undo);
        done_image = (ImageView)activity.findViewById(R.id.image_done);
        //setting_image = (ImageView)activity.findViewById(R.id.image_setting);
        undo_text = (TextView)activity.findViewById(R.id.textView_undo);
        done_text = (TextView)activity.findViewById(R.id.textView_done);
        //setting_text = (TextView)activity.findViewById(R.id.textView_setting);
        titleName = (TextView)activity.findViewById(R.id.titleName);
        undo_linearLayout.setOnTouchListener(onTouchListener);
        done_linearLayout.setOnTouchListener(onTouchListener);
        //setting_linearLayout.setOnTouchListener(onTouchListener);
        searchLayout = (LinearLayout)activity.findViewById(R.id.buttonLayout);
        expandableListView = (ExpandableListView)activity.findViewById(R.id.expandableListView);
        expandAdapter1 = expandAdapter1_;
        expandAdapter2 = expandAdapter2_;
        expandableListView.setOnChildClickListener(onChildClickListener);

    }
    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == undo_linearLayout.getId()) {
                undo_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.undo));
                done_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.done_1));
                //setting_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.setting_1));
                undo_text.setTextColor(Color.rgb(50,168,225));
                done_text.setTextColor(Color.rgb(0,0,0));
                //setting_text.setTextColor(Color.rgb(0,0,0));
                titleName.setText("未完成");
                expandableListView.setAdapter(expandAdapter1);
            } else if (v.getId() == done_linearLayout.getId()) {
                undo_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.undo_1));
                done_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.done));
                //setting_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.setting_1));
                undo_text.setTextColor(Color.rgb(0, 0, 0));
                done_text.setTextColor(Color.rgb(50, 168, 225));
                //setting_text.setTextColor(Color.rgb(0, 0, 0));
                titleName.setText("已完成");
                expandableListView.setAdapter(expandAdapter2);
            }
//            } else if (v.getId() == setting_linearLayout.getId()) {
//                undo_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.undo_1));
//                done_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.done_1));
//                setting_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.setting));
//                undo_text.setTextColor(Color.rgb(0,0,0));
//                done_text.setTextColor(Color.rgb(0,0,0));
//                setting_text.setTextColor(Color.rgb(50,168,225));
//                titleName.setText("设置");
//            }
            return false;
        }
    };
    public ExpandableListView.OnChildClickListener onChildClickListener =new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Item item;
            if (titleName.getText().toString().equals("未完成"))
                item = expandAdapter1.getChild(groupPosition,childPosition);
            else
                item = expandAdapter2.getChild(groupPosition,childPosition);
            Intent intent = new Intent();
            intent.setClass(activity,EditActivity.class);
            intent.putExtra("option",2);
            intent.putExtra("subject",item.getName());
            intent.putExtra("table",item.getTable());
            intent.putExtra("position",childPosition);
            intent.putExtra("alarm",item.isAlarm());
            activity.startActivityForResult(intent,1);
            return false;
        }
    };

}
