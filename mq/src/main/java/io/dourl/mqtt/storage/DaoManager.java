package io.dourl.mqtt.storage;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.WorkerThread;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import io.dourl.mqtt.BuildConfig;
import io.dourl.mqtt.base.MqttBaseApp;
import io.dourl.mqtt.utils.log.LoggerUtil;
import io.dourl.mqtt.bean.DaoMaster;
import io.dourl.mqtt.bean.DaoSession;
import io.dourl.mqtt.bean.MessageModelDao;
import io.dourl.mqtt.bean.SessionModelDao;
import io.dourl.mqtt.bean.UserModelDao;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.storage.db.DbOpenHelper;

/**
 * 管理数据库操作
 */
@WorkerThread
public class DaoManager {

    private static DaoManager mInstance;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private SessionModelDao mSessionModelDao;
    private MessageModelDao mMessageModelDao;
    private UserModelDao mUserModelDao;
    private int version;

    private DaoManager() {
        initDb();
    }

    public static DaoManager getInstance() {
        if (mInstance == null) {
            synchronized (DaoManager.class) {
                if (mInstance == null) {
                    mInstance = new DaoManager();
                }
            }
        }
        return mInstance;
    }

    public void initDb() {
        if (BuildConfig.DEBUG) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
        if (!LoginManager.isLogin()) {
            LoggerUtil.d("not login, so no db!");
            return;
        }
        if (mDaoSession == null) {
            String name = "implus" + LoginManager.getCurrentUserId();
            DbOpenHelper helper = new DbOpenHelper(MqttBaseApp.getApp(), name, null);
            //helper.close();
           // SQLiteDatabase db = helper.getWritableDatabase();
            Database db = helper.getEncryptedWritableDb(LoginManager.getCurrentUserId()); //加密
            mDaoMaster = new DaoMaster(db);
            mDaoSession = mDaoMaster.newSession();

            mSessionModelDao = mDaoSession.getSessionModelDao();
            mMessageModelDao = mDaoSession.getMessageModelDao();
            mUserModelDao = mDaoSession.getUserModelDao();
            version = mDaoMaster.getSchemaVersion();
            LoggerUtil.d("create db name: " + name);
        } else {
            LoggerUtil.d("db version: %d is already create", mDaoMaster.getSchemaVersion());
        }
    }

    public MessageModelDao getMsgDao() {
        return mMessageModelDao;
    }

    public SessionModelDao getSessionDao() {
        return mSessionModelDao;
    }

    public UserModelDao getUserDao() {
        return mUserModelDao;
    }

    public int getVersion() {
        return version;
    }

    public void deleteAll() {
        getMsgDao().deleteAll();
        getSessionDao().deleteAll();
        getUserDao().deleteAll();
    }

    public void clearOnLogout() {
        mInstance = null;
        if (mDaoSession != null) {
            mDaoSession.clear();
        }
        mDaoSession = null;
        mInstance = null;
    }
}
