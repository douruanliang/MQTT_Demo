package io.dourl.mqtt.ui.adpater.chat;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.model.BaseUser;
import io.dourl.mqtt.model.customenum.ManagerType;
import io.dourl.mqtt.model.message.chat.AudioBody;
import io.dourl.mqtt.model.message.chat.BodyType;
import io.dourl.mqtt.model.message.chat.ImageBody;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by SpiritTalk on 17/1/13.
 */

public class ChatAdapter extends MultiTypeAdapter {
    /**
     * 消息间隔时间为 60 秒
     */
    private static final int INTERVAL_TIME = 60 * 1000;
    private List<MessageModel> mDataList;

    public OnItemActionListener mOnItemActionListener;
    public HashMap<String, BaseUser> mClanMember = new HashMap<>();
    public ManagerType mMyManagrType = ManagerType.normal;

    public ChatAdapter(List<MessageModel> list) {
        super(list);
        mDataList = list;
    }

    /**
     * 判断当前消息是否需要显示时间
     *
     * @param position
     */
    private void checkIsShowTime(int position) {
        // 超出长度，直接返回
        if (position >= mDataList.size()) return;

        MessageModel curMessage = mDataList.get(position);
        // 最后一个，必须显示
        if (position == mDataList.size() - 1) {
            curMessage.setShowTime(true);
            return;
        } else {
            curMessage.setShowTime(false);
        }

        MessageModel preMessage = mDataList.get(position + 1);
        if (Math.abs(curMessage.getTime() - preMessage.getTime()) > INTERVAL_TIME) {
            curMessage.setShowTime(true);
        } else {
            curMessage.setShowTime(false);
        }

    }

    public void setClanMember(HashMap<String, BaseUser> clanMember) {
        this.mClanMember.clear();
        this.mClanMember = clanMember;
    }

    public void setMyManagrType(ManagerType myManagrType) {
        mMyManagrType = myManagrType;
    }


    public void setData(List<MessageModel> messages) {
        if (messages != null && !messages.isEmpty()) {
            mDataList.clear();
            mDataList.addAll(messages);
            notifyDataSetChanged();
        }
    }

    public void addPreviousPage(List<MessageModel> messages) {
        if (messages != null && !messages.isEmpty()) {
            int positionStart = mDataList.size();
            mDataList.addAll(messages);
            notifyItemRangeInserted(positionStart, messages.size());
        }
    }

    public void addData(MessageModel message) {
        mDataList.add(0, message);
        notifyItemInserted(0);
    }

    public void removeData(MessageModel message) {
        int index = mDataList.indexOf(message);
        mDataList.remove(message);
        notifyItemRemoved(index);
    }

    public void updateData(MessageModel message) {
        for (int i = mDataList.size() - 1; i >= 0; i--) {
            MessageModel m = mDataList.get(i);
            if (m.equals(message)) {
                m.setDownloading(message.isDownloading());
                m.setSendStatus(message.getSendStatus());
                if (m.getBodyType() == BodyType.TYPE_AUDIO) {
                    ((AudioBody) m.getBody()).setPlaying(((AudioBody) message.getBody()).isPlaying());
                } else if (m.getBodyType() == BodyType.TYPE_RED_PACKET) {
                    m.setBody(message.getBody());
                }
                notifyItemChanged(i);
                LoggerUtil.d("update msg at: " + i);
                return;
            }
        }
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        mOnItemActionListener = onItemActionListener;
    }


    public List<MessageModel> getDataList() {
        return mDataList;
    }

    /**
     * 获取当前会话的图片
     *
     * @return
     */
    public ArrayList<String> getImageDataList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = mDataList.size() - 1; i >= 0; i--) {
            MessageModel model = mDataList.get(i);
            if (model.getBody() instanceof ImageBody) {
                String local = model.getLocalPath();
               /* if (FileUtils.isFileExist(local)) {
                    list.add(local);
                } else {
                    list.add(model.getBody().getServerPath());
                }*/
            }
        }
        return list;
    }

    public OnItemActionListener getOnItemActionListener() {
        return mOnItemActionListener;
    }

    public interface OnItemActionListener {

        /**
         * 复制
         *
         * @param message
         */
        void copy(MessageModel message);

        /**
         * 复制
         *
         * @param message
         */
        void save(MessageModel message);

        /**
         * 删除
         *
         * @param message
         */
        void delete(MessageModel message);

        /**
         * 转发
         *
         * @param message
         */
        void forward(MessageModel message);

        /**
         * 撤回
         */
        void withdraw(MessageModel message);

        /**
         * @
         */
        void at(MessageModel message);

    }

}
