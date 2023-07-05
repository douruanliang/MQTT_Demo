package io.dourl.mqtt.storage;


import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.dourl.mqtt.utils.log.LoggerUtil;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.bean.UserModelDao;
import io.dourl.mqtt.thread.DaoHandlerThread;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * UserDao管理类
 */
public class UserDao {

    public static CountDownLatch insertOrUpdate(UserModel user) {
        CountDownLatch latch = new CountDownLatch(1);
        DaoHandlerThread.getInstance().execute(() -> {
            if (user != null) {
                UserModelDao userDao = DaoManager.getInstance().getUserDao();
                if (userDao != null) {
                    try {
                        long rowid = userDao.insertOrReplace(user);
                        LoggerUtil.d("update user in rowid: " + rowid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    LoggerUtil.d("UserModelDao is null");
                }
            } else {
                LoggerUtil.d("input user is null");
            }
            latch.countDown();
        });
        return latch;
    }

    /**
     * 全量插入
     *
     * @param users
     */
    public static void insertAll(final List<UserModel> users) {
        UserModelDao userDao = DaoManager.getInstance().getUserDao();
        userDao.deleteAll();
        userDao.insertInTx(users);
        LoggerUtil.d("insertAll count: %d", users != null ? users.size() : 0);
    }

    public static void deleteUser(String uid) {
        UserModelDao userDao = DaoManager.getInstance().getUserDao();
        QueryBuilder<UserModel> queryBuilder = userDao.queryBuilder();
        UserModel oldUser = queryBuilder.where(UserModelDao.Properties.Uid.eq(uid)).build().unique();
        if (oldUser == null) {
            LoggerUtil.d("delete user in id: fail for not exist!" + uid);
            return;
        } else {
            userDao.delete(oldUser);
            LoggerUtil.d("delete user in id: " + uid);
        }
    }


    public static LazyList<UserModel> getAllUsers() {
        UserModelDao userDao = DaoManager.getInstance().getUserDao();
        QueryBuilder<UserModel> queryBuilder = userDao.queryBuilder();
        LazyList<UserModel> tableUserLazyList = queryBuilder.listLazy();
        LoggerUtil.d("getAllUsers size is: " + tableUserLazyList.size());
        return tableUserLazyList;
    }

    public static Observable<LazyList<UserModel>> getAllUsersObservable() {
        return Observable.create(new ObservableOnSubscribe<LazyList<UserModel>>() {
            @Override
            public void subscribe(ObservableEmitter<LazyList<UserModel>> e) throws Exception {
                e.onNext(UserDao.getAllUsers());
            }
        }).subscribeOn(AndroidSchedulers.from(DaoHandlerThread.getInstance().getLooper()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void clearAll() {
        DaoManager.getInstance().getUserDao().deleteAll();
    }
}
