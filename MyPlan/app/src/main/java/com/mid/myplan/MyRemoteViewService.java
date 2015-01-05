package com.mid.myplan;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by OptimusV5 on 2014/12/10.
 */
public class MyRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MyRemoteViewsFactory(this.getApplicationContext(),intent);
    }
}
