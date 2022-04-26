package io.dourl.mqtt.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;

import io.dourl.mqtt.R;
import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.MessagePaging;
import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.event.ChatMsgEvent;
import io.dourl.mqtt.event.MsgStatusUpdateEvent;
import io.dourl.mqtt.manager.EventBusManager;
import io.dourl.mqtt.manager.MessageManager;
import io.dourl.mqtt.model.message.chat.BodyType;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.model.message.emoji.DefaultEmojiconDatas;
import io.dourl.mqtt.model.message.emoji.Emojicon;
import io.dourl.mqtt.model.message.emoji.EmojiconGroupEntity;
import io.dourl.mqtt.storage.DbCallback;
import io.dourl.mqtt.storage.MessageDao;
import io.dourl.mqtt.storage.SessionDao;
import io.dourl.mqtt.storage.SessionManager;
import io.dourl.mqtt.ui.adpater.chat.ChatAdapter;
import io.dourl.mqtt.ui.adpater.chat.ChatTextProvider;
import io.dourl.mqtt.ui.widge.EmojiconMenu;
import io.dourl.mqtt.ui.widge.EmojiconMenuBase;
import io.dourl.mqtt.ui.widge.MultiLineEditText;
import io.dourl.mqtt.ui.widge.RecordStateView;
import io.dourl.mqtt.ui.widge.RecordView;
import io.dourl.mqtt.ui.widge.TitleBar;
import io.dourl.mqtt.utils.AppContextUtil;
import io.dourl.mqtt.utils.ImSmileUtils;
import io.reactivex.functions.Consumer;


