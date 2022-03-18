package io.dourl.mqtt.ui;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.dourl.mqtt.R;
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
    RecyclerView messageList;
    TextView noticeContent;
    ImageView noticeClose;
    RelativeLayout noticeLayout;

    TitleBar titleBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
