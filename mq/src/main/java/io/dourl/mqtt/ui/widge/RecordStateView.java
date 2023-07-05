package io.dourl.mqtt.ui.widge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import io.dourl.mqtt.R;
import io.dourl.mqtt.utils.log.LoggerUtil;


/**
 * Created by SpiritTalk on 17/2/6.
 */

public class RecordStateView extends CardView {
    private Context mContext;
    private ImageView imgVoiceLevel;
    private TextView tvRecord;

    public RecordStateView(Context context) {
        super(context);
        init(context);
    }

    public RecordStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_chat_record, null);
        imgVoiceLevel = (ImageView) view.findViewById(R.id.imgVoiceLevel);
        tvRecord = (TextView) view.findViewById(R.id.tvRecord);
        addView(view);
    }

    public void setStateText(int resId) {
        if (tvRecord != null) {
            tvRecord.setText(resId);
        }
    }

    public void setVoiceLevel(int voiceLevel) {
        LoggerUtil.d("voiceLevel: " + voiceLevel);
        int resId = mContext.getResources().getIdentifier("voice_level_" + voiceLevel, "drawable", mContext.getPackageName());
        imgVoiceLevel.setImageResource(resId);
    }
}
