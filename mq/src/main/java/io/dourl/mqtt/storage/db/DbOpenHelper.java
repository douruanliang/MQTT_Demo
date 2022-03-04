package io.dourl.mqtt.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import org.greenrobot.greendao.database.Database;

import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.bean.DaoMaster;
import io.dourl.mqtt.bean.MessageModelDao;
import io.dourl.mqtt.bean.SessionModelDao;
import io.dourl.mqtt.bean.UserModelDao;

/**
 * 数据库升级操作需要在此类中完成
 */
@SuppressWarnings("unchecked")
public class DbOpenHelper extends DaoMaster.OpenHelper {

    public static int sOldVersion = 0;

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LoggerUtil.d("upgrade db from " + oldVersion + " to " + newVersion);
        sOldVersion = oldVersion;
        if (oldVersion == 2 && newVersion == 3) {//用户表增加标示是否商家的字段
            MigrationHelper.getInstance().migrate(db, UserModelDao.class);
        } else {
            MigrationHelper.getInstance().migrate(db, MessageModelDao.class,
                    SessionModelDao.class,
                    UserModelDao.class);
        }

    }
}
