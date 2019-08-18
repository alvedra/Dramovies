package com.example.h.dramoviesvfinal.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.h.dramoviesvfinal.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
