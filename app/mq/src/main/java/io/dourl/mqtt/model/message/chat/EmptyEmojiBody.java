package io.dourl.mqtt.model.message.chat;
import io.dourl.mqtt.base.BaseObject;

/**
 * 空文字表情消息体
 * Created by zhangheng on 2018/3/20.
 */

public class EmptyEmojiBody extends BaseMsgBody {

    private ContentEntity content;

    public EmptyEmojiBody() {
        type = BodyType.TYPE_FANCY_SMILE_BALL;
    }

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EmptyEmojiBody{" +
                "content=" + content +
                "} " + super.toString();
    }

    public static class ContentEntity implements BaseObject {
        private int ball;
        private int smile;

        public int getBall() {
            return ball;
        }

        public void setBall(int ball) {
            this.ball = ball;
        }

        public int getSmile() {
            return smile;
        }

        public void setSmile(int smile) {
            this.smile = smile;
        }

        @Override
        public String toString() {
            return "ContentEntity{" +
                    "ball='" + ball + '\'' +
                    ", smile=" + smile +
                    '}';
        }
    }
}
