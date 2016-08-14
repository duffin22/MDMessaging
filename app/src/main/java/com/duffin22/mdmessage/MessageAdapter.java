package com.duffin22.mdmessage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by matthewtduffin on 14/08/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    private int rowLayout;
    private Context context;


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView bodyText, dateText, userText;
        CardView messageCard;
        View view;


        public MessageViewHolder(View v) {
            super(v);
            //TODO: Add reference to view on the message card view
            bodyText = (TextView) v.findViewById(R.id.message_body);
            dateText = (TextView) v.findViewById(R.id.message_date);
            userText = (TextView) v.findViewById(R.id.message_user);
            messageCard = (CardView) v.findViewById(R.id.message_card);
            view = v;

        }
    }

    public MessageAdapter(List<Message> messages, int rowLayout, Context context) {
        this.messages = messages;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MessageViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final MessageViewHolder holder, final int position) {

        holder.bodyText.setText(messages.get(position).getBody());
        holder.dateText.setText(messages.get(position).getDate());
        holder.userText.setText(messages.get(position).getUserId());

        holder.messageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.dateText.getVisibility() == View.GONE) {
                    holder.dateText.setVisibility(View.VISIBLE);
                } else {
                    holder.dateText.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
