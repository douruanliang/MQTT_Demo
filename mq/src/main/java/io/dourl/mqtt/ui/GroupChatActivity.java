package io.dourl.mqtt.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.manager.MessageManager;
import io.dourl.mqtt.model.BaseUser;
import io.dourl.mqtt.model.ClanModel;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/4/28
 */
public class GroupChatActivity extends ChatActivity {

    protected ClanModel mClanModel;
    private List<BaseUser> mAtUsers = new ArrayList<>();

    public static void intentTo(Context context, String sessionId, ClanModel clan) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("session_id", sessionId);
        bundle.putParcelable("clan", clan);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    protected void init() {

        Intent intent = getIntent();
        if (intent != null) {
            mSessionID = intent.getExtras().getString("session_id");
            mClanModel = intent.getExtras().getParcelable("clan");
        }
        if (mSessionID == null) {
            finish();
            return;
        }

        LoggerUtil.d("group", mSessionID);
    }

    @Override
    protected void sendTxtMessage() {
        if (mClanModel == null) return;
        String text = mEditText.getText().toString();
        if (text != null && text.length() > 0) {
            MessageManager.getInstance().sendTextMessage(mClanModel, text, mAtUsers);
            mEditText.setText("");
            mAtUsers.clear();
            scrollToBottom();
        } else {
            //sendEmptyTxtMessage();
        }
    }

    /*@Override
    protected void sendEmptyTxtMessage() {
        if (mClanModel == null) return;
        MessageManager.getInstance().sendTextEmptyMessage(mClanModel);
    }*/
}
