package io.dourl.mqtt.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.drakeet.multitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.SessionModel;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/31
 */
public class SessionAdapter extends MultiTypeAdapter {

    private List<SessionModel> mDataList;

    public SessionAdapter(List<SessionModel> mDataList) {
        super(mDataList);
        this.mDataList = mDataList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<SessionModel> messages) {
        if (messages != null && !messages.isEmpty()) {
            mDataList.clear();
            mDataList.addAll(messages);
            notifyDataSetChanged();
        }
    }
}
