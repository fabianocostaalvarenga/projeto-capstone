package com.fca.agenda;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fabiano.alvarenga on 11/04/18.
 */

public class CommunicationViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView date;
    ImageView checked;
    ImageView favorite;

    public CommunicationViewHolder(View itemView, final OnItemClickListener listener) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.date = (TextView) itemView.findViewById(R.id.date);

        this.checked = (ImageView)itemView.findViewById(R.id.ic_check);
        this.favorite = (ImageView)itemView.findViewById(R.id.ic_favorite);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, getLayoutPosition());
            }
        });
    }

}
