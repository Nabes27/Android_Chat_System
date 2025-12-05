package com.example.chat_app2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    
    private List<Message> messages;
    private String currentUserName;

    public MessageAdapter(String currentUserName) {
        this.messages = new ArrayList<>();
        this.currentUserName = currentUserName;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message, currentUserName);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessageSelf;
        private TextView tvMessageOther;
        private TextView tvTimestamp;
        private View layoutMessageSelf;
        private View layoutMessageOther;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageSelf = itemView.findViewById(R.id.tv_message_self);
            tvMessageOther = itemView.findViewById(R.id.tv_message_other);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            layoutMessageSelf = itemView.findViewById(R.id.layout_message_self);
            layoutMessageOther = itemView.findViewById(R.id.layout_message_other);
        }

        public void bind(Message message, String currentUserName) {
            boolean isCurrentUser = message.getSenderName().equals(currentUserName);
            
            if (isCurrentUser) {
                layoutMessageSelf.setVisibility(View.VISIBLE);
                layoutMessageOther.setVisibility(View.GONE);
                tvMessageSelf.setText(message.getContent());
            } else {
                layoutMessageSelf.setVisibility(View.GONE);
                layoutMessageOther.setVisibility(View.VISIBLE);
                tvMessageOther.setText(message.getContent());
            }

            // Format timestamp
            if (message.getTimestamp() != null) {
                Date date = message.getTimestamp().toDate();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                tvTimestamp.setText(timeFormat.format(date));
            } else {
                tvTimestamp.setText("");
            }
        }
    }
}