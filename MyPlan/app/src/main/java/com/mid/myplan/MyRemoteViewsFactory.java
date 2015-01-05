package com.mid.myplan;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by OptimusV5 on 2014/12/10.
 */
public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public final Context context;
    public static ArrayList<Item> arrayList;
    private int appWidgetId;
    public MyRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        arrayList = new ArrayList<>();
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    @Override
    public void onCreate() {
        arrayList = MainActivity.items1;
    }
    @Override
    public void onDataSetChanged() {
        arrayList = MainActivity.items1;
    }

    @Override
    public void onDestroy() {if (arrayList != null) arrayList.clear(); }

    @Override
    public int getCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < 0 || position > arrayList.size())
            return null;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_item);
        remoteViews.setTextViewText(R.id.widget_subject,arrayList.get(position).getName());
        String[] times = arrayList.get(position).getTime().split(" ");
        remoteViews.setTextViewText(R.id.widget_day,times[0]);
        remoteViews.setTextViewText(R.id.widget_time,times[1]);
        Bundle extras = new Bundle();
        extras.putString("subject",arrayList.get(position).getName());
        extras.putInt("position", arrayList.get(position).getPosition());
        extras.putString("table",arrayList.get(position).getTable());
        extras.putInt("alarm",arrayList.get(position).isAlarm());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_item,fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
