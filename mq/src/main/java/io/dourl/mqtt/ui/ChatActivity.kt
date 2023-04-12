package io.dourl.mqtt.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dourl.mqtt.R
import io.dourl.mqtt.bean.MessageModel
import io.dourl.mqtt.bean.SessionModel
import io.dourl.mqtt.bean.UserModel
import io.dourl.mqtt.constants.Constants
import io.dourl.mqtt.event.ChatMsgEvent
import io.dourl.mqtt.event.MsgStatusUpdateEvent
import io.dourl.mqtt.manager.EventBusManager
import io.dourl.mqtt.manager.MessageManager
import io.dourl.mqtt.model.message.chat.*
import io.dourl.mqtt.model.message.emoji.DefaultEmojiconDatas
import io.dourl.mqtt.model.message.emoji.Emojicon
import io.dourl.mqtt.model.message.emoji.EmojiconGroupEntity
import io.dourl.mqtt.storage.DbCallback
import io.dourl.mqtt.storage.MessageDao
import io.dourl.mqtt.storage.SessionDao
import io.dourl.mqtt.storage.SessionManager
import io.dourl.mqtt.ui.adpater.chat.*
import io.dourl.mqtt.ui.widge.*
import io.dourl.mqtt.ui.widge.EmojiconMenuBase.EmojiconMenuListener
import io.dourl.mqtt.ui.widge.RecordView.AudioRecordListener
import io.dourl.mqtt.utils.AndroidUtils
import io.dourl.mqtt.utils.AppContextUtil
import io.dourl.mqtt.utils.ImSmileUtils
import io.dourl.mqtt.utils.MediaFilesUtils
import io.dourl.mqtt.utils.log.LoggerUtil
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.*

/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/8
 */
open class ChatActivity : BaseActivity(), View.OnClickListener, AudioRecordListener {
    var mRecyclerView: RecyclerView? = null
    var noticeContent: TextView? = null
    var noticeClose: ImageView? = null
    var noticeLayout: RelativeLayout? = null
    var mTitleBar: TitleBar? = null
    var msgCountHint: TextView? = null
    var msgUnread: TextView? = null

    @JvmField
    var mEditText: MultiLineEditText? = null
    lateinit var mBtnSend: ImageView
    lateinit var btnAlbum: ImageButton
    var btnPhoto: ImageButton? = null
    var btnRecord: ImageButton? = null
    var mBtnFace: ImageButton? = null
    var mEmojiMenuContainer: FrameLayout? = null
    var inputView: LinearLayout? = null
    var btnKeyboard: ImageView? = null
    var recordView: RecordView? = null
    var bottomView: FrameLayout? = null
    var recordStateView: RecordStateView? = null
    var mMainLayout: RelativeLayout? = null

