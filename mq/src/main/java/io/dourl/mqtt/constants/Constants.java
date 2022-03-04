package io.dourl.mqtt.constants;


import io.dourl.mqtt.utils.DeviceInfoUtils;

/**
 * 包含各种常量的类
 * Created by zhangpeng on 16/1/5.
 */
public class Constants{

    /**
     * 首页是否处于可显示状态
     */
    public static boolean isHomeVisible = false;

//    public static final boolean DEBUG = true;

    public static String VERSION_NAME;

    public static int VERSION_CODE;

    public static String PACKAGE_NAME;

    public static String MAC_ADDRESS;

    public static String DEVICEID = "";

    public static int SCREENWIDTH;

    public static int SCREENHEIGHT;

    public static float SCREENDENSITY;

    public static float SCREENDENSITYDPI;

    public static float SCREENSCALEDDENSIT;

    public static String CHANNEL = "main";

    public static String NETWORKCOUNTRYISO = "";

    public static String SIMCOUNTRYISO = "";

    public static String NETWORKOPERATOR = "";

    public static String SIMOPERATOR = "";

    public static String TimeZone = "";

    public static final String DEVICE_NAME = DeviceInfoUtils.getDeviceName();

    public static final String OS_VERSION = "Android " + android.os.Build.VERSION.RELEASE;

    public static final String USER_AGENT = DeviceInfoUtils.getUserAgent();

    public static final int IMAGE_SCALE_SIZE = 2196;

    public static final int MAX_PRICE = 500;

    public static int STATUSBAR_HEIGHT = 0;

    public static final String DIR = "gameme";


    /**
     * 打开相册
     */
    public static final int REQUEST_CODE_OPEN_ALBUM = 1;

    /**
     * 打开相机拍照
     */
    public static final int REQUEST_CODE_TAKE_PHOTO = 2;

    /**
     * 打开打开相机录像
     */
    public static final int REQUEST_CODE_RECORD_VIDEO = 3;

    /**
     * 打开相册
     */
    public static final int REQUEST_CODE_CHOOSE_VIDEO = 4;

    /**
     * 启动MainActivity
     */
    public static final int REQUEST_CODE_MAINACTIVITY = 7;

    /**
     * 启动测试页面
     */
    public static final int REQUEST_CODE_TESTACTIVITY = 8;

    /**
     * 裁剪头像
     */
    public static final int REQUEST_CODE_CROP_PHOTO = 9;

    /**
     * 扫描二维码
     */
    public static final int REQUEST_CODE_SCAN_QRCODE = 13;


    public static String PROVIDER = ".fileprovider";

    public static String FILE_PROVIDER;

    public static String SCHEME_HOST = "https://im.game.com?";
    public static String HOST = "im.game.com";

    public static final String SCHEME_TYPE_USER = "0";//用户
    public static final String SCHEME_TYPE_TRIBE = "1";//部落

    public static String UPGRADE_VERSION_CODE = "upgrade_version_code";
    public static String UPGRADE_NO_TIP = "upgrade_no_tip";
    public static String CHAT_FUNCTION_NO_TIP = "chat_function_tip";

    /**
     * 是否提示绑定google验证器
     */
    public static boolean NEED_TIP_GOOGLE_BIND = false;

    /**
     * 乐乐小游戏App更新官网
     */
    public static final String GAME_WEBSITE = "https://im.game.com";

    public static final String CAPTCHAID = "a5756ebabe664dd0aa0a52647a6bbb46";

    /**
     * 是否可以强制更新
     */
    public static boolean CAN_FORCE_UPGRADE = true;

    /**
     * 是否强制认证
     */
    public static boolean isForceAuth = false;

}
