package com.mid.myplan;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    public ExpandableListView expandableListView;
    public ExpandAdapter expandAdapter1;
    public ExpandAdapter expandAdapter2;
    public LinearLayout searchLayout;
    public Button search;
    public ImageView add;
    public static ArrayList<Item> items1;
    public static ArrayList<Item> items2;
    public ListView widgetList;
    public static int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
        searchLayout = (LinearLayout)findViewById(R.id.buttonLayout);
        search = (Button)findViewById(R.id.searchButton);
        add = (ImageView)findViewById(R.id.addPlan);
        widgetList = (ListView)findViewById(R.id.widget_list);
        search.setOnClickListener(onClickListener);
        add.setOnClickListener(onClickListener);
        id = 0;
        expandAdapter1 = new ExpandAdapter(this,1);
        expandAdapter2 = new ExpandAdapter(this,2);
        items1 = expandAdapter1.getDatas();
        items2 = expandAdapter2.getDatas();
        expandableListView.setAdapter(expandAdapter1);
        new OnTouch(this, expandAdapter1, expandAdapter2);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(new ComponentName(this,WidgetProvider.class)),R.id.widget_list);
    }
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.searchButton) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchableActivity.class);
                startActivityForResult(intent,0);
                searchLayout.setVisibility(View.GONE);
            } else {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditActivity.class);
                intent.putExtra("option",1);
                startActivityForResult(intent,1);
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        searchLayout.setVisibility(View.VISIBLE);
        if (data == null)
            return;
        List<List<Item>> data1 = expandAdapter1.getData();
        List<List<Item>> data2 = expandAdapter2.getData();

        int option = data.getIntExtra("option",0);
        int Class = data.getIntExtra("class",1);
        int oldClass = data.getIntExtra("oldClass", 0);
        int position = data.getIntExtra("position",0);
        int alarm = data.getIntExtra("alarm",0);
        String table = data.getStringExtra("table");
        String subject = data.getStringExtra("subject");
        String time = data.getStringExtra("time");

        if (option == 1) {
            Item item = new Item(subject,time,table,data1.get(Class).size(),alarm);
            data1.get(Class).add(item);
            expandAdapter1.setData(data1);
            expandAdapter1.notifyDataSetChanged();
        } else if (option == 2 || option == 3) {
//            String newSubject = data.getStringExtra("newSubject");
//            List<Item> list = data1.get(Class);
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getName().equals(newSubject)) {
//                    position = i;
//                    break;
//                }
//            }
            if (option == 2) {
                if (oldClass != Class) {
                    data1.get(oldClass).remove(position);
                    data1.get(Class).add(new Item(subject,time,table,data1.get(Class).size(),alarm));
                } else {
                    data1.get(Class).get(position).setName(subject);
                    data1.get(Class).get(position).setTime(time);
                    data1.get(Class).get(position).setAlarm(alarm);
                }
            } else {
                data1.get(Class).remove(position);
                data2.get(Class).add(new Item(subject,time,"done",data2.get(Class).size(),alarm));
                expandAdapter2.setData(data2);
                expandAdapter2.notifyDataSetChanged();
            }
            expandAdapter1.setData(data1);
            expandAdapter1.notifyDataSetChanged();
        } else if (option == 4) {
            if (table.equals("todos")) {
                data1.get(Class).remove(position);
                expandAdapter1.setData(data1);
                expandAdapter1.notifyDataSetChanged();
            } else {
                data2.get(Class).remove(position);
                expandAdapter2.setData(data2);
                expandAdapter2.notifyDataSetChanged();
            }
        }
        items1 = expandAdapter1.getDatas();
        items2 = expandAdapter2.getDatas();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(new ComponentName(this,WidgetProvider.class)),R.id.widget_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
