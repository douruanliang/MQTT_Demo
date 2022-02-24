package io.dourl.mqtt.utils.chat;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.List;

import io.dourl.mqtt.R;
import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.utils.ImSmileUtils;

/**
 * Created by zhangheng on 2018/4/18.
 */

public class TextBodyContentUtils {

    public static Spannable getSpannableDraftContent(Context mContext, List<TextBody.TextEntity> textList) {
        Spannable contentSpannable = getSpannableContent(mContext, textList);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        //String draft = mContext.getString(R.string.draft_content_desc);
        builder.append("");
        //builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_e9394f)), 0, draft.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(contentSpannable);

        return Spannable.Factory.getInstance().newSpannable(builder);
    }

    public static Spannable getSpannableContent(Context mContext, List<TextBody.TextEntity> textList) {
        if (textList != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            LoggerUtil.d("cs is %s", textList.toString());
            int start = 0;
            for (TextBody.TextEntity text : textList) {
                if (text.t == null || text.t.equals(TextBody.TextEntity.TextEntityType.txt)) {
                    builder.append(text.c.replaceAll("(\r\n|\r)", System.getProperty("line.separator")));
                } else {
                    ClickableSpan clickableSpan = null;

                    switch (text.t) {
                        case link:
                            String url = text.u;
                            if (!TextUtils.isEmpty(url)) {
                                if (url.length() <= 4 || (!url.substring(0, 4).equals("http"))) {
                                    url = "http://" + url;
                                }
                                clickableSpan = new LinkSpan(url);
                                start = builder.length();
                                builder.append(text.u);
                            }
                            break;
                        case email:
                            start = builder.length();
                            builder.append(text.c);
                            clickableSpan = new LinkSpan("mailto:" + text.c);
                            break;
                        case at:
//                            clickableSpan = new UserSpan(text.c);
                            break;
                        case notice:
                            List<TextBody.TextEntity> notice = new Gson().fromJson(text.c, new TypeToken<List<TextBody.TextEntity>>() {
                            }.getType());
                           // builder.append(AppConstant.getApp().getString(R.string.chat_all));
                            builder.append(getSpannableContent(mContext, notice));
                            break;
                        case tel:
                            start = builder.length();
                            builder.append(text.c);
                           // clickableSpan = new LinkSpan("tel:" + text.c);
                            break;
                        case emotion:
                            start = builder.length();
                            builder.append(text.c);
                            break;
                    }

                    if (clickableSpan != null) {
                        builder.setSpan(clickableSpan, start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
//                    if (colorSpan != null) {
//                        builder.setSpan(colorSpan, start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    }
                }
            }
            return ImSmileUtils.getSmiledText(mContext, builder);
        } else {
            return null;
        }
    }
}
