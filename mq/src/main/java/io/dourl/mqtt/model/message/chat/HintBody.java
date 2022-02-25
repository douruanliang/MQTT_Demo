package io.dourl.mqtt.model.message.chat;

import android.text.TextUtils;

import java.util.List;

import io.dourl.mqtt.base.BaseObject;

/**
 * 提示消息体
 * Created by zhangheng on 18/2/2.
 */
public class HintBody extends BaseMsgBody {

    protected List<ContentEntity> content;

    public HintBody() {
    }

    public List<ContentEntity> getContent() {
        return content;
    }

    public void setContent(List<ContentEntity> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ImageBody{" +
                "content=" + content +
                "} " + super.toString();
    }

    public static class ContentEntity implements BaseObject {
        private String t;
        private String c;
        private String c_zh;
        private String c_en;

        public ContentEntity(String t, String c) {
            this.t = t;
            this.c = c;
        }

        public String getC_zh() {
            if (TextUtils.isEmpty(c_zh))
                return c;
            return c_zh;
        }

        public void setC_zh(String c_zh) {
            this.c_zh = c_zh;
        }

        public String getC_en() {
            if (TextUtils.isEmpty(c_en))
                return c;
            return c_en;
        }

        public void setC_en(String c_en) {
            this.c_en = c_en;
        }

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "t='" + t + '\'' +
                    ", c=" + c +
                    '}';
        }
    }
}
