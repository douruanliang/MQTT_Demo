package io.dourl.mqtt.model.message.chat;
import io.dourl.mqtt.base.BaseObject;

/**
 * Created by zhangpeng on 16/1/21.
 */
public class AudioBody extends BaseMsgBody {
    public AudioBody() {
        setType(BodyType.TYPE_AUDIO);
    }

    /**
     * r : http://i0.nihao.com/FiIqlKtuQlL5aRi7Sz71cz9_dY-I
     * l : 6
     */

    private boolean mPlaying;

    private ContentEntity content;

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public boolean isPlaying() {
        return mPlaying;
    }

    public void setPlaying(boolean playing) {
        mPlaying = playing;
    }

    @Override
    public String toString() {
        return "AudioBody{" +
                "mPlaying=" + mPlaying +
                ", content=" + content +
                "} " + super.toString();
    }

    @Override
    protected String getServerUrl() {
        return content.r;
    }

    public static class ContentEntity implements BaseObject {
        private String r;
        private int l;

        public String getR() {
            return r;
        }

        public void setR(String r) {
            this.r = r;
        }

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "r='" + r + '\'' +
                    ", l=" + l +
                    '}';
        }
    }
}
