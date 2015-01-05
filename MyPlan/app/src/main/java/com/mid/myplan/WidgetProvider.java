package com.mid.myplan;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

/**
 * Created by OptimusV5 on 2014/12/10.
 */
public class WidgetProvider extends AppWidgetProvider{
//    private static HashSet<Integer> hashSet = new HashSet<>();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_myplan);
            Intent intent = new Intent(context, MyRemoteViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(id, R.id.widget_list, intent);

            Intent clickIntent = new Intent(context, WidgetProvider.class);
            clickIntent.setAction("itemClick");
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
    @Override
    public void onDeleted(Context context,int[] appWidgetIds)
    {
//        for(int id:appWidgetIds)
//            hashSet.remove(id);
        super.onDeleted(context, appWidgetIds);
    }
    @Override
    public void onReceive(Context context,Intent intent) {
        if (intent.getAction().equals("itemClick")){
            Intent newIntent = new Intent();
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.setClass(context,EditActivity.class);
            newIntent.putExtra("subject",intent.getStringExtra("subject"));
            newIntent.putExtra("position",intent.getIntExtra("position",0));
            newIntent.putExtra("table",intent.getStringExtra("table"));
            newIntent.putExtra("alarm",intent.getIntExtra("alarm",0));
            newIntent.putExtra("option",3);
            context.startActivity(newIntent);
        }
        super.onReceive(context,intent);
    }
}
