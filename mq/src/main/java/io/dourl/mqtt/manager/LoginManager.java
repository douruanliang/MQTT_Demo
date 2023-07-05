package io.dourl.mqtt.manager;

import android.text.TextUtils;

import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.model.BaseUser;

/**
 * 已登录用户管理类
 * user信息存储到文件中
 */
@SuppressWarnings("unused")
public class LoginManager {
    private BaseUser mCurrentUser;

    private static class Holder {
        private static LoginManager INSTANCE = new LoginManager();
    }

    /**
     * access_token
     */
    private static volatile String mToken = "mToken";
    /**
     * app_secret
     */
    private static volatile String mSecret = "mSecret";

    private int mInviteMark;
    private int mInviteId;

    private LoginManager() {
        initLoginResult();
    }

    public static LoginManager getInstance() {
        return Holder.INSTANCE;
    }

    public static String getToken() {
        return mToken;
    }

    public static String getSecret() {
        return mSecret;
    }

    /**
     * 是否已经登录
     *
     * @return true为已登录
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(mToken);
    }


    public void setInviteData(int invite_mark, int invite_id) {
        this.mInviteMark = invite_mark;
        this.mInviteId = invite_id;
    }

    public int getInviteMark() {
        return mInviteMark;
    }

    public int getInviteId() {
        return mInviteId;
    }


    private void saveUser(boolean insertToMessageDb) {
        /*DbThreadPool.getInstance().submitUserTask(() -> {
            if (mCurrentUser != null && sToken != null && sSecret != null) {
                ContentValues values = new ContentValues();
                values.put(UserInfoContentProvider.COLLUM_UID, mCurrentUser.getUid());
                values.put(UserInfoContentProvider.COLLUM_USER, GsonManager.getGson().toJson(mCurrentUser));
                values.put(UserInfoContentProvider.COLLUM_TOKEN, SecretHelperKt.encryptString(sToken));
                values.put(UserInfoContentProvider.COLLUM_SECRET, SecretHelperKt.encryptString(sSecret));
                mContentResolver.insert(UserInfoContentProvider.CONTENT_URI, values);
                if (insertToMessageDb) {
                    UserDao.insertOrUpdate(mCurrentUser);
                }
            }
        });*/
    }


    /**
     * 清除所有登录信息
     */
    public void clear() {
        clearMemory();
    }

    private void setUserLiveData() {

    }

    private void clearMemory() {
        clearStaticProperties();
    }

    private static void clearStaticProperties() {
        mToken = null;
        mSecret = null;
    }


    private void initLoginResult() {
        try {
            setUserLiveData();
        } catch (Exception e) {
            clear();
            e.printStackTrace();
        }
    }


    public static void clearOnLogout() {
        LoginManager.clearStaticProperties();
        LoginManager.getInstance().clear();
       /* MqttManager.getInstance().destroy();
        HttpApiBase.cancelAll();
        XGManager.unBindAccount();
        JobManager.getInstance().stop();
        DaoManager.getInstance().clearOnLogout();
        JobManager.getInstance().stop();
        ShortcutHelper.clear();
        EventBusManager.getInstance().post(new LogoutEvent());*/
    }


    public UserModel getCurrentUser() {
        UserModel  mFromUser = new UserModel();
        mFromUser.setUid(getCurrentUserId());
        mFromUser.setAge(30);
        mFromUser.setFullname(getCurrentUserId()+"千年-boss");
        return mFromUser;
    }

    public static String getCurrentUserId() {
        return "lenovo";
    }
}