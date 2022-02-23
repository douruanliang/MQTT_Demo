package io.dourl.mqtt.model.message.chat;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;

import com.zhp.base.http.model.BaseObject;
import com.being.fame.R;
import com.being.fame.utils.ImSmileUtils;
import com.being.fame.utils.chat.TextBodyContentUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 文本消息体
 * Created by zhangpeng on 16/1/6.
 */
public class TextBody extends BaseMsgBody {

    protected List<TextEntity> content;
    protected Spannable spanContent;

    /**
     * type : 1
     * content : [{"t":"txt","c":"en"}]
     */

    public TextBody() {
        type = BodyType.TYPE_TEXT;
    }

    public List<TextEntity> getContent() {
        return content;
    }

    public void setContent(List<TextEntity> content) {
        this.content = content;
    }

    public Spannable getSpanContent() {
        return spanContent;
    }

    public Spannable createSpan(Context mContext) {

//        final StringBuilder builder = new StringBuilder();
        // 设置内容
        Spannable span = null;
//        for (TextBody.TextEntity textEntity : getContent()) {
//            if (textEntity.t.equals("notice")) {
//                List<TextEntity> notice = new Gson().fromJson(textEntity.getC(), new TypeToken<List<TextEntity>>() {
//                }.getType());
//                String title = mContext.getResources().getString(R.string.chat_all);
//                builder.append(title);
//                builder.append("\n");
//                for (TextBody.TextEntity item : notice) {
//                    builder.append(item.getC());
//                }
//                span = Spannable.Factory.getInstance().newSpannable(builder.toString());
//                span.setSpan(new ForegroundColorSpan(Color.BLUE), builder.indexOf(title), builder.indexOf(title) + title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            } else {
//                builder.append(textEntity.getC());
//                // 设置内容
//                span = ImSmileUtils.getSmiledText(mContext, builder.toString());
//            }
//        }
        span = TextBodyContentUtils.getSpannableContent(mContext,getContent());
        setSpanContent(span);
        return span;
    }

    public void setSpanContent(Spannable spanContent) {
        this.spanContent = spanContent;
    }

    @Override
    public String toString() {
        return "TextBody{" +
                "content=" + content +
                "} " + super.toString();
    }

    /**
     * t : txt
     * c : en
     */
    public static class TextEntity implements BaseObject {
        public TextEntityType t;
        public String c;
        public String u;
        public transient int start;
        public transient int end;

        public TextEntity(TextEntityType t, String c) {
            this.t = t;
            this.c = c;
        }

        public TextEntity(TextEntityType t, String c, int start, int end) {
            this.t = t;
            this.c = c;
            this.start = start;
            this.end = end;
        }

        public enum TextEntityType implements BaseObject {
            @SerializedName("txt")
            txt,
            @SerializedName("notice")
            notice,
            @SerializedName("link")
            link,
            @SerializedName("email")
            email,
            @SerializedName("@")
            at,
            @SerializedName("tel")
            tel,
            @SerializedName("emotion")
            emotion,
            unknown
        }

        @Override
        public String toString() {
            return "TextEntity{" +
                    "t='" + t + '\'' +
                    ", c='" + c + '\'' +
                    '}';
        }
    }
}

