package io.dourl.mqtt.storage;

import androidx.annotation.WorkerThread;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.dourl.mqtt.utils.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.bean.SessionModelDao;
import io.dourl.mqtt.model.message.chat.MessageType;
import io.dourl.mqtt.thread.DaoHandlerThread;

/**
 * SessionDao管理类
 * Created by zhangpeng on 16/1/7.
 */
public class SessionDao {

    public static CountDownLatch insertOrUpdate(SessionModel session) {
        CountDownLatch latch = new CountDownLatch(1);
        DaoHandlerThread.getInstance().execute(() -> {
            SessionModelDao dao = DaoManager.getInstance().getSessionDao();
            if (dao != null) {
                long rowid = dao.insertOrReplace(session);
                LoggerUtil.d("update session in rowid: " + rowid);
            } else {
                LoggerUtil.e("SessionDao is null");
            }
            latch.countDown();
        });
        return latch;
    }

    public static CountDownLatch insertOrUpdate(List<SessionModel> sessions) {
        CountDownLatch latch = new CountDownLatch(1);
        DaoHandlerThread.getInstance().execute(() -> {
            DaoManager.getInstance().getSessionDao().insertOrReplaceInTx(sessions);
            LoggerUtil.d("update session in rowid: all session");
            latch.countDown();
        });
        return latch;
    }

    /**
     * 更新消息发送状态
     *
     * @param sessionId
     * @param sendStatus
     */
    public static void updateSendStatus(String sessionId, MessageModel.Status sendStatus) {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        SessionModel oldSession = findSessionById(sessionId);
        if (oldSession == null) {
            LoggerUtil.d("updateSendStatus fail, can not find that session");
        } else {
            oldSession.setSendStatus(sendStatus);
            long rowid = sessionDao.insertOrReplace(oldSession);
            LoggerUtil.d("updateSendStatus: %s in rowid: %d", sendStatus, rowid);
        }
        DaoHandlerThread.getInstance().execute(() -> {
        });
    }

    /**
     * 更新草稿内容
     *
     * @param sessionId
     * @param draft     草稿内容
     */
    public static SessionModel updateDraft(String sessionId, String draft) {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        SessionModel oldSession = findSessionById(sessionId);
        if (oldSession == null) {
            LoggerUtil.d("updateDraft fail, can not find that session");
        } else {
            oldSession.setDraft(draft);
            long rowid = sessionDao.insertOrReplace(oldSession);
            LoggerUtil.d("updateDraft: %s in rowid: %d", draft, rowid);
        }
        return oldSession;
    }

    /**
     * 清楚session的未读消息数
     *
     * @param sessionId
     * @return
     */
    public static int resetUnreadCount(String sessionId) {
        int result = 0;
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        SessionModel oldSession = findSessionById(sessionId);
        if (oldSession == null) {
            LoggerUtil.d("resetUnreadCount but cannot find that session: " + sessionId);
        } else {
            result = oldSession.getUnreadMsgCount();
            oldSession.setUnreadMsgCount(0);
            if (oldSession.getClan() != null)
                oldSession.getClan().hasAt = false;
            long rowid = sessionDao.insertOrReplace(oldSession);
            LoggerUtil.d("resetUnreadCount in rowid: " + rowid);
        }
        return result;
    }

    public static  SessionModel findSessionById(String sessionId) {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        if (sessionDao != null) {
            QueryBuilder<SessionModel> queryBuilder = sessionDao.queryBuilder();
            return queryBuilder.where(SessionModelDao.Properties.SessionID.eq(sessionId)).build().unique();
        } else {
            return null;
        }
    }

    public static CountDownLatch findSessionById(String sessionId, DbCallback<SessionModel> callback) {
        CountDownLatch latch = new CountDownLatch(1);
        DaoHandlerThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
                SessionModel model = null;
                if (sessionDao != null) {
                    QueryBuilder<SessionModel> queryBuilder = sessionDao.queryBuilder();
                    model = queryBuilder.where(SessionModelDao.Properties.SessionID.eq(sessionId)).build().unique();
                }
                if (callback != null) {
                    callback.onSuccess(model);
                }
                latch.countDown();
            }
        });
        return latch;
    }

    /**
     * 减少未读数
     *
     * @param sessionId
     * @param count     减少的数量
     */
    public static SessionModel subUnreadCount(String sessionId, int count) {
        SessionModel sessionModel = findSessionById(sessionId);
        if (sessionModel != null) {
            int unreadMsgCount = sessionModel.getUnreadMsgCount() - count;
            sessionModel.setUnreadMsgCount(unreadMsgCount > 0 ? unreadMsgCount : 0);
        } else {
            LoggerUtil.d("subUnreadCount but cannot find that session: " + sessionId);
        }
        return sessionModel;
    }

    public static SessionModel updateUnreadCount(String sessionId, int count) {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        SessionModel oldSession = findSessionById(sessionId);
        if (oldSession == null) {
            LoggerUtil.d("resetUnreadCount but cannot find that session: " + sessionId);
        } else {
            oldSession.setUnreadMsgCount(count);
            long rowid = sessionDao.insertOrReplace(oldSession);
            LoggerUtil.d("resetUnreadCount in rowid: " + rowid);
        }
        return oldSession;
    }

    public static SessionModel deleteSession(String sessionId) {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        SessionModel oldSession = findSessionById(sessionId);
        if (oldSession == null) {
            LoggerUtil.d("delete session fail! can not find that session: " + sessionId);
        } else {
            sessionDao.delete(oldSession);
            LoggerUtil.d("delete session in session id: " + sessionId);
        }
        return oldSession;
    }

    public static void deleteAllSession() {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        sessionDao.deleteAll();
    }

    @WorkerThread
    public static List<SessionModel> getAllSessions() {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        List<SessionModel> sessionLazyList = new ArrayList<>();
        if (sessionDao != null) {
            QueryBuilder<SessionModel> queryBuilder = sessionDao.queryBuilder();
            sessionLazyList = queryBuilder.orderAsc(SessionModelDao.Properties.Priority).orderDesc(SessionModelDao.Properties.CreateTime).list();
            LoggerUtil.d("get all sessions  size is: " + sessionLazyList.size());
        }
        return sessionLazyList;
    }

    @WorkerThread
    public static List<SessionModel> getSessionsByMsgType(MessageType type) {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        QueryBuilder<SessionModel> queryBuilder = sessionDao.queryBuilder();
        List<SessionModel> sessionList = queryBuilder.where(SessionModelDao.Properties.MsgType.eq(type)).orderDesc(SessionModelDao.Properties.CreateTime).list();
        LoggerUtil.d("get all sessions  size is: " + sessionList.size());
        return sessionList;
    }

    @WorkerThread
    public static List<SessionModel> getAllSessionsList() {
        SessionModelDao sessionDao = DaoManager.getInstance().getSessionDao();
        QueryBuilder<SessionModel> queryBuilder = sessionDao.queryBuilder();
        List<SessionModel> sessionList = queryBuilder.orderDesc(SessionModelDao.Properties.CreateTime).list();
        LoggerUtil.d("get all sessions  size is: " + sessionList.size());
        return sessionList;
    }

    public static void clearAll() {
        DaoManager.getInstance().getSessionDao().deleteAll();
    }
}
