package io.dourl.mqtt.ui.adpater.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import butterknife.ButterKnife;
import io.dourl.mqtt.R;
import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.constants.Constants;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.model.BaseUser;
import io.dourl.mqtt.model.customenum.ManagerType;
import io.dourl.mqtt.model.message.chat.AudioBody;
import io.dourl.mqtt.model.message.chat.BaseMsgBody;
import io.dourl.mqtt.model.message.chat.HintBody;
import io.dourl.mqtt.model.message.chat.ImageBody;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.model.message.chat.VideoBody;
import io.dourl.mqtt.utils.IMTextBodyUtils;
import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by SpiritTalk on 17/1/12.
 */

public abstract class ChatFrameProvider<Content extends BaseMsgBody, SubViewHolder extends RecyclerView.ViewHolder>
        extends ItemViewBinder<MessageModel, ChatFrameProvider.FrameHolder> {
    protected static int textPadding;
    protected static int avatarSize;
    protected static int imageMargin;
    protected static int coverMaxWidth;
    protected static int coverMaxHeight;
    protected static int textWidth;

    private MessageModel mMessageModel;
    private static PopupWindow popupView;
    protected ChatAdapter mAdapter;

    abstract RecyclerView.ViewHolder onCreateContentViewHolder(LayoutInflater inflater, ViewGroup parent);

    abstract void onBindContentViewHolder(SubViewHolder holder, Content content, boolean isMine);

    @Override
    protected FrameHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        textPadding = parent.getResources().getDimensionPixelSize(R.dimen.chat_item_text_padding);
        avatarSize = parent.getResources().getDimensionPixelSize(R.dimen.chat_avatarSize);
        imageMargin = parent.getResources().getDimensionPixelSize(R.dimen.chat_item_image_margin);
        textWidth = Constants.SCREENWIDTH - 2 * parent.getResources().getDimensionPixelSize(R.dimen.chat_item_margin) - textPadding - avatarSize - imageMargin;
        coverMaxWidth = Constants.SCREENWIDTH - 2 * (imageMargin + parent.getResources().getDimensionPixelSize(R.dimen.chat_item_margin));
        coverMaxWidth = (int) (coverMaxWidth * 0.8);
        coverMaxHeight = coverMaxWidth * 4 / 3;
        mAdapter = (ChatAdapter) getAdapter();
        View root = inflater.inflate(R.layout.item_chat_frame, parent, false);
        RecyclerView.ViewHolder contentHolder = onCreateContentViewHolder(inflater, parent);
        return new FrameHolder(root, contentHolder);
    }

    @Override
    protected void onBindViewHolder(@NonNull FrameHolder holder, @NonNull final MessageModel messageModel) {
        mMessageModel = messageModel;
        holder.bindData(messageModel, mAdapter.mClanMember, mAdapter.mMyManagrType);
        if (!(messageModel.getBody() instanceof HintBody)) {
            onBindContentViewHolder((SubViewHolder) holder.contentHolder, (Content) messageModel.getBody(), messageModel.isMine());
        }

        holder.leftAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAdapter.mOnItemActionListener.at(messageModel);
                return true;
            }
        });

        if (messageModel.getBody() instanceof TextBody) {
            holder.contentHolder.itemView.findViewById(R.id.textView).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemAction(view, messageModel, true, false);
                    return false;
                }
            });
        } else if (messageModel.getBody() instanceof AudioBody) {
            holder.contentHolder.itemView.findViewById(R.id.rlBackground).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemAction(view, messageModel, false, false);
                    return false;
                }
            });
        } else if (messageModel.getBody() instanceof ImageBody) {
            holder.contentHolder.itemView.findViewById(R.id.imageView).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemAction(view, messageModel, false, false);
                    return false;
                }
            });
        } else if (messageModel.getBody() instanceof VideoBody) {
            holder.contentHolder.itemView.findViewById(R.id.imgCover).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemAction(view, messageModel, false, true);
                    return false;
                }
            });
        }


    }

    protected void itemAction(View view, final MessageModel messageModel, boolean copyAble, boolean saveAble) {

        View popView = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_chat_item_pop, null, false);
        View copy = popView.findViewById(R.id.item_action_copy);
        if (copyAble) {
            copy.setVisibility(View.VISIBLE);
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPop();
                    mAdapter.mOnItemActionListener.copy(messageModel);
                }
            });
        } else {
            copy.setVisibility(View.GONE);
        }

        View save = popView.findViewById(R.id.item_action_save);
        if (saveAble) {
            save.setVisibility(View.VISIBLE);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissPop();
                    mAdapter.mOnItemActionListener.save(messageModel);
                }
            });
        } else {
            save.setVisibility(View.GONE);
        }
        popView.findViewById(R.id.item_action_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPop();
                mAdapter.mOnItemActionListener.delete(messageModel);
            }
        });
        popView.findViewById(R.id.item_action_forword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPop();
                mAdapter.mOnItemActionListener.forward(messageModel);
            }
        });
        popView.findViewById(R.id.item_action_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPop();
                mAdapter.mOnItemActionListener.withdraw(messageModel);
            }
        });
        popupView = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupView.setTouchable(true);
        popupView.setOutsideTouchable(true);
        popupView.setBackgroundDrawable(new BitmapDrawable(view.getContext().getResources(), (Bitmap) null));
        popView.measure(0, 0);
        //popupView.showAsDropDown(view, (view.getWidth() - popView.getMeasuredWidth()) / 2, -view.getHeight() - popView.getMeasuredHeight() - (float)DeviceInfoUtils.dip2px(5));

    }

    public MessageModel getMessageModel() {
        return mMessageModel;
    }

    public static void setCoverWidthHeight(ImageView cover, int width, int height) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cover.getLayoutParams();
        int[] values = getCoverWidthHeight(width, height);
        if (values != null) {
            lp.width = values[0];
            lp.height = values[1];
            cover.setLayoutParams(lp);
        }
    }

    private void dismissPop() {
        if (popupView != null && popupView.isShowing())
            popupView.dismiss();
    }

    public static int[] getCoverWidthHeight(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        int[] value = new int[2];
        value[0] = width > coverMaxWidth ? coverMaxWidth : width;
        value[1] = (int) (((float) height / width) * value[0]) > coverMaxHeight ? coverMaxHeight : (int) (((float) height / width) * value[0]);
        LoggerUtil.d("cover max width: " + coverMaxWidth);
        LoggerUtil.d(String.format("cover [%d,%d] to [%d,%d]", width, height, value[0], value[1]));
        return value;
    }

    static class FrameHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private RecyclerView.ViewHolder contentHolder;
        TextView tvTime;
        LinearLayout mNickNameLayout;
        TextView tvNickName;
        ImageView mLevelLogo;
        TextView tvHint;

        RelativeLayout mContentLayout;

        ImageView leftAvatar;

        ImageView rightAvatar;
        FrameLayout container;
        View statusView;
        ProgressBar progressBar;

        ImageView ivFail;
        public FrameHolder(View itemView, RecyclerView.ViewHolder viewHolder) {
            super(itemView);
            mContext = itemView.getContext();
            container.addView(viewHolder.itemView);
            this.contentHolder = viewHolder;
        }

        public void bindData(final MessageModel messageModel, HashMap<String, BaseUser> members, ManagerType type) {
            // 设置时间
            if (messageModel.isShowTime()) {
                tvTime.setVisibility(View.VISIBLE);
                //TODO
               // tvTime.setText(MessageTimeUtils.formatDateTime(mContext, messageModel.getTime()));
            } else {
                tvTime.setVisibility(View.GONE);
            }
            if (messageModel.getBody() instanceof HintBody) {
                tvHint.setVisibility(View.VISIBLE);
                mContentLayout.setVisibility(View.GONE);
                HintBody body = (HintBody) messageModel.getBody();
                tvHint.setText(IMTextBodyUtils.processHintTxt(body));

            } else {
                tvHint.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                // 设置 view 相对位置
                RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
                RelativeLayout.LayoutParams statusParams = (RelativeLayout.LayoutParams) statusView.getLayoutParams();
                if (messageModel.isMine()) {
                    containerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                    containerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    statusParams.addRule(RelativeLayout.LEFT_OF, R.id.container);
                    statusParams.addRule(RelativeLayout.RIGHT_OF, 0);
                    mNickNameLayout.setVisibility(View.GONE);
                    //TODO
                    //rightAvatar.setImageUrl(LoginManager.getInstance().getCurrentUser().getAvatar());
                    leftAvatar.setVisibility(View.GONE);
                    rightAvatar.setVisibility(View.VISIBLE);
                } else {
                    if (messageModel.getClan() != null) {
                        mNickNameLayout.setVisibility(View.VISIBLE);
                        tvNickName.setText(messageModel.getFromUser().getFullNameOrUserName());

                        BaseUser user = members.get(messageModel.getFromUser().getUid());
                        if (user != null) {
                            tvNickName.setText(user.getDisplayName());
                            if (!TextUtils.isEmpty(user.levellogo)) {
                                mLevelLogo.setVisibility(View.VISIBLE);
                                //TODO
                               /* mLevelLogo.setImageUrl(user.levellogo, 0, ScalingUtils.ScaleType.CENTER_INSIDE,
                                        FrescoHelper.TransformType.NONE, FrescoHelper.ProgressBarType.NONE, null, null, true);*/
                            } else {
                                mLevelLogo.setVisibility(View.GONE);
                            }
                            switch (type) {
                                case manager:
                                case leader:
                                    //leftAvatar.setOnClickListener(v -> ClanMemberDetailActivity.intentTo(mContext, user, messageModel.getClan().id, ClanMemberDetailActivity.TYPE_MOREMEMBER));
                                    break;
                                case normal:
                                    //leftAvatar.setOnClickListener(v -> ProfileInfoActivity.intentTo(mContext, messageModel.getFromUid()));
                                    break;
                            }
                        } else {
                            mLevelLogo.setVisibility(View.GONE);
                           // leftAvatar.setOnClickListener(v -> ProfileInfoActivity.intentTo(mContext, messageModel.getFromUser().getUid()));
                        }
                    } else {
                        mNickNameLayout.setVisibility(View.GONE);
                       // leftAvatar.setOnClickListener(v -> ProfileInfoActivity.intentTo(mContext, messageModel.getFromUid()));
                    }

                    containerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    containerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                    statusParams.addRule(RelativeLayout.LEFT_OF, 0);
                    statusParams.addRule(RelativeLayout.RIGHT_OF, R.id.container);
                    //todo
                    //leftAvatar.setImageUrl(messageModel.getFromUser().getAvatar());
                    leftAvatar.setVisibility(View.VISIBLE);
                    rightAvatar.setVisibility(View.GONE);
                }
                statusView.setLayoutParams(statusParams);
                container.setLayoutParams(containerParams);


                // 设置 statusView
                if (messageModel.getSendStatus() == MessageModel.Status.sending) {
                    progressBar.setVisibility(View.VISIBLE);
                    ivFail.setVisibility(View.GONE);
                } else if (messageModel.getSendStatus() == MessageModel.Status.fail) {
                    progressBar.setVisibility(View.GONE);
                    ivFail.setVisibility(View.VISIBLE);
                } else if (messageModel.getSendStatus() == MessageModel.Status.success) {
                    progressBar.setVisibility(View.GONE);
                    ivFail.setVisibility(View.GONE);
                }
                ivFail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                        //JobManager.getInstance().addJob(new ReSendMsgJob(messageModel.getMsgId()));
                    }
                });
//TODO
              //  rightAvatar.setOnClickListener(v -> ProfileInfoActivity.intentTo(mContext, messageModel.isMine() ? LoginManager.getInstance().getCurrentUserId() : messageModel.getFromUid()));
            }

        }
    }
}
