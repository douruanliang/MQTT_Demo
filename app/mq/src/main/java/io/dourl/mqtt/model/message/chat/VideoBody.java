package io.dourl.mqtt.model.message.chat;

import io.dourl.mqtt.base.BaseObject;

/**
 * Created by zhangpeng on 16/2/1.
 */
public class VideoBody extends BaseMsgBody {

    public VideoBody() {
        setType(BodyType.TYPE_VIDEO);
    }

    /**
     * r : http://7xqgm2.com2.z0.glb.qiniucdn.com/FmDqqe5VDKenBzeTmiqfn_0EMh2i
     * w : 480
     * l : 4
     * s : 504.0703
     * h : 360
     * cover_r : http://7xqgm2.com2.z0.glb.qiniucdn.com/Ft2m_nHLMLfrWkhXqoseQ7VMf87C
     */

    /**
     * 本地cover路径
     */
    protected String coverPath;

    private ContentEntity content;

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    @Override
    public String toString() {
        return "VideoBody{" +
                "coverPath='" + coverPath + '\'' +
                ", content=" + content +
                "} " + super.toString();
    }

    @Override
    protected String getServerUrl() {
        return content.r;
    }

    public static class ContentEntity implements BaseObject {
        private String r; //视频地址
        private int w; //宽
        private int l; //时长
        private double s; //视频大小
        private int h; //高
        private String cover_r; //视频封面

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

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }

        public double getS() {
            return s;
        }

        public void setS(double s) {
            this.s = s;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public String getCover_r() {
            return cover_r;
        }

        public void setCover_r(String cover_r) {
            this.cover_r = cover_r;
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "r='" + r + '\'' +
                    ", w=" + w +
                    ", l=" + l +
                    ", s=" + s +
                    ", h=" + h +
                    ", cover_r='" + cover_r + '\'' +
                    '}';
        }
    }

}
