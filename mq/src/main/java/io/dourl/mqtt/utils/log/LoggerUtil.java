package io.dourl.mqtt.utils.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.dourl.mqtt.BuildConfig;

/**
 * Log管理工具，上线前设置 debugFlag = false。
 */
public class LoggerUtil {
    public static boolean debugFlag = BuildConfig.DEBUG;
    private static final String LOGTAG = "BUG";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void setDebugFlag(boolean debug) {
        debugFlag = debug;
    }

    public static void i(String msg, Object... args) {
        if (debugFlag)
            Log.i(LOGTAG, String.format(msg, args));
    }

    public static void i(String key, String msg) {
        if (debugFlag)
            Log.i(key, msg);
    }

    public static void e(String key, String msg) {
        if (debugFlag)
            Log.e(key, msg);
    }

    public static void e(String key, String msg, Throwable t) {
        if (debugFlag)
            Log.e(key, msg, t);
    }
    public static void e(String msg, Object... args) {
        if (debugFlag)
            Log.e(LOGTAG, String.format(msg, args));
    }

    public static void d(String msg) {
        if (debugFlag)
            Log.d(LOGTAG, msg);
    }

    public static void d(String msg, Object... args) {
        if (debugFlag)
            Log.d(LOGTAG, String.format(msg, args));
    }

    public static void d(String key,String msg, Object... args) {
        if (debugFlag)
            Log.d(key, String.format(msg, args));
    }

    public static void d(String key, String msg) {
        if (debugFlag)
            Log.d(key, msg);
    }


    public static void e(String msg) {
        if (debugFlag)
            Log.e(LOGTAG, msg);
    }

    public static void w(String msg) {
        if (debugFlag)
            Log.w(LOGTAG, msg);
    }

    public static void w(String key, String msg) {
        if (debugFlag)
            Log.w(key, msg);
    }

    public static void printStackTrace(Throwable throwable) {
        if (debugFlag)
            throwable.printStackTrace();
    }

    /**
     * 打印出来Json信息；使用默认tag
     *
     * @param jsonMsg
     */
    public static void printJson(String jsonMsg) {
        printJsonWithHead(LOGTAG, jsonMsg, "");
    }


    /**
     * 打印出来Json信息；使用自定义tag，并且添加Head信息。
     *
     * @param tag
     * @param msg
     * @param headString
     */
    public static void printJsonWithHead(String tag, String msg, String headString) {
        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.i(tag, "║ " + line);
        }
        printLine(tag, false);
    }


    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            //Log.i(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            //Log.i(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

}
