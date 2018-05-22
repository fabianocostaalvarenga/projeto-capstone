package com.fca.agenda.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class CommunicationWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new CommunicationRemoteViewsFactory(this, intent);
    }

}
