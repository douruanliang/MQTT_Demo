package io.dourl.mqtt.model.message.chat;


import java.util.List;

import io.dourl.mqtt.base.BaseObject;

/**
 * 红包消息体
 */
public class RedPacketBody extends BaseMsgBody {


    public static final int NOMAL = 0;

    public static final int NOMAL_GROUP = 1;

    public static final int LUCKY = 2;


    private List<ContentEntity> content;

    public RedPacketBody() {
        type = BodyType.TYPE_RED_PACKET;
    }

    public List<ContentEntity> getContent() {
        return content;
    }

    public void setContent(List<ContentEntity> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RedPacketBody{" +
                "content=" + content +
                "} " + super.toString();
    }

    public static class ContentEntity implements BaseObject {
        private String c;//红包标题
        private String t;
        private String coin_type;//币类型
        private String coin_name;//币名称
        private String coin_icon;//币图片

        private int type;// "红包类型：(0=普通红包|1=群普通红包|2=群拼手气红包）",
        private RedPkgStatus state = RedPkgStatus.UNOPEN;//领取状态：0-未领取、1-已领取、2-已过期 、3-已领完
        private String packet_id;//红包id


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getCoin_type() {
            return coin_type;
        }

        public void setCoin_type(String coin_type) {
            this.coin_type = coin_type;
        }

        public String getCoin_name() {
            return coin_name;
        }

        public void setCoin_name(String coin_name) {
            this.coin_name = coin_name;
        }

        public String getCoin_icon() {
            return coin_icon;
        }

        public void setCoin_icon(String coin_icon) {
            this.coin_icon = coin_icon;
        }

        public RedPkgStatus getState() {
            if (state == null)
                return RedPkgStatus.UN_KNOWN;
            return state;
        }

        public void setState(RedPkgStatus state) {
            this.state = state;
        }

        public String getPacket_id() {
            return packet_id;
        }

        public void setPacket_id(String packet_id) {
            this.packet_id = packet_id;
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "c='" + c + '\'' +
                    ", t='" + t + '\'' +
                    ", coin_type=" + coin_type +
                    ", coin_name='" + coin_name + '\'' +
                    ", coin_icon='" + coin_icon + '\'' +
                    ", state=" + state +
                    ", packet_id='" + packet_id + '\'' +
                    '}';
        }
    }
}
