package io.dourl.mqtt.ui.widge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.RequiresApi;
import io.dourl.mqtt.R;
import io.dourl.mqtt.utils.AudioController;
import io.dourl.mqtt.utils.CountUpTimer;
import io.dourl.mqtt.utils.MediaFilesUtils;


/**
 * Created by SpiritTalk on 17/1/18.
 */

public class RecordView extends RelativeLayout {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_RECORDING = 1;
    private static final int STATE_WANT_CANCEL = 2;
    private static final int AUDIO_VOICE_CHANGE = 3;
    private final int UPDATE_VOICE_LEVEL = 100;
    private final int DISTANCE_Y_CANCEL = 200;
    private Context mContext;
    private String sessionId;
    private int mState = STATE_NORMAL;
    private boolean isLongClick;
    private boolean isRecording;
    private int mRecordDuration;
    private CountUpTimer mCountUpTimer;
    private AudioRecordListener mListener;
    //    private ChatRecordDialog mChatRecordDialog;
    private RecordStateView mRecordStateView;
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AUDIO_VOICE_CHANGE) {
                mRecordStateView.setVoiceLevel(AudioController.getInstance().getVoiceLevel(7));
                mHandler.sendEmptyMessageDelayed(AUDIO_VOICE_CHANGE, UPDATE_VOICE_LEVEL);
            }
        }
    };

    public RecordView(Context context) {
        super(context);
        init(context);
    }

    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mCountUpTimer = new CountUpTimer(0, 100) {
            @Override
            public void onTick(long millisFromStart) {
                if (millisFromStart % 1000 > 0) {
                    mRecordDuration = (int) (millisFromStart / 1000);
                }
            }

            @Override
            public void onFinish() {
            }
        };
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongClick = true;
                AudioController.getInstance().startRecord(MediaFilesUtils.getSessionAudioFile(getContext(), sessionId).getAbsolutePath());
                mHandler.sendEmptyMessage(AUDIO_VOICE_CHANGE);
                isRecording = true;
                mCountUpTimer.start();
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeButtonState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    if (isWantToCancel(x, y)) {
                        changeButtonState(STATE_WANT_CANCEL);
                    } else {
                        changeButtonState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isLongClick) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mRecordDuration < 1) {
                    AudioController.getInstance().cancelRecord();
                } else if (mState == STATE_RECORDING) {
                    String path = AudioController.getInstance().stopRecording();
                    if (mListener != null) {
                        mListener.recordFinished(path, mRecordDuration);
                    }
                } else if (mState == STATE_WANT_CANCEL) {
                    AudioController.getInstance().cancelRecord();
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void reset() {
        isLongClick = false;
        isRecording = false;
        mRecordDuration = 0;
        mHandler.removeMessages(AUDIO_VOICE_CHANGE);
        changeButtonState(STATE_NORMAL);
    }

    private void changeButtonState(int state) {
        if (mState != state) {
            mState = state;
            switch (state) {
                case STATE_NORMAL:
                    mRecordStateView.setVisibility(GONE);
                    mRecordStateView.setStateText(R.string.record_state_recording);
//                    setText(mContext.getString(R.string.record_state_normal));
                    break;
                case STATE_RECORDING:
//                    setText(mContext.getString(R.string.record_state_recording));
                    if (mRecordStateView.getVisibility() == VISIBLE) {
                        mRecordStateView.setStateText(R.string.record_state_recording);
                    } else {
                        mRecordStateView.setVisibility(VISIBLE);
                    }
                    break;
                case STATE_WANT_CANCEL:
                    mRecordStateView.setStateText(R.string.record_state_cancel);
//                    setText(mContext.getString(R.string.record_state_cancel));
                    break;
            }
        }
    }

    private boolean isWantToCancel(int x, int y) {
//        NHLog.d(String.format("Moved to [%d, %d]", x, y));
        if (x < getLeft() || x > getRight()) {
            return true;
        }
        return y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAudioRecordListener(AudioRecordListener listener) {
        mListener = listener;
    }

    public void setRecordStateView(RecordStateView recordStateView) {
        mRecordStateView = recordStateView;
    }

    public interface AudioRecordListener {
        void recordFinished(String path, int time);
    }
}
