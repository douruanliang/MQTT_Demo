package io.dourl.mqtt.storage;



import android.os.FileUtils;

import java.util.List;

import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.event.SessionEvent;
import io.dourl.mqtt.manager.EventBusManager;
import io.dourl.mqtt.model.ClanModel;
import io.dourl.mqtt.model.message.chat.SessionPriority;
import io.dourl.mqtt.thread.CallbackRunnable;
import io.dourl.mqtt.thread.DaoHandlerThread;

/**
 * 聊天Session管理类
 */
public class SessionManager {
    private static SessionManager ourInstance = new SessionManager();

//    private AtomicInteger mUnreadCount = new AtomicInteger();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (ourInstance == null) {
            synchronized (SessionManager.class) {
                if (ourInstance == null) {
                    ourInstance = new SessionManager();
                }
            }
        }
        return ourInstance;
    }

    public static SessionModel createChatSession(UserModel baseUser, MessageModel messageModel) {
        SessionModel session = new SessionModel();
        switch (messageModel.getType()) {
            case CHAT_NORMAL:
                session.setSessionID(messageModel.getSessionId());
                session.setSessionName(baseUser.getName());
                session.setSessionIcon(baseUser.getAvatar());
                session.setUser(baseUser);
                session.setClan(new ClanModel());
                session.setPriority(SessionPriority.CHAT_NORMAL.value());
                break;
            case CHAT_GROUP:
                session.setSessionID(messageModel.getClan().id);
                session.setSessionName(messageModel.getClan().name);
                session.setSessionIcon(messageModel.getClan().avatar);
                session.setClan(messageModel.getClan());
                session.setPriority(messageModel.getClan().msg_top ? SessionPriority.MSG_TOP.value() : SessionPriority.CHAT_NORMAL.value());
                break;
        }

        session.setContent(messageModel.getContentDesc().toString());
        session.setCreateTime(messageModel.getLocalTime());
        session.setMsgDbId(messageModel.getId());
        session.setSendStatus(messageModel.getSendStatus());
        session.setMsgType(messageModel.getType());
        session.setSessionMsg(messageModel);
        return session;
    }

    /**
     * 消息未读数减一
     */
    public void subUnreadCountByOne(final String sId) {
        DaoHandlerThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
//                EventBusManager.getInstance().post(new SessionEvent(SessionDao.subUnreadCount(sId, 1)));
                SessionDao.subUnreadCount(sId, 1);
//                mUnreadCount.decrementAndGet();
            }
        });
    }

    public void resetUnreadCount(final String sessionID) {
        DaoHandlerThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                int count = SessionDao.resetUnreadCount(sessionID);
//                mUnreadCount.addAndGet(0 - count);
                EventBusManager.getInstance().post(new SessionEvent());
            }
        });
    }

    public void deleteSession(final String sessionID, final CallbackRunnable.Callback callback) {
        DaoHandlerThread.getInstance().execute(new CallbackRunnable(callback) {
            @Override
            public void run() {
                try {
                    SessionModel sessionModel = SessionDao.deleteSession(sessionID);
//                    mUnreadCount.addAndGet(0 - sessionModel.getUnreadMsgCount());
                    MessageDao.deleteMsgBySession(sessionID);
                   // FileUtils.deleteFile(MediaFilesUtils.getSessionFileDir(AppConstant.getApp(), sessionID));
                    callback.onSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e);
                }
            }
        });
    }

    public void deleteAllSession(final CallbackRunnable.Callback callback) {
        DaoHandlerThread.getInstance().execute(new CallbackRunnable(callback) {
            @Override
            public void run() {
                try {
                    SessionDao.deleteAllSession();
                    callback.onSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e);
                }
            }
        });
    }

    public void updateSessionDraft(final String sessionID, String draft) {
        DaoHandlerThread.getInstance().execute((new Runnable() {
            @Override
            public void run() {
                try {
                    SessionModel sessionModel = SessionDao.updateDraft(sessionID, draft);
                    if (sessionModel != null) {
                        EventBusManager.getInstance().post(new SessionEvent(sessionModel));
                    }
                } catch (Exception e) {
                    LoggerUtil.e("update draft exception", e.toString());
                }
            }
        }));
    }


    public void getAllSession(final DbCallback<List<SessionModel>> callback) {
        DaoHandlerThread.getInstance().execute(() -> {
            final List<SessionModel> ss = SessionDao.getAllSessions();
            for (SessionModel s : ss) {
                s.getUser();
                s.getSessionMsg();
            }
//            mUnreadCount.set(unreadCount);
            if (callback != null) {
               // BaseActivity.runOnUi(() -> callback.onSuccess(ss));
            }
        });
    }

    public void calculateUnreadCount() {
        getAllSession(null);
    }

//    public int getUnreadCount() {
//        return mUnreadCount.get();
//    }

//    public int incrementAndGetUnreadCount() {
//        return mUnreadCount.incrementAndGet();
//    }
}
