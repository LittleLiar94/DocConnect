package com.example.docconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.docconnect.Common.Common;
import com.example.docconnect.Interface.IRecyclerItemSelectedListener;
import com.example.docconnect.Model.Labor;
import com.example.docconnect.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyLaborAdapter extends RecyclerView.Adapter<MyLaborAdapter.MyViewHolder> {

    Context context;
    List<Labor> laborList;
    List<CardView> cardViewList;

    LocalBroadcastManager localBroadcastManager;

    public MyLaborAdapter(Context context, List<Labor> laborList) {
        this.context = context;
        this.laborList = laborList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_labor,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.tv_labor_name.setText(laborList.get(position).getName());

        if(laborList.get(position).getRatingTimes() != null)
            myViewHolder.ratingBar_labor.setRating((float) laborList.get(position).getRating().floatValue() / laborList.get(position).getRatingTimes());
        else
            myViewHolder.ratingBar_labor.setRating(0);

        if(!cardViewList.contains(myViewHolder.card_labor))
            cardViewList.add(myViewHolder.card_labor);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }

                // Set background for chosen Barber
                myViewHolder.card_labor.setCardBackgroundColor(
                        context.getResources()
                                .getColor(R.color.colorPrimary)
                );


                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_LABOR_SELECTED, laborList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);

                // IMPORTANT! This tell the ServiceFragment that this labor is selected!
                Common.labor = laborList.get(pos).getLaborId();

                localBroadcastManager.sendBroadcast(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return laborList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_labor_name;
        RatingBar ratingBar_labor;
        CardView card_labor;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_labor_name = (TextView) itemView.findViewById(R.id.tv_labor_name);
            ratingBar_labor = (RatingBar) itemView.findViewById(R.id.rating_labor);
            card_labor = (CardView) itemView.findViewById(R.id.card_labor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
                }
            });
        }
    }
}
