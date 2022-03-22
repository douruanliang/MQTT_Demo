package io.dourl.mqtt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.dourl.mqtt.R;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.storage.DbCallback;
import io.dourl.mqtt.storage.SessionDao;
import io.dourl.mqtt.storage.SessionManager;
import io.dourl.mqtt.ui.adpater.chat.ChatAdapter;
import io.dourl.mqtt.ui.adpater.chat.ChatTextProvider;
import io.dourl.mqtt.ui.widge.MultiLineEditText;
import io.dourl.mqtt.ui.widge.RecordStateView;
import io.dourl.mqtt.ui.widge.RecordView;
import io.dourl.mqtt.ui.widge.TitleBar;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/8
 */
public class ChatActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    TextView noticeContent;
    ImageView noticeClose;
    RelativeLayout noticeLayout;
    TitleBar mTitleBar;
    TextView msgCountHint;
    TextView msgUnread;
    MultiLineEditText editText;
    ImageView btnSend;
    ImageButton btnAlbum;
    ImageButton btnPhoto;
    ImageButton btnRecord;
    ImageButton btnFace;
    FrameLayout emojiconMenuContainer;
    LinearLayout inputView;
    ImageView btnKeyboard;
    RecordView recordView;

    FrameLayout bottomView;
    RecordStateView recordStateView;
    RelativeLayout rootView;

    protected String mSessionID = null;
    private UserModel mBaseUser;
    private MultiTypeAdapter mAdapter;
    protected int mUnreadCount = 15;
    protected TextView mMsgUnread;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        init();
        mTitleBar.showLine();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new ChatAdapter(new ArrayList<MessageModel>());
        mAdapter.register(TextBody.class, new ChatTextProvider());
        mRecyclerView.setAdapter(mAdapter);
        setupMessageList();
    }

    private void setupMessageList() {
        SessionDao.findSessionById(mSessionID, new DbCallback<SessionModel>() {
            @Override
            public void onSuccess(SessionModel model) {

                runOnUiThread(() -> {
                    if (model != null) {
                        mUnreadCount = model.getUnreadMsgCount();
                        if (mUnreadCount > 15) {
                            mMsgUnread.setVisibility(View.VISIBLE);
                            mMsgUnread.setText(getString(R.string.hint_chat_unread_msg, mUnreadCount));
                        }
                      //  mEditText.setText(model.getDraft());
                    }
                    //getMsgsByPage(mUnreadCount);
                    SessionManager.getInstance().resetUnreadCount(mSessionID);
                });


            }

            @Override
            public void onFail(Throwable e) {

            }
        });

    }

    protected void initView() {
        mTitleBar = findViewById(R.id.titleBar);
        mRecyclerView = findViewById(R.id.message_list);
        mMsgUnread = findViewById(R.id.msg_unread);
    }

    protected void init() {
        Intent intent = getIntent();
        String mSessionID;
        if (intent != null) {
            mSessionID = intent.getExtras().getString("session_id");
            mBaseUser = intent.getExtras().getParcelable("user");
        }
        if (mSessionID == null || mBaseUser == null) {
            finish();
            return;
        }
        mTitleBar.setTitle(mBaseUser.getName());
        getChatUserInfo();
    }

    private void getChatUserInfo() {
    }
}
