package com.example.docconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.docconnect.Common.Common;
import com.example.docconnect.Interface.IRecyclerItemSelectedListener;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.TimeSlot;
import com.example.docconnect.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    Premise premiseInfo;

    // Create cardview list for user to choose
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context, Premise premiseInfo) {
        this.context = context;
        this.premiseInfo = premiseInfo;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList= new ArrayList<>();
        this.premiseInfo=new Premise();

        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();

    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList, Premise premiseInfo) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.premiseInfo = premiseInfo;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    // Set the text in cardview of time slot
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

//        myViewHolder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(i)).toString());

        myViewHolder.txt_time_slot.setText(premiseInfo.getTimeSlot().get(i));

        if(timeSlotList == null){ // If all the position is available

            //Part 29: If all time slot is empty, all card is enable
            myViewHolder.card_time_slot.setEnabled(true);

            myViewHolder.txt_time_slot.setTextColor(context.getResources()
                    .getColor(android.R.color.black));
            myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                    .getColor(android.R.color.white));

        }
        else{ // if the position is fully booked
            for(TimeSlot slotValue:timeSlotList){
                //Loop all time slot form server to  different color
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == i){ // If slot == position

                    // We will set tag for all the time slot that's full. So based on tag, we can set
                    // all the remaining card background without changing full time slot.

                    //Part 29: If all time slot is empty, all card is enable
                    myViewHolder.card_time_slot.setEnabled(false);

                    myViewHolder.card_time_slot.setTag(Common.DISABLE_TAG);
                    myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.darker_gray));
                    myViewHolder.txt_time_slot.setTextColor(context.getResources()
                            .getColor(android.R.color.white));

                }
            }
        }

        // Add all 20 card to list
        if(!cardViewList.contains(myViewHolder.card_time_slot))
            cardViewList.add(myViewHolder.card_time_slot);

        //Check if the time_slot is available
        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Loop all card in cardList
                for(CardView cardView:cardViewList) {
                    if (cardView.getTag() == null) // Only available card time_slotto be change
                        cardView.setCardBackgroundColor(context.getResources()
                                .getColor(android.R.color.white));

                }

                // Selected card will change color
                myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                        .getColor(R.color.colorPrimary));
//                myViewHolder.txt_time_slot.setTextColor(context.getResources()
//                        .getColor(android.R.color.white));

                // after that, send broadcast to enable button NEXT
                //Part 27: Removed
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_TIME_SLOT, i); // Put index of time_slot selected
                intent.putExtra(Common.KEY_STEP,3); // Go to Step 3
                localBroadcastManager.sendBroadcast(intent);
                // IMPORTANT! This tell the ServiceFragment that this timeslot is selected!
                Common.timeSlot = premiseInfo.getTimeSlot().get(pos);
                // Part 27:
//                EventBus.getDefault().postSticky(new EnableNextButton(3, i));

            }
        });
    }

    @Override
    public int getItemCount() {
        return premiseInfo.getTimeSlot().size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
