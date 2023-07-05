package io.dourl.mqtt.ui.adpater.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import io.dourl.mqtt.R;
import io.dourl.mqtt.model.message.chat.ImageBody;


public class ChatImageBinder extends ChatFrameBinder<ImageBody, ChatImageBinder.ViewHolder> {
    @Override
    RecyclerView.ViewHolder onCreateContentViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_chat_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    void onBindContentViewHolder(ViewHolder holder, ImageBody imageBody, boolean isMine) {
        holder.bindData(imageBody, isMine);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   PhotoViewActivity.intentTo(holder.mContext, isMine && FileUtils.isFileExist(imageBody.getLocalPath()) ?
                        imageBody.getLocalPath() : imageBody.getServerPath(), mAdapter.getImageDataList());*/
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mImageView = itemView.findViewById(R.id.imageView);
        }

        public void bindData(ImageBody imageBody, boolean isMine) {
            //RoundingParams params = mImageView.getHierarchy().getRoundingParams();
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
            if (isMine) {
                lp.leftMargin = 0;
                lp.rightMargin = imageMargin;
                //params.setCornersRadii(AppContextUtil.dip2px(10), 0, AppContextUtil.dip2px(10), AppContextUtil.dip2px(10));
            } else {
                lp.leftMargin = imageMargin;
                lp.rightMargin = 0;
                //params.setCornersRadii(0, AppContextUtil.dip2px(10), AppContextUtil.dip2px(10), AppContextUtil.dip2px(10));
            }
            int[] values = getCoverWidthHeight(imageBody.getContent().getW(), imageBody.getContent().getH());
            if (values != null) {
                lp.width = values[0];
                lp.height = values[1];
            }
            mImageView.setLayoutParams(lp);
            //mImageView.getHierarchy().setRoundingParams(params);
           // mImageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            final String coverUrl = imageBody.getLocalOrServerPath();
            /*mImageView.setImageUrl(coverUrl, R.color.gray, ScalingUtils.ScaleType.CENTER_CROP,
                    FrescoHelper.TransformType.NONE, FrescoHelper.ProgressBarType.CIRCLE, null, null, true);*/
        }
    }
}
