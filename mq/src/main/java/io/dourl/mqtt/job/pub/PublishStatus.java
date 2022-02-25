package io.dourl.mqtt.job.pub;
import java.io.Serializable;

/**
 * 发布状态
 */

public enum PublishStatus implements Serializable {
    prepare(0),
    uploading(1),
    sending(2),
    success(3),
    fail(-1),
    draft(-2);

    public int value;

    PublishStatus(int i) {
        this.value = i;
    }

    public static PublishStatus valueOf(Integer i) {
        if (i == null) {
            return fail;
        }
        for (PublishStatus publishStatus : PublishStatus.values()) {
            if (i == publishStatus.value) {
                return publishStatus;
            }
        }
        return success;
    }

}
