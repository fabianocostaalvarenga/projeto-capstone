package com.fca.agenda;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fabiano.alvarenga on 11/04/18.
 */

public class PersonalDataViewHolder extends RecyclerView.ViewHolder {

    TextView registration;
    TextView name;

    public PersonalDataViewHolder(View itemView, final OnItemClickListener listener) {
        super(itemView);
        this.registration = (TextView) itemView.findViewById(R.id.studantRegistration);
        this.name = (TextView) itemView.findViewById(R.id.studantName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, getPosition());
            }
        });
    }

}