/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/8
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener,RecordView.AudioRecordListener {
    private static final String TAG = "ChatActivity";
    RecyclerView mRecyclerView;
    TextView noticeContent;
    ImageView noticeClose;
    RelativeLayout noticeLayout;
    TitleBar mTitleBar;
    TextView msgCountHint;
    TextView msgUnread;
    MultiLineEditText mEditText;
    ImageView mBtnSend;
    ImageButton btnAlbum;
    ImageButton btnPhoto;
    ImageButton btnRecord;
    ImageButton mBtnFace;
    FrameLayout mEmojiMenuContainer;
    LinearLayout inputView;
    ImageView btnKeyboard;
    RecordView recordView;

    FrameLayout bottomView;
    RecordStateView recordStateView;
    RelativeLayout mMainLayout;

    protected String mSessionID = null;
    private UserModel mBaseUser;
    private ChatAdapter mAdapter;
    protected int mUnreadCount = 15;
    private long mLastMsgId;
    protected TextView mMsgUnread;
    protected boolean ctrlPress = false;
    private boolean rightShown = false;

    public static void intentTo(Context context, String sessionId) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("session_id", sessionId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void intentTo(Context context, String sessionId, UserModel user) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("session_id", sessionId);
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EventBusManager.getInstance().register(this);
        init();
        initView();

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
        setupInputBar();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.getItemCount() - 1) {
                    LoggerUtil.d(TAG,"reach top");
//                    mPtrLayout.autoRefresh(true, 200);
                    getMsgsByPage(0);
                }
                if (mMsgUnread.isShown() && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mUnreadCount - 1) {
                    mMsgUnread.setVisibility(View.GONE);
                }
            }
        });
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom - 300) {
                    LoggerUtil.d(TAG,"LayoutChange oldBottom");
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollToBottom();
                        }
                    }, 100);
                }
            }
        });
        initEmoji();

    }
    protected EmojiconMenuBase emojiconMenu;

    private void initEmoji() {
        if (emojiconMenu == null) {
            emojiconMenu = (EmojiconMenu) LayoutInflater.from(this).inflate(R.layout.im_layout_emojicon_menu, null);
            ArrayList<EmojiconGroupEntity> emojiconGroupList = new ArrayList<EmojiconGroupEntity>();
            emojiconGroupList.add(new EmojiconGroupEntity(R.drawable.ee_1, Arrays.asList(DefaultEmojiconDatas.getData())));
            ((EmojiconMenu) emojiconMenu).init(emojiconGroupList);
        }
        mEmojiMenuContainer.addView(emojiconMenu);

        // emojicon menu
        emojiconMenu.setEmojiconMenuListener(new EmojiconMenuBase.EmojiconMenuListener() {

            @Override
            public void onExpressionClicked(Emojicon emojicon) {
                if (emojicon.getType() != Emojicon.Type.BIG_EXPRESSION) {
                    if (emojicon.getEmojiText() != null) {
                        mEditText.append(ImSmileUtils.getSmiledText(ChatActivity.this, emojicon.getEmojiText()));
                    }
                } else {
//                    sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
                }
            }

            @Override
            public void onDeleteImageClicked() {
                if (!TextUtils.isEmpty(mEditText.getText())) {
                    KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                    dispatchKeyEvent(event);
                }
            }

            @Override
            public void onSendClicked() {
                sendTxtMessage();
            }
        });
    }

    protected void toggleEmojicon() {
        if (mEmojiMenuContainer.getVisibility() == View.VISIBLE) {
            mEmojiMenuContainer.setVisibility(View.GONE);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEmojiMenuContainer.setVisibility(View.VISIBLE);
                }
            }, 50);
            hideKeyboard();
        }

    }
    private void setupInputBar() {
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                // test on Mac virtual machine: ctrl map to KEYCODE_UNKNOWN
                if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        ctrlPress = true;
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        ctrlPress = false;
                    }
                }
                return false;
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE ||
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                ctrlPress == true)) {
                    sendTxtMessage();
                    return true;
                }

                return false;
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    toggleEditMode();
                }
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard();
                    if (rightShown)
                        toggleRightLayout();
                }
                return false;
            }
        });
    }

    protected void toggleRightLayout() {
        ObjectAnimator animator = null;
        if (rightShown) {
            animator = ObjectAnimator.ofFloat(mMainLayout, "translationX", -AppContextUtil.dip2px(240), 0f);
        } else {
            animator = ObjectAnimator.ofFloat(mMainLayout, "translationX", 0f, -AppContextUtil.dip2px(240));
        }
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        rightShown = !rightShown;
        hideKeyboard();
    }


    protected void hideKeyboard() {
        final View focusView = getCurrentFocus();
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            focusView.clearFocus();
        }
    }

    private void toggleEditMode() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEmojiMenuContainer.setVisibility(View.GONE);
            }
        }, 50);
    }

    private void sendTxtMessage() {
        String text = mEditText.getText().toString();
        if (text != null && text.length() > 0) {
            MessageManager.getInstance().sendTextMessage(mBaseUser, text);
            mEditText.setText("");
            scrollToBottom();
        } else {
            // sendEmptyTxtMessage();
        }
        if (!mBaseUser.isFriend()) {
            /*HINT_LIMIT--;
            if (HINT_LIMIT == -1) {
                new ChatHintDialog(this, mMainLayout, new ChatHintDialog.OnFunClickListener() {
                    @Override
                    public void onFunClick() {
                        showLoadingProgress();
                        doContactsAction();
                    }
                }).show();
            }*/
        }
    }

    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(0);
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
                          mEditText.setText(model.getDraft());
                    }
                    getMsgsByPage(mUnreadCount);
                    SessionManager.getInstance().resetUnreadCount(mSessionID);
                });


            }

            @Override
            public void onFail(Throwable e) {

            }
        });

    }
    public static final int PRE_PAGE_COUNT = 10; //最少10
    @SuppressLint("CheckResult")
    private void getMsgsByPage(int unread) {
        MessageDao.getMsgsBySessionIdObservable(mSessionID, mLastMsgId, PRE_PAGE_COUNT + unread).subscribe(new Consumer<MessagePaging>() {
            @Override
            public void accept(@NonNull MessagePaging messagePaging) throws Exception {
                if (messagePaging == null) return;
                if (0 == mLastMsgId) {
                    mAdapter.setData(messagePaging.list);
                    scrollToBottom();
//                    mRecyclerView.scrollToPosition(messagePaging.list.size() - 1);
                } else {
                    mAdapter.addPreviousPage(messagePaging.list);
                }
                mLastMsgId = messagePaging.lastMsgDbId;
                //mPtrLayout.setPullToRefresh(messagePaging.hasMore);
            }

        });
    }

    protected void initView() {
        mTitleBar = findViewById(R.id.titleBar);
        mRecyclerView = findViewById(R.id.message_list);
        mMsgUnread = findViewById(R.id.msg_unread);

        mEditText = findViewById(R.id.editText);
        mEmojiMenuContainer = findViewById(R.id.emojicon_menu_container);
        mMainLayout = findViewById(R.id.rootView);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnFace = findViewById(R.id.btnFace);
        mBtnFace.setOnClickListener(this);


        inputView = findViewById(R.id.inputView);

        btnRecord = findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(this);
        recordStateView = findViewById(R.id.recordStateView);
        recordView = findViewById(R.id.recordView);
        recordView.setSessionId(mSessionID);
        recordView.setRecordStateView(recordStateView);
        recordView.setAudioRecordListener(this);

        btnKeyboard = findViewById(R.id.btnKeyboard);
    }

    protected void init() {
        Intent intent = getIntent();
        if (intent != null) {
            mSessionID = intent.getExtras().getString("session_id");
            mBaseUser = intent.getExtras().getParcelable("user");
        }
        if (mSessionID == null || mBaseUser == null) {
            finish();
            return;
        }
    }

    private void getChatUserInfo() {
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        if (id == R.id.btn_send) {
            sendTxtMessage();
        }else if (id == R.id.btnFace){
            toggleEmojicon();
        }else if (id == R.id.btnRecord){
            checkPermission();
            showRecordButton(true);
            hideKeyboard();
        }else if (id == R.id.btnKeyboard){
            showRecordButton(false);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgStatusUpdateEvent event) {
        LoggerUtil.d(TAG, "MsgStatusUpdateEvent: %s", event);
        if (event.getMessage().getSessionId().equalsIgnoreCase(String.valueOf(mSessionID))) {
            mAdapter.updateData(event.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChatMsgEvent event) {
        LoggerUtil.d(TAG,"ChatMsgEvent: %s", event);
        if (event.getSessionId().equalsIgnoreCase(String.valueOf(mSessionID))) {
            if (event.getMessage().getBodyType() == BodyType.TYPE_GROUP_APPLY_NUM) {
                try {
                   // showCountHint(Integer.parseInt(event.getMessage().getContentDesc().toString()));
                } catch (Exception e) {
                }
            } else {
                if (mAdapter.getDataList().contains(event.getMessage())) {
                    mAdapter.updateData(event.getMessage());
                } else {
                    mAdapter.addData(event.getMessage());
                    if (event.getMessage().isMine() || isBottom()) {
                        scrollToBottom();
                    }
                }
            }

        }
    }

    protected boolean isBottom() {
        if (mRecyclerView == null) return false;
        if (mRecyclerView.computeVerticalScrollExtent() + mRecyclerView.computeVerticalScrollOffset()
                >= mRecyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void showRecordButton(boolean isShow) {
        recordView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        inputView.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusManager.getInstance().unRegister(this);
    }

    @Override
    public void recordFinished(String path, int time) {
        MessageManager.getInstance().sendAudioMessage(mBaseUser, path, time);
    }
}
