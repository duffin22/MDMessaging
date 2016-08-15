package com.duffin22.mdmessage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.android.AndroidPlatform;

import java.util.List;

/**
 * Created by matthewtduffin on 14/08/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    private int rowLayout;
    private Context context;
    public String lastId;


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView bodyText, dateText, userText;
        LinearLayout cardLayout, bigLayout;
        CardView messageCard;
        View view;


        public MessageViewHolder(View v) {
            super(v);
            bigLayout = (LinearLayout) v.findViewById(R.id.message_layout);
            bodyText = (TextView) v.findViewById(R.id.message_body);
            dateText = (TextView) v.findViewById(R.id.message_date);
            userText = (TextView) v.findViewById(R.id.message_user);
            messageCard = (CardView) v.findViewById(R.id.message_card);
            cardLayout = (LinearLayout) v.findViewById(R.id.card_linear_layout);
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

        User currentUser;
        try {
            currentUser = MainActivity.allUsers.get(messages.get(position).getUserId());
        } catch (Exception e) {
            currentUser = new User(messages.get(position).getUserId());
        }

        holder.bodyText.setText(messages.get(position).getBody());
        holder.dateText.setText(messages.get(position).getDate());
        try {
            holder.userText.setText(currentUser.getAlias());
        } catch (Exception e) {
            holder.userText.setText(messages.get(position).getUserId());
            e.printStackTrace();
        }

        try {
            if (messages.get(position).getUserId().equals(messages.get(position - 1).getUserId())) {
                holder.userText.setVisibility(View.GONE);
            } else {
                holder.userText.setVisibility(View.VISIBLE);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            holder.userText.setVisibility(View.VISIBLE);
        }


        int color = 0, oppositeColor = 0, softColor = 0, backColor = 0;
        if (messages.get(position).getUserId().equals(MainActivity.userId)) {
            holder.bigLayout.setGravity(Gravity.START);
            try {
                holder.userText.setText(currentUser.getAlias() + " (me)");
            } catch (Exception e) {
                holder.userText.setText(messages.get(position).getUserId());
                e.printStackTrace();
            }
            try {
                color = MainActivity.userColor;
                oppositeColor = getOpposite(color);
                softColor = getSoft(color);
            } catch (Exception e) {
                color = holder.view.getResources().getColor(R.color.colorAccent);
                oppositeColor = getOpposite(color);
                softColor = getSoft(color);
            }
            backColor = holder.view.getResources().getColor(R.color.colorPrimary);
        } else {
            holder.bigLayout.setGravity(Gravity.END);
            color = holder.view.getResources().getColor(R.color.grey);
            oppositeColor = getOpposite(color);
            softColor = getSoft(color);
            backColor = holder.view.getResources().getColor(R.color.colorAccent);
        }

        holder.userText.setTextColor(color);
//        holder.userText.setTextColor(backColor);
        holder.cardLayout.setBackgroundColor(softColor);
        holder.bodyText.setTextColor(oppositeColor);


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

        lastId = messages.get(position).getUserId();


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public int getSoft(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(180,red,green,blue);
    }

    public int getOpposite(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(255,255-red,255-green,255-blue);
    }

}
