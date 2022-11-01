package io.dourl.mqtt.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;

import java.util.List;

import io.dourl.mqtt.R;
import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.configuration.ColorModel;
import io.dourl.mqtt.configuration.ConfigManager;
import io.dourl.mqtt.model.ClanModel;
import io.dourl.mqtt.model.typeadapter.BaseClanModel;
import io.dourl.mqtt.utils.IMTextBodyUtils;
import io.dourl.mqtt.utils.MessageTimeUtils;
import io.dourl.mqtt.utils.chat.TextBodyContentUtils;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/31
 */
public class SessionItemBinder extends ItemViewBinder<SessionModel, SessionItemBinder.SessionViewHolder> {
    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder sessionViewHolder, SessionModel sessionModel) {
        sessionViewHolder.bindData(sessionModel);
        sessionViewHolder.itemView.setOnClickListener(view -> {
            switch (sessionModel.getMsgType()) {
                case CHAT_GROUP:
                    GroupChatActivity.intentTo(view.getContext(), sessionModel.getSessionID(), sessionModel.getClan());
                    break;
                case CHAT_NORMAL:
                    ChatActivity.intentTo(view.getContext(), sessionModel.getSessionID(), sessionModel.getUser());
                    break;
            }
        });
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageViewAvatar;
        TextView mTextViewName;
        TextView mTextViewContent;
        TextView mTextViewTime;
        ViewGroup mLLayout;
        TextView mTextViewUnRead;
        ImageView mVipIcon;
        ImageView mUserAgent;
        ImageView mUnReceiveIcon;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewAvatar = itemView.findViewById(R.id.webImageView_avatar);
            mTextViewName = itemView.findViewById(R.id.textView_name);
            mTextViewContent = itemView.findViewById(R.id.textView_content);
            mTextViewTime = itemView.findViewById(R.id.textView_time);
            mTextViewUnRead = itemView.findViewById(R.id.textView_unread);
            mLLayout = itemView.findViewById(R.id.llayout);
            mVipIcon = itemView.findViewById(R.id.clan_vip);
            mUserAgent = itemView.findViewById(R.id.user_agent);
            mUnReceiveIcon = itemView.findViewById(R.id.receive_able_view);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bindData(SessionModel data) {
            switch (data.getMsgType()) {
                case CHAT_GROUP:
                    ClanModel clanModel = data.getClan();
                    if (clanModel != null) {
                        /*if (clanModel.avatar.startsWith("clan_cover")) {
                            //mWebImageViewAvatar.setImageUrl("res://" + BuildConfig.APPLICATION_ID + "/" +
                            //       itemView.getContext().getResources().getIdentifier(clanModel.avatar, "drawable", BuildConfig.APPLICATION_ID));
                        } else {
                            //mWebImageViewAvatar.setImageUrl(clanModel.avatar);
                        }*/
                        mTextViewName.setText(clanModel.name);
                        mVipIcon.setVisibility(clanModel.getSpec_clan() == BaseClanModel.AuthType.vip ? View.VISIBLE : View.GONE);
                        mUnReceiveIcon.setVisibility(clanModel.msg_receive ? View.GONE : View.VISIBLE);
                        mLLayout.setBackground(mLLayout.getContext().getResources().getDrawable(clanModel.msg_top ? R.drawable.session_item_bg_top : R.drawable.session_item_bg));
                    }
                    mUserAgent.setVisibility(View.GONE);
                    break;
                case CHAT_NORMAL:
                    UserModel user = data.getUser();
                    if (user != null) {
                        // mImageViewAvatar.setImageUrl(user.getAvatar());
                        mTextViewName.setText(user.getName());
                        // mUserAgent.setVisibility(user.isAgent() ? View.VISIBLE : View.GONE);
                    }
                    mVipIcon.setVisibility(View.GONE);
                    mUnReceiveIcon.setVisibility(View.GONE);
                    mLLayout.setBackground(mLLayout.getContext().getResources().getDrawable(R.drawable.session_item_bg));
                    break;
            }
            //草稿
            if (TextUtils.isEmpty(data.getDraft())) {
                if (data.getSessionMsg() != null) {
                    if (data.getClan() != null && data.getClan().hasAt) {
                        String atHint = itemView.getContext().getString(R.string.hint_at);
                        String content = atHint + data.getSessionMsg().getContentDesc();
                        SpannableStringBuilder builder = new SpannableStringBuilder(content);
                        builder.setSpan(new AbsoluteSizeSpan(12, true), content.indexOf(atHint), content.indexOf(atHint) + atHint.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), content.indexOf(atHint), content.indexOf(atHint) + atHint.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        mTextViewContent.setText(builder, TextView.BufferType.SPANNABLE);
                    } else {
                        mTextViewContent.setText(data.getSessionMsg().getContentDesc(), TextView.BufferType.SPANNABLE);
                    }
                }

            } else {
                mTextViewContent.setText(TextBodyContentUtils.getSpannableDraftContent(mTextViewContent.getContext(), IMTextBodyUtils.createTextBody(data.getDraft())), TextView.BufferType.SPANNABLE);
            }

            setColor();
            mTextViewTime.setText(MessageTimeUtils.formatDateTime(itemView.getContext(), data.getCreateTime()));
            if (data.getUnreadMsgCount() > 0) {
                mTextViewUnRead.setVisibility(View.VISIBLE);
                String text = null;
                if (data.getUnreadMsgCount() <= 99) {
                    text = String.valueOf(data.getUnreadMsgCount());
                } else {
                    text = "99+";
                }
                mTextViewUnRead.setText(text);
            } else {
                mTextViewUnRead.setVisibility(View.GONE);
            }
        }

        private void setColor() {
            Drawable drawable = mLLayout.getBackground().mutate();
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
            int position = getAdapterPosition();
            List<ColorModel> colors = ConfigManager.getInstance().getColors();
            String color = colors.get(position % colors.size()).getColor();
            int c = 0;
            try {
                c = Color.parseColor(color);
            } catch (Exception e) {
                c = Color.parseColor("fuchsia");
            }
            DrawableCompat.setTint(drawable, c);
        }


    }
}
