package com.fca.agenda.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fca.agenda.R;
import com.fca.agenda.ScheduleActivity;
import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.dto.MainResponseDTO;
import com.fca.agenda.service.CommunicationService;
import com.fca.agenda.utils.ApplicationConstants;
import com.fca.agenda.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunicationRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, Callback, SharedPreferences.OnSharedPreferenceChangeListener {

    private List<CommunicationDTO> communicationDTOList = new ArrayList<>();

    private Context context = null;

    public CommunicationRemoteViewsFactory(final Context context, Intent intent) {
        this.context = context;
        updateData();
    }

    @Override
    public void onCreate() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public int getCount() {
        return this.communicationDTOList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_itens);
        CommunicationDTO communicationDTO = this.communicationDTOList.get(position);
        rv.setTextViewText(R.id.title, communicationDTO.getTitle());
        rv.setTextViewText(R.id.date, DateUtils.dateToString(new Date(communicationDTO.getDate())));

        Intent intent = new Intent(context, ScheduleActivity.class);
        intent.putExtra(ApplicationConstants.COMMUNICATION, communicationDTOList.get(position));
        rv.setOnClickFillInIntent(R.id.widget_row, intent);

        return rv;
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
        return true;
    }

    @Override
    public void onResponse(Call call, Response response) {
        MainResponseDTO mainResponseDTO = (MainResponseDTO) response.body();
        communicationDTOList.clear();
        communicationDTOList.addAll(mainResponseDTO.getListCommunication());
        CommunicationWidgetProvider.widgetsUpdate(context);
    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(context.getResources().getString(R.string.login_event))) {
            updateData();
        }
    }

    private void updateData() {
        if (null != getEmail()) {
            CommunicationService.asyncListCommunicationByLoggedUser(context, getEmail(), this);
        } else {
            communicationDTOList.clear();
            CommunicationWidgetProvider.widgetsUpdate(context);
        }
    }

    private String getEmail() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        return sharedPreferences.getString(context.getResources().getString(R.string.login_event),null);
    }
}
