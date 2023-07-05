package io.dourl.mqtt.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.text.TextUtils;


import java.io.File;
import java.io.IOException;

import io.dourl.mqtt.utils.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.event.AudioPlayStatusEvent;
import io.dourl.mqtt.manager.EventBusManager;
import io.dourl.mqtt.model.message.chat.AudioBody;


/**
 * 语音播放管理类
 */
public class AudioController implements AudioManager.OnAudioFocusChangeListener {
    private static final int SAMPLE_RATE_IN_HZ = 8000;
    private static AudioController ourInstance = new AudioController();

    private MediaPlayer mMediaPlayer;

    private MediaRecorder mMediaRecorder;

    private String mAudioPath;

    /**
     * 当前正在播放的音频
     */
    private MessageModel mAudioMessage;
    private MediaPlayer.OnCompletionListener mCompletionListener;


    private AudioController() {
        initMediaPlayer();
    }

    public static AudioController getInstance() {
        return ourInstance;
    }

    /**
     * 播放音频
     *
     * @param audioMessage
     */
    public void playAudio(final MessageModel audioMessage) {
        playAudio(audioMessage, null, null);
    }

    /**
     * 播放 audio
     *
     * @param audioMessage
     * @param preparedListener
     * @param completionListener
     */
    public void playAudio(final MessageModel audioMessage,
                          final MediaPlayer.OnPreparedListener preparedListener,
                          final MediaPlayer.OnCompletionListener completionListener) {
        // 停止播放其它 Audio
        if (mMediaPlayer != null && mMediaPlayer.isPlaying() && mAudioMessage != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            ((AudioBody) mAudioMessage.getBody()).setPlaying(false);
            if (mCompletionListener != null) {
                mCompletionListener.onCompletion(mMediaPlayer);
            } else {
                EventBusManager.getInstance().post(new AudioPlayStatusEvent(mAudioMessage));
            }
            mAudioMessage = null;
        }
        mAudioMessage = audioMessage;
        mCompletionListener = completionListener;
        final AudioBody audioBody = (AudioBody) audioMessage.getBody();
        initMediaPlayer();
        try {
            mMediaPlayer.setDataSource(audioBody.getLocalOrServerPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    LoggerUtil.d("start play audio for duration: %dms", mp.getDuration());
                    if (!audioMessage.isRead()) {
                        audioMessage.setIsRead(true);
                        //MessageManager.getInstance().setMsgRead(audioMessage);
                    }
                    audioBody.setPlaying(true);
                    if (preparedListener != null) {
                        preparedListener.onPrepared(mp);
                    } else {
                        EventBusManager.getInstance().post(new AudioPlayStatusEvent(audioMessage));
                    }
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.reset();
                    LoggerUtil.d("play completion");
                    mAudioMessage = null;
                    audioBody.setPlaying(false);
                    if (completionListener != null) {
                        completionListener.onCompletion(mp);
                    } else {
                        EventBusManager.getInstance().post(new AudioPlayStatusEvent(audioMessage));
                    }
                    mCompletionListener = null;
                }
            });
            mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    LoggerUtil.d("playInfo: what: %d, extra: %d", what, extra);
                    return false;
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.reset();
                    LoggerUtil.e("play but error happen, what: %d extra: %d! ", what, extra);
                    mAudioMessage = null;
                    audioBody.setPlaying(false);
                    if (completionListener != null) {
                        completionListener.onCompletion(mMediaPlayer);
                    } else {
                        EventBusManager.getInstance().post(new AudioPlayStatusEvent(audioMessage));
                    }
                    mCompletionListener = null;
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            mMediaPlayer.release();
            mAudioMessage = null;
            mMediaPlayer = null;
            mCompletionListener = null;
        }
    }

    /**
     * 停止播放音频
     */
    public void stopPlaying() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCompletionListener = null;
        }
        if (mAudioMessage != null) {
            ((AudioBody) mAudioMessage.getBody()).setPlaying(false);
            EventBusManager.getInstance().post(new AudioPlayStatusEvent(mAudioMessage));
        }
    }


    public void startRecord(String path) {
        mAudioPath = path;
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        } else {
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
            }
            mMediaRecorder.reset();
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
        mMediaRecorder.setOutputFile(mAudioPath);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            LoggerUtil.e("prepare fail");
        }
        mMediaRecorder.start();
    }

    public String stopRecording() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
        }
        mMediaRecorder = null;
        return mAudioPath;
    }

    public void cancelRecord() {
        stopRecording();
        if (!TextUtils.isEmpty(mAudioPath)) {
            File file = new File(mAudioPath);
            file.delete();
            mAudioPath = null;
        }
    }

    /**
     * 获取音量级别
     *
     * @param maxLevel
     * @return 1～maxLevel
     */
    public int getVoiceLevel(int maxLevel) {
        if (mMediaRecorder != null) {
            return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
        }
        return 1;
    }

    /**
     * 获取录音音量
     *
     * @return 0~32767
     */
    public int getMaxAmplitude() {
        if (mMediaRecorder != null) {
            return mMediaRecorder.getMaxAmplitude();
        }
        return 0;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mMediaPlayer == null) initMediaPlayer();
                else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private void initMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
    }

    private void initMediaRecorder() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
    }

}
