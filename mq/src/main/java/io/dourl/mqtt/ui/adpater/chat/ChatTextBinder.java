package io.dourl.mqtt.ui.adpater.chat;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.dourl.mqtt.R;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.utils.chat.TextBodyContentUtils;
import io.dourl.mqtt.utils.log.LoggerUtil;


public class ChatTextBinder extends ChatFrameBinder<TextBody, ChatTextBinder.ViewHolder> {

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
            if (textBody.getSpanContent() == null) {
                // 设置内容
                Spannable span = TextBodyContentUtils.getSpannableContent(mContext, textBody.getContent());
                mTextView.setText(span, TextView.BufferType.SPANNABLE);
            } else {
                mTextView.setText(textBody.getSpanContent(), TextView.BufferType.SPANNABLE);
            }

            mTextView.setMaxWidth(textWidth);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTextView.getLayoutParams();
            if (isMine) {
                mTextView.setBackgroundResource(R.drawable.round_corner_rect_green);
                mTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                lp.leftMargin = 0;
                lp.rightMargin = imageMargin;
                mTextView.setPadding(textPadding, textPadding, textPadding, textPadding);
            } else {
                mTextView.setBackgroundResource(R.drawable.round_corner_rect_white);
                mTextView.setTextColor(mContext.getResources().getColor(R.color.color_5d697f));
                lp.leftMargin = imageMargin;
                lp.rightMargin = 0;
                mTextView.setPadding(textPadding, textPadding, textPadding, textPadding);
            }
            mTextView.setLayoutParams(lp);
        }
    }
}
