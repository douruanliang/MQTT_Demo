package io.dourl.mqtt.model.message.chat;

import io.dourl.mqtt.base.BaseObject;

/**
 * Created by zhangpeng on 16/4/25.
 */
public class AImageBody extends BaseMsgBody {

    /**
     * item_type : gif
     * bundle_name : meitu_xiaolongbaoji
     * bundle_id :
     * item_name : a2
     * h : 400
     * item_url : http://s0.nihao.com/emoji/20160225/b3e5ba5cd18975fd635271e843126c75
     * bundle_type : 0
     * w : 400
     */

    private ContentEntity content;

    public AImageBody() {
        type = BodyType.TYPE_A_IMAGE;
    }

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public static class ContentEntity implements BaseObject {
        private String item_type;
        private String bundle_name;
        private String bundle_id;
        private String item_name;
        private int h;
        private String item_url;
        private int bundle_type;
        private int w;

        public String getItem_type() {
            return item_type;
        }

        public void setItem_type(String item_type) {
            this.item_type = item_type;
        }

        public String getBundle_name() {
            return bundle_name;
        }

        public void setBundle_name(String bundle_name) {
            this.bundle_name = bundle_name;
        }

        public String getBundle_id() {
            return bundle_id;
        }

        public void setBundle_id(String bundle_id) {
            this.bundle_id = bundle_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public String getItem_url() {
            return item_url;
        }

        public void setItem_url(String item_url) {
            this.item_url = item_url;
        }

        public int getBundle_type() {
            return bundle_type;
        }

        public void setBundle_type(int bundle_type) {
            this.bundle_type = bundle_type;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }
    }
}
