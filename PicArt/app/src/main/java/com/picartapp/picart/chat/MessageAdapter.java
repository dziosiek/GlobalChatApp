package com.picartapp.picart.chat;



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.picartapp.picart.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private List<Message> messageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, time, message;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.chatNickname);
            message = (TextView) view.findViewById(R.id.chatMessage);
            time = (TextView) view.findViewById(R.id.chatTime);
        }
    }


    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Message message = messageList.get(position);
        holder.nickname.setText(message.getUid());
        holder.message.setText(message.getContents());
        holder.time.setText(dateFormat.format(message.getTime()));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
