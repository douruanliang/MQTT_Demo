package io.dourl.mqtt.ui.adpater.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;
import io.dourl.mqtt.R;
import io.dourl.mqtt.model.message.chat.VideoBody;


public class ChatVideoBinder extends ChatFrameBinder<VideoBody, ChatVideoBinder.ViewHolder> {

    @Override
    RecyclerView.ViewHolder onCreateContentViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_chat_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    void onBindContentViewHolder(ViewHolder holder, VideoBody videoBody, boolean isMine) {
        holder.bindData(videoBody, isMine);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private View rootView;
        private ImageView imgCover;
        private TextView tvDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            rootView = itemView.findViewById(R.id.rootView);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
        }

        public void bindData(final VideoBody videoBody, boolean isMine) {
            ViewGroup.LayoutParams params = imgCover.getLayoutParams();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rootView.getLayoutParams();
            RelativeLayout.LayoutParams imglp = (RelativeLayout.LayoutParams) imgCover.getLayoutParams();
            if (isMine) {
                lp.leftMargin = 0;
                lp.rightMargin = imageMargin;
               /* params.setCornersRadii(DeviceInfoUtils.dip2px(itemView.getContext(),10), 0,
                        DeviceInfoUtils.dip2px(itemView.getContext(),10),
                        DeviceInfoUtils.dip2px(itemView.getContext(),10));*/
            } else {
                lp.leftMargin = imageMargin;
                lp.rightMargin = 0;
               /* params.setCornersRadii(0, DeviceInfoUtils.dip2px(itemView.getContext(), 10),
                        DeviceInfoUtils.dip2px(itemView.getContext(),10),
                        DeviceInfoUtils.dip2px(itemView.getContext(),10));*/
            }
            int[] values = getCoverWidthHeight(videoBody.getContent().getW(), videoBody.getContent().getH());
            if (values != null) {
                lp.width = imglp.width = values[0];
                lp.height = imglp.height = values[1];
            }

            imgCover.setLayoutParams(imglp);
            rootView.setLayoutParams(lp);
            //imgCover.getHierarchy().setRoundingParams(params);
            //imgCover.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            String coverPath = videoBody.getCoverPath();
            if (TextUtils.isEmpty(coverPath)) {
                coverPath = videoBody.getContent().getCover_r();
            }
          //  imgCover.setImageUrl(coverPath);
          //  tvDuration.setText(MessageTimeUtils.secondsFormat(videoBody.getContent().getL(), "mm:ss"));
            imgCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    VideoPlayerActivity.intentTo(mContext, videoBody.getLocalOrServerPath());
                    if (!TextUtils.isEmpty(videoBody.getLocalPath()) /*&& FileUtils.isFileExist(videoBody.getLocalPath())*/) {
                      //  AndroidUtils.openVideoPlayer(mContext, new File(videoBody.getLocalPath()), Constants.FILE_PROVIDER);
                    } else {
                      //  AndroidUtils.openVideoPlayer(mContext, videoBody.getLocalOrServerPath());
                    }
                }
            });
        }
    }
}
