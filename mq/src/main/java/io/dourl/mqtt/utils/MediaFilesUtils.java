package io.dourl.mqtt.utils;

import android.content.Context;
import android.os.Environment;


import java.io.File;

/**
 * 处理所有的媒体文件存储
 * Created by zhangpeng on 17/1/20.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class MediaFilesUtils {

    public static final String DIR = "your";

    public static final String SDIR = "sdir";

    public static final String MEDIADIR = "Media";

    /**
     * 获取用户调用拍照或者录像生成临时文件的位置
     * 存储在用户可见的区域
     * @param context
     * @return
     */
    public static File getMediaFileTopDir(Context context) {
        File dir = Environment.getExternalStoragePublicDirectory(MEDIADIR);
        if (dir != null) {
            if (!dir.exists()) {
                boolean r = dir.mkdir();
                if (r) {
                    return dir;
                }
            } else {
                return dir;
            }
        }
        return StorageUtils.getOwnFilesDirectory(context, MEDIADIR);
    }

    public static File getImageFile(Context context) {
        return new File(getMediaFileTopDir(context), CacheFileUtils.generatePictureFilename());
    }

    public static File getVideoFile(Context context) {
        return new File(getMediaFileTopDir(context), CacheFileUtils.generateVideoFilename());
    }

    public static File getAudioFile(Context context) {
        return new File(getMediaFileTopDir(context), generateAudioFilename());
    }

    public static File getSessionFileDir(Context context, String sId) {
        return new File(StorageUtils.getOwnCacheDirectory(context, SDIR), sId);
    }

    /**
     * 获取每个session的文件存储位置
     * @param context
     * @param sId
     * @return
     */
    public static File getSessionMediaDir(Context context, String sId) {
        File file = new File(StorageUtils.getOwnCacheDirectory(context, SDIR), sId);
        file.mkdirs();
        return file;
    }

    public static File getSessionImageFile(Context context, String sID) {
        return new File(getSessionMediaDir(context, sID), CacheFileUtils.generatePictureFilename());
    }

    public static File getSessionVideoFile(Context context, String sID) {
        return new File(getSessionMediaDir(context, sID), CacheFileUtils.generateVideoFilename());
    }

    public static File getSessionAudioFile(Context context, String sID) {
        return new File(getSessionMediaDir(context, sID), generateAudioFilename());
    }

    public static String generateAudioFilename() {
        long dateTake = System.currentTimeMillis();
        return "audio" + "_" + dateTake;
    }

}
