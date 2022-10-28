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


public class ChatTextProvider extends ChatFrameProvider<TextBody, ChatTextProvider.ViewHolder> {

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
                final StringBuilder builder = new StringBuilder();
                // 设置内容
                Spannable span = null;
//                for (TextBody.TextEntity textEntity : textBody.getContent()) {
//                    if (textEntity.getT().equals("notice")) {
//                        List<TextBody.TextEntity> notice = new Gson().fromJson(textEntity.getC(), new TypeToken<List<TextBody.TextEntity>>() {
//                        }.getType());
//                        String title = mContext.getResources().getString(R.string.chat_all);
//                        builder.append(title);
//                        builder.append("\n");
//                        for (TextBody.TextEntity item : notice) {
//                            builder.append(item.getC());
//                        }
//                        span = Spannable.Factory.getInstance().newSpannable(builder.toString());
//                        span.setSpan(new ForegroundColorSpan(Color.BLUE), builder.indexOf(title), builder.indexOf(title) + title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                    } else {
//                        builder.append(textEntity.getC());
//                        // 设置内容
//                        span = ImSmileUtils.getSmiledText(mContext, builder.toString());
//                    }
//                }
                span = TextBodyContentUtils.getSpannableContent(mContext, textBody.getContent());
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
