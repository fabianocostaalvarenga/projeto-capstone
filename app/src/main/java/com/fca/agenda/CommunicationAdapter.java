package com.fca.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by fabiano.alvarenga on 11/04/18.
 */

public class CommunicationAdapter extends RecyclerView.Adapter {

    private OnItemClickListener listener;
    private Context ctx;
    private List<CommunicationDTO> dtos;

    public CommunicationAdapter(Context ctx, List<CommunicationDTO> dtos, OnItemClickListener listener){
        this.ctx = ctx;
        this.dtos = dtos;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.communication_itens, parent, false);

        CommunicationViewHolder communicationViewHolder = new CommunicationViewHolder(view, listener);

        return communicationViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommunicationViewHolder communicationViewHolder = (CommunicationViewHolder) holder;

        if(null != dtos) {
            CommunicationDTO dto = dtos.get(position);

            communicationViewHolder.title.setText(dto.getTitle());
            communicationViewHolder.date.setText(DateUtils.dateToString(new Date(dto.getDate())));

            if(null != dto.getFavorite() && dto.getFavorite()) {
                communicationViewHolder.favorite.setImageResource(R.drawable.ic_favorited);
            } else {
                communicationViewHolder.favorite.setImageResource(R.drawable.ic_un_favorite);
            }

            if(null != dto.getChecked() && dto.getChecked()) {
                communicationViewHolder.checked.setImageResource(R.drawable.ic_viewed);
            } else {
                communicationViewHolder.checked.setImageResource(R.drawable.ic_check);
            }

        }
    }

    @Override
    public int getItemCount() {
        if(dtos == null || dtos.isEmpty()) { return 0; }

        return dtos.size();
    }

}
