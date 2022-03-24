package io.dourl.mqtt.ui.widge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import io.dourl.mqtt.model.message.emoji.Emojicon;

public class EmojiconMenuBase extends LinearLayout{
    protected EmojiconMenuListener listener;

    public EmojiconMenuBase(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    public EmojiconMenuBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public EmojiconMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    
    /**
     * set emojicon menu listener
     * @param listener
     */
    public void setEmojiconMenuListener(EmojiconMenuListener listener){
        this.listener = listener;
    }
    
    public interface EmojiconMenuListener{
        /**
         * on emojicon clicked
         * @param emojicon
         */
        void onExpressionClicked(Emojicon emojicon);
        /**
         * on delete image clicked
         */
        void onDeleteImageClicked();

        void onSendClicked();
    }
}
