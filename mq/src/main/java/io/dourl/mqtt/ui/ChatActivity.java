package io.dourl.mqtt.ui;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dourl.mqtt.R;
import io.dourl.mqtt.R2;
import io.dourl.mqtt.ui.widge.MultiLineEditText;
import io.dourl.mqtt.ui.widge.RecordStateView;
import io.dourl.mqtt.ui.widge.RecordView;
import io.dourl.mqtt.ui.widge.TitleBar;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/8
 */
public class ChatActivity extends BaseActivity {
    @BindView(R2.id.message_list)
    RecyclerView messageList;
    @BindView(R2.id.notice_content)
    TextView noticeContent;
    @BindView(R2.id.notice_close)
    ImageView noticeClose;
    @BindView(R2.id.notice_layout)
    RelativeLayout noticeLayout;
    @BindView(R2.id.titleBar)
    TitleBar titleBar;
    @BindView(R2.id.msg_count_hint)
    TextView msgCountHint;
    @BindView(R2.id.msg_unread)
    TextView msgUnread;
    @BindView(R2.id.editText)
    MultiLineEditText editText;
    @BindView(R2.id.btn_send)
    ImageView btnSend;
    @BindView(R2.id.btnAlbum)
    ImageButton btnAlbum;
    @BindView(R2.id.btnPhoto)
    ImageButton btnPhoto;
    @BindView(R2.id.btnRecord)
    ImageButton btnRecord;
    @BindView(R2.id.btnFace)
    ImageButton btnFace;
    @BindView(R2.id.emojicon_menu_container)
    FrameLayout emojiconMenuContainer;
    @BindView(R2.id.inputView)
    LinearLayout inputView;
    @BindView(R2.id.btnKeyboard)
    ImageView btnKeyboard;
    @BindView(R2.id.recordView)
    RecordView recordView;
    @BindView(R2.id.bottomView)
    FrameLayout bottomView;
    @BindView(R2.id.recordStateView)
    RecordStateView recordStateView;
    @BindView(R2.id.rootView)
    RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
    }
}
