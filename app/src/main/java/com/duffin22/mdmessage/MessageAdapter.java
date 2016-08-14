package com.duffin22.mdmessage;

import android.content.Context;
import android.content.Intent;
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
        TextView senderText, dateText, snippetText, letterText, subjectText;
        View view;


        public MessageViewHolder(View v) {
            super(v);
            //TODO: Add reference to view on the message card view
//            senderText = (TextView) v.findViewById(R.id.senderView);
//            subjectText = (TextView) v.findViewById(R.id.subjectView);
//            dateText = (TextView) v.findViewById(R.id.dateView);
//            snippetText = (TextView) v.findViewById(R.id.snippetView);
//            letterText = (TextView) v.findViewById(R.id.letterView);
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
    public void onBindViewHolder(MessageViewHolder holder, final int position) {

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringy = "Pressed "+ messages.get(position).getMessageId();
                Toast.makeText(context, stringy, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
