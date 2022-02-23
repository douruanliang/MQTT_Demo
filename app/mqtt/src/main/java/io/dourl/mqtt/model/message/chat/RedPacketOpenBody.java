package io.dourl.mqtt.model.message.chat;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.being.fame.R;
import com.being.fame.net.http.api.login.LoginManager;
import com.being.fame.ui.activity.redpacket.RedPacketResultActivity;
import com.being.fame.utils.chat.TextBodyContentUtils;
import com.zhp.base.AppConstant;
import com.zhp.base.http.model.BaseObject;

import com.zhp.base.http.model.BaseObject;

import java.util.List;

/**
 * 红包已被打开消息体
 */
public class RedPacketOpenBody extends BaseMsgBody {

    private List<ContentEntity> content;

    protected Spannable spanContent;

    public RedPacketOpenBody() {
        type = BodyType.TYPE_RED_PACKET;
    }

    public List<ContentEntity> getContent() {
        return content;
    }

    public void setContent(List<ContentEntity> content) {
        this.content = content;
    }


    public Spannable getSpanContent() {
        return spanContent;
    }

    public Spannable createSpan(Context mContext) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        ContentEntity entity = getContent().get(0);
        if (entity.getState() == RedPkgStatus.EXPIRE) {
            //过期咱不处理
            builder.append("");
        } else {
            String mUid = LoginManager.getInstance().getCurrentUserId();
            //领取人信息 占位符1
            String to = "";
            if (!TextUtils.isEmpty(entity.getTo_uid()) && entity.getTo_uid().equals(mUid)) {
                to = AppConstant.getApp().getString(R.string.you);
            } else {
                to = entity.getTo_name();
            }
            //发送人信息 占位符2
            String from = "";
            if (!TextUtils.isEmpty(entity.getTo_uid()) && entity.getTo_uid().equals(mUid)) {

                if (!TextUtils.isEmpty(entity.getFrom_uid()) && entity.getFrom_uid().equals(mUid)) {
                    from = AppConstant.getApp().getString(R.string.yourself);
                } else {
                    from = entity.getC() + AppConstant.getApp().getString(R.string.of);
                }

            } else {
                if (!TextUtils.isEmpty(entity.getFrom_uid()) && entity.getFrom_uid().equals(mUid))
                    from = AppConstant.getApp().getString(R.string.your);
            }


            String redPacket = AppConstant.getApp().getString(R.string.red_packet);
            SpannableString spannableStr = new SpannableString(redPacket);

            //添加点击事件
            spannableStr.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    RedPacketResultActivity.intentTo(mContext, entity.getPacket_id());
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(ContextCompat.getColor(AppConstant.getApp(), R.color.color_ff944d));//设置文字颜色
                    ds.setUnderlineText(false);//去除超链接自带的下划线
                }
            }, 0, redPacket.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

            builder.append(AppConstant.getApp().getString(R.string.red_packet_format_1, to, from));
            builder.append(spannableStr);
            if (!TextUtils.isEmpty(entity.getFrom_uid()) && entity.getFrom_uid().equals(mUid) && entity.getState() == RedPkgStatus.FINISHED) {
                //发送人信息（用于 %s的红包被领取完） 占位符3
                String owner = "";
                if (!TextUtils.isEmpty(entity.getFrom_uid()) && entity.getFrom_uid().equals(mUid)) {
                    owner = AppConstant.getApp().getString(R.string.your);
                } else {
                    owner = entity.getC() + AppConstant.getApp().getString(R.string.of);
                }
                builder.append(AppConstant.getApp().getString(R.string.red_packet_format_2, owner));
            }
        }
        setSpanContent(builder);
        return builder;
    }

    public void setSpanContent(Spannable spanContent) {
        this.spanContent = spanContent;
    }

    @Override
    public String toString() {
        return "RedPacketBody{" +
                "content=" + content +
                "} " + super.toString();
    }

    public static class ContentEntity implements BaseObject {
        private String packet_id;//红包id
        private String c;//发红包人的name（群聊：群昵称、fullname、username）
        private String t;//redpacket
        private String from_uid;//发红包人uid
        private String coin_icon;//币图标
        private String to_uid;//领红包人uid
        private String to_name;//领取红包人的name（群聊：群昵称、fullname、username）
        private RedPkgStatus state;//领取状态：0-未领完、1-已领完

        public String getTo_name() {
            return to_name;
        }

        public String getPacket_id() {
            return packet_id;
        }

        public void setPacket_id(String packet_id) {
            this.packet_id = packet_id;
        }

        public void setTo_name(String to_name) {
            this.to_name = to_name;
        }

        public RedPkgStatus getState() {
            return state;
        }

        public void setState(RedPkgStatus state) {
            this.state = state;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getFrom_uid() {
            return from_uid;
        }

        public void setFrom_uid(String from_uid) {
            this.from_uid = from_uid;
        }

        public String getCoin_icon() {
            return coin_icon;
        }

        public void setCoin_icon(String coin_icon) {
            this.coin_icon = coin_icon;
        }

        public String getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(String to_uid) {
            this.to_uid = to_uid;
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "c='" + c + '\'' +
                    ", t='" + t + '\'' +
                    ", from_uid='" + from_uid + '\'' +
                    ", coin_icon='" + coin_icon + '\'' +
                    ", to_uid='" + to_uid + '\'' +
                    ", to_name='" + to_name + '\'' +
                    ", state=" + state +
                    '}';
        }
    }
}