    @JvmField
    protected var mSessionID: String? = null
    private var mBaseUser: UserModel? = null
    protected var mAdapter: ChatAdapter? = null
    protected var mUnreadCount = 15
    private var mLastMsgId: Long = 0
    protected var mMsgUnread: TextView? = null
    protected var ctrlPress = false
    private var rightShown = false
    protected var mSendFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        EventBusManager.getInstance().register(this)
        init()
        initView()
        mTitleBar!!.showLine()
        mTitleBar!!.setLeftBtnListener { finish() }
        mTitleBar!!.setTitle(mSessionID)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isAutoMeasureEnabled = true
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = true
        mRecyclerView!!.layoutManager = linearLayoutManager
        mRecyclerView!!.setHasFixedSize(false)
        mAdapter = ChatAdapter(ArrayList())
        mAdapter?.register(MessageModel::class)?.to(
            ChatHintBinder(),
            ChatTextBinder(),
            ChatImageBinder(),
            ChatAudioBinder(),
            ChatVideoBinder()
        )?.withKotlinClassLinker { _, item ->
            when (item.body) {
                is HintBody -> ChatHintBinder::class
                is TextBody -> ChatTextBinder::class
                is ImageBody -> ChatImageBinder::class
                is AudioBody -> ChatAudioBinder::class
                else -> ChatVideoBinder::class
            }
        }
        mRecyclerView!!.adapter = mAdapter
        setupMessageList()
        setupInputBar()
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.itemCount - 1) {
                    LoggerUtil.d(TAG, "reach top")
                    //                    mPtrLayout.autoRefresh(true, 200);
                    getMsgsByPage(0)
                }
                if (mMsgUnread!!.isShown && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mUnreadCount - 1) {
                    mMsgUnread!!.visibility = View.GONE
                }
            }
        })
        mRecyclerView!!.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom - 300) {
                LoggerUtil.d(TAG, "LayoutChange oldBottom")
                mRecyclerView!!.postDelayed({ scrollToBottom() }, 100)
            }
        }
        initEmoji()
    }

    protected var emojiconMenu: EmojiconMenuBase? = null
    private fun initEmoji() {
        if (emojiconMenu == null) {
            emojiconMenu = LayoutInflater.from(this)
                .inflate(R.layout.im_layout_emojicon_menu, null) as EmojiconMenu
            val emojiconGroupList = ArrayList<EmojiconGroupEntity>()
            emojiconGroupList.add(
                EmojiconGroupEntity(
                    R.drawable.ee_1,
                    Arrays.asList(*DefaultEmojiconDatas.getData())
                )
            )
            (emojiconMenu as EmojiconMenu?)!!.init(emojiconGroupList)
        }
        mEmojiMenuContainer!!.addView(emojiconMenu)

        // emojicon menu
        emojiconMenu!!.setEmojiconMenuListener(object : EmojiconMenuListener {
            override fun onExpressionClicked(emojicon: Emojicon) {
                if (emojicon.type != Emojicon.Type.BIG_EXPRESSION) {
                    if (emojicon.emojiText != null) {
                        mEditText!!.append(
                            ImSmileUtils.getSmiledText(
                                this@ChatActivity,
                                emojicon.emojiText
                            )
                        )
                    }
                } else {
//                    sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
                }
            }

            override fun onDeleteImageClicked() {
                if (!TextUtils.isEmpty(mEditText!!.text)) {
                    val event = KeyEvent(
                        0,
                        0,
                        0,
                        KeyEvent.KEYCODE_DEL,
                        0,
                        0,
                        0,
                        0,
                        KeyEvent.KEYCODE_ENDCALL
                    )
                    dispatchKeyEvent(event)
                }
            }

            override fun onSendClicked() {
                sendTxtMessage()
            }
        })
    }

    protected fun toggleEmojicon() {
        if (mEmojiMenuContainer!!.visibility == View.VISIBLE) {
            mEmojiMenuContainer!!.visibility = View.GONE
        } else {
            sHandler.postDelayed({ mEmojiMenuContainer!!.visibility = View.VISIBLE }, 50)
            hideKeyboard()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInputBar() {
        mEditText!!.setOnKeyListener { v, keyCode, event -> // test on Mac virtual machine: ctrl map to KEYCODE_UNKNOWN
            if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    ctrlPress = true
                } else if (event.action == KeyEvent.ACTION_UP) {
                    ctrlPress = false
                }
            }
            false
        }
        mEditText!!.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE ||
                event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN && ctrlPress == true
            ) {
                sendTxtMessage()
                return@OnEditorActionListener true
            }
            false
        })
        mEditText!!.onFocusChangeListener = OnFocusChangeListener { view, b ->
            if (b) {
                toggleEditMode()
            }
        }
        mRecyclerView!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                hideKeyboard()
                if (rightShown) toggleRightLayout()
            }
            false
        }
    }

    protected fun toggleRightLayout() {
        var animator: ObjectAnimator? = null
        animator = if (rightShown) {
            ObjectAnimator.ofFloat(
                mMainLayout,
                "translationX",
                -AppContextUtil.dip2px(240f).toFloat(),
                0f
            )
        } else {
            ObjectAnimator.ofFloat(
                mMainLayout,
                "translationX",
                0f,
                -AppContextUtil.dip2px(240f).toFloat()
            )
        }
        animator.duration = 500
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
        rightShown = !rightShown
        hideKeyboard()
    }

    protected fun hideKeyboard() {
        val focusView = currentFocus
        if (focusView != null) {
            val imm = focusView.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(focusView.windowToken, 0)
            focusView.clearFocus()
        }
    }

    private fun toggleEditMode() {
        sHandler.postDelayed({ mEmojiMenuContainer!!.visibility = View.GONE }, 50)
    }

    protected open fun sendTxtMessage() {
        val text = mEditText!!.text.toString()
        if (text.length > 0) {
            MessageManager.getInstance().sendTextMessage(mBaseUser, text)
            mEditText!!.setText("")
            scrollToBottom()
        } else {
            // sendEmptyTxtMessage();
        }
        if (!mBaseUser!!.isFriend) {
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
    protected open fun sendImageMessage(path: String?) {
        MessageManager.getInstance().sendImageMessage(mBaseUser, path)
    }

    protected fun scrollToBottom() {
        mRecyclerView!!.scrollToPosition(0)
    }

    private fun setupMessageList() {
        SessionDao.findSessionById(mSessionID, object : DbCallback<SessionModel?> {
            override fun onSuccess(model: SessionModel?) {
                runOnUiThread {
                    if (model != null) {
                        mUnreadCount = model.unreadMsgCount
                        if (mUnreadCount > 15) {
                            mMsgUnread!!.visibility = View.VISIBLE
                            mMsgUnread!!.text =
                                getString(R.string.hint_chat_unread_msg, mUnreadCount)
                        }
                        mEditText!!.setText(model.draft)
                    }
                    getMsgsByPage(mUnreadCount)
                    SessionManager.getInstance().resetUnreadCount(mSessionID)
                }
            }

            override fun onFail(e: Throwable) {}

        })
    }

    @SuppressLint("CheckResult")
    private fun getMsgsByPage(unread: Int) {
        MessageDao.getMsgsBySessionIdObservable(mSessionID, mLastMsgId, PRE_PAGE_COUNT + unread)
            .subscribe(
                Consumer { messagePaging ->
                    if (messagePaging == null) return@Consumer
                    if (0L == mLastMsgId) {
                        mAdapter!!.setData(messagePaging.list)
                        scrollToBottom()
                        //                    mRecyclerView.scrollToPosition(messagePaging.list.size() - 1);
                    } else {
                        mAdapter!!.addPreviousPage(messagePaging.list)
                    }
                    mLastMsgId = messagePaging.lastMsgDbId
                    //mPtrLayout.setPullToRefresh(messagePaging.hasMore);
                })
    }

    protected fun initView() {
        mTitleBar = findViewById(R.id.titleBar)
        mRecyclerView = findViewById(R.id.message_list)
        mMsgUnread = findViewById(R.id.msg_unread)
        mEditText = findViewById(R.id.editText)
        mEmojiMenuContainer = findViewById(R.id.emojicon_menu_container)
        mMainLayout = findViewById(R.id.rootView)
        mBtnSend = findViewById(R.id.btn_send)
        mBtnSend?.setOnClickListener(this)
        mBtnFace = findViewById(R.id.btnFace)
        mBtnFace?.setOnClickListener(this)
        inputView = findViewById(R.id.inputView)
        btnRecord = findViewById(R.id.btnRecord)
        btnRecord?.setOnClickListener(this)
        recordStateView = findViewById(R.id.recordStateView)
        recordView = findViewById(R.id.recordView)
        recordView?.setSessionId(mSessionID)
        recordView?.setRecordStateView(recordStateView)
        recordView?.setAudioRecordListener(this)
        btnKeyboard = findViewById(R.id.btnKeyboard)
        btnAlbum = findViewById(R.id.btnAlbum)
        btnAlbum.setOnClickListener(this)
    }

    protected open fun init() {
        val intent = intent
        if (intent != null) {
            mSessionID = intent.extras!!.getString("session_id")
            mBaseUser = intent.extras!!.getParcelable("user")
        }
        if (mSessionID == null || mBaseUser == null) {
            finish()
            return
        }
    }

    private val chatUserInfo: Unit
        private get() {}

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_send) {
            sendTxtMessage()
        } else if (id == R.id.btnFace) {
            toggleEmojicon()
        } else if (id == R.id.btnRecord) {
            checkPermission()
            showRecordButton(true)
            hideKeyboard()
        } else if (id == R.id.btnKeyboard) {
            showRecordButton(false)
        } else if (id == R.id.btnAlbum){
            choosePhoto();
        }
    }

    private fun choosePhoto() {
        mSendFile = MediaFilesUtils.getImageFile(this);
        AndroidUtils.openAlbum(this, mSendFile, false, 0, 0, Constants.REQUEST_CODE_OPEN_ALBUM, Constants.FILE_PROVIDER);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: MsgStatusUpdateEvent) {
        LoggerUtil.d(TAG, "MsgStatusUpdateEvent: %s", event)
        if (event.message.sessionId.equals(mSessionID.toString(), ignoreCase = true)) {
            mAdapter!!.updateData(event.message)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ChatMsgEvent) {
        LoggerUtil.d(TAG, "ChatMsgEvent: %s", event)
        if (event.sessionId.equals(mSessionID.toString(), ignoreCase = true)) {
            if (event.message.bodyType == BodyType.TYPE_GROUP_APPLY_NUM) {
                try {
                    // showCountHint(Integer.parseInt(event.getMessage().getContentDesc().toString()));
                } catch (e: Exception) {
                }
            } else {
                if (mAdapter!!.dataList.contains(event.message)) {
                    mAdapter!!.updateData(event.message)
                } else {
                    mAdapter!!.addData(event.message)
                    if (event.message.isMine() || isBottom) {
                        scrollToBottom()
                    }
                }
            }
        }
    }

    protected val isBottom: Boolean
        protected get() {
            if (mRecyclerView == null) return false
            return if (mRecyclerView!!.computeVerticalScrollExtent() + mRecyclerView!!.computeVerticalScrollOffset()
                >= mRecyclerView!!.computeVerticalScrollRange()
            ) true else false
        }

    private fun showRecordButton(isShow: Boolean) {
        recordView!!.visibility = if (isShow) View.VISIBLE else View.GONE
        inputView!!.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusManager.getInstance().unRegister(this)
    }

    override fun recordFinished(path: String, time: Int) {
        MessageManager.getInstance().sendAudioMessage(mBaseUser, path, time)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_OPEN_ALBUM) {
            if (mSendFile != null) {
             val filePath =   AndroidUtils.parseActivityMediaResult(this, mSendFile, requestCode, resultCode, data, Constants.REQUEST_CODE_TAKE_PHOTO, Constants.REQUEST_CODE_OPEN_ALBUM);
                if (filePath != null) {
                    sendImageMessage(filePath);
                }
            }
        }
    }



    companion object {
        private const val TAG = "ChatActivity"
        fun intentTo(context: Context, sessionId: String?) {
            val intent = Intent(context, ChatActivity::class.java)
            val bundle = Bundle()
            bundle.putString("session_id", sessionId)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        @JvmStatic
        fun intentTo(context: Context, sessionId: String?, user: UserModel?) {
            val intent = Intent(context, ChatActivity::class.java)
            val bundle = Bundle()
            bundle.putString("session_id", sessionId)
            bundle.putParcelable("user", user)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        const val PRE_PAGE_COUNT = 10 //最少10
    }
}