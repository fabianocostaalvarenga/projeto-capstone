package com.fca.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.dto.StudantDTO;
import com.fca.agenda.dto.UserDTO;
import com.fca.agenda.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by fabiano.alvarenga on 11/04/18.
 */

public class PersonalDataAdapter extends RecyclerView.Adapter {

    private OnItemClickListener listener;
    private Context ctx;
    private List<StudantDTO> dtos;

    public PersonalDataAdapter(Context ctx, List<StudantDTO> dtos, OnItemClickListener listener){
        this.ctx = ctx;
        this.dtos = dtos;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.personal_data_itens, parent, false);

        PersonalDataViewHolder personalDataViewHolder = new PersonalDataViewHolder(view, listener);

        return personalDataViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PersonalDataViewHolder personalDataViewHolder = (PersonalDataViewHolder) holder;

        if(null != dtos) {
            StudantDTO dto = dtos.get(position);

            personalDataViewHolder.name.setText(dto.getName());
            personalDataViewHolder.registration.setText(dto.getRegistration());

        }
    }

    @Override
    public int getItemCount() {
        if(dtos == null || dtos.isEmpty()) { return 0; }

        return dtos.size();
    }

}
