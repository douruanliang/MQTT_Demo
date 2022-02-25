package io.dourl.mqtt.model.message.chat;


import io.dourl.mqtt.base.BaseObject;

/**
 * 图像消息体
 * Created by zhangpeng on 16/1/6.
 */
public class ImageBody extends BaseMsgBody {

    /**
     * r : http://i0.nihao.com/Fr_OU9nPao8JVhwYjUrqt-45u39j
     * w : 1024
     * h : 685
     */

    private ContentEntity content;

    public ImageBody() {
        type = BodyType.TYPE_IMAGE;
    }

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ImageBody{" +
                "content=" + content +
                "} " + super.toString();
    }

    @Override
    protected String getServerUrl() {
        return content.r;
    }

    public static class ContentEntity implements BaseObject {
        private String r;
        private int w;
        private int h;

        public String getR() {
            return r;
        }

        public void setR(String r) {
            this.r = r;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "r='" + r + '\'' +
                    ", w=" + w +
                    ", h=" + h +
                    '}';
        }
    }
}
