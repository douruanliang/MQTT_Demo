package io.dourl.mqtt.ui.adpater.chat;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import io.dourl.mqtt.R;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.model.message.chat.AudioBody;
import io.dourl.mqtt.utils.AudioController;
import io.dourl.mqtt.utils.log.LoggerUtil;


public class ChatAudioBinder extends ChatFrameBinder<AudioBody, ChatAudioBinder.ViewHolder> {

    @Override
    RecyclerView.ViewHolder onCreateContentViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_chat_audio, parent, false);
        return new ViewHolder(root);
    }

    @Override
    void onBindContentViewHolder(ViewHolder holder, AudioBody audioBody, boolean isMine) {
//        holder.bindData(audioBody, isMine);
        holder.bindData(getMessageModel(), isMine);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView leftTime;
        private TextView rightTime;
        private View background;
        private ImageView imgPlayView;
        private int avatarSize;
        private TextView isReadFlag;

        ViewHolder(View itemView) {
            super(itemView);
            leftTime = (TextView) itemView.findViewById(R.id.leftTime);
            rightTime = (TextView) itemView.findViewById(R.id.rightTime);
            background = itemView.findViewById(R.id.rlBackground);
            imgPlayView = (ImageView) itemView.findViewById(R.id.imgPlayView);
            isReadFlag = (TextView) itemView.findViewById(R.id.isRead);
            avatarSize = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.avatarSize);
        }

        public void bindData(final MessageModel audioMessage, final boolean isMine) {
            AudioBody audio = (AudioBody) audioMessage.getBody();
            RelativeLayout.LayoutParams playViewParams = (RelativeLayout.LayoutParams) imgPlayView.getLayoutParams();
            if (isMine) {
                leftTime.setVisibility(View.VISIBLE);
                leftTime.setText(audio.getContent().getL() + "\"");
                rightTime.setVisibility(View.GONE);
                isReadFlag.setVisibility(View.GONE);
                background.setBackgroundResource(R.drawable.round_corner_rect_green);
                background.setPadding(0, 0, avatarSize, 0);
                imgPlayView.setImageResource(R.drawable.audio_play_out);
                playViewParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                playViewParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            } else {
                leftTime.setVisibility(View.GONE);
                rightTime.setVisibility(View.VISIBLE);
                isReadFlag.setVisibility(audioMessage.isRead() ? View.GONE : View.VISIBLE);
                rightTime.setText(audio.getContent().getL() + "\"");
                background.setBackgroundResource(R.drawable.round_corner_rect_white);
                background.setPadding(avatarSize, 0, 0, 0);
                imgPlayView.setImageResource(R.drawable.audio_play_in);
                playViewParams.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
                playViewParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            }
            imgPlayView.setLayoutParams(playViewParams);
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioController.getInstance().playAudio(audioMessage, new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            LoggerUtil.d("onPrepared"+ audioMessage.getId());
                            ((AnimationDrawable) imgPlayView.getDrawable()).start();
                            isReadFlag.setVisibility(View.GONE);
                        }
                    }, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            LoggerUtil.d("onCompletion" + audioMessage.getId());
//                            animation.stop();
                            if (isMine) {
                                imgPlayView.setImageResource(R.drawable.audio_play_out);
                            } else {
                                imgPlayView.setImageResource(R.drawable.audio_play_in);
                            }
                        }
                    });
                }
            });
        }
    }
}