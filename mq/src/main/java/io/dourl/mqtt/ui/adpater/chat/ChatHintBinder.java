package io.dourl.mqtt.ui.adpater.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;
import io.dourl.mqtt.R;
import io.dourl.mqtt.model.message.chat.TextBody;


public class ChatHintBinder extends ChatFrameBinder<TextBody, ChatHintBinder.ViewHolder> {
    @Override
    RecyclerView.ViewHolder onCreateContentViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_chat_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    void onBindContentViewHolder(ViewHolder holder, TextBody textBody, boolean isMine) {
        holder.bindData(textBody, isMine);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mTextView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void bindData(TextBody textBody, boolean isMine) {
//            StringBuilder builder = new StringBuilder();
//            for (TextBody.TextEntity textEntity : textBody.getContent()) {
//                builder.append(textEntity.getC());
//            }
//            Spannable span = ImSmileUtils.getSmiledText(mContext, builder.toString());
            // 设置内容
//            mTextView.setText(span, TextView.BufferType.SPANNABLE);
            mTextView.setMaxWidth(textWidth);
            if (isMine) {
                mTextView.setBackgroundResource(R.drawable.round_corner_rect_green);
                mTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                mTextView.setPadding(textPadding, textPadding, textPadding + avatarSize, textPadding);
            } else {
                mTextView.setBackgroundResource(R.drawable.round_corner_rect_white);
                mTextView.setTextColor(mContext.getResources().getColor(R.color.color_5d697f));
                mTextView.setPadding(textPadding + avatarSize, textPadding, textPadding, textPadding);
            }
        }
    }
}
