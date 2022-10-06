package io.dourl.mqtt.storage;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

import io.dourl.mqtt.base.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.MessageModelDao;
import io.dourl.mqtt.bean.MessagePaging;
import io.dourl.mqtt.model.message.chat.MessageType;
import io.dourl.mqtt.thread.DaoHandlerThread;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 用来处理消息的CRUD操作
 * Created by dourl on 16/1/7.
 */
public class MessageDao {

    public static CountDownLatch insert(MessageModel msg) {
        return insertOrUpdate(msg);
    }

    public static CountDownLatch insertOrReplace(List<MessageModel> msg) {
        CountDownLatch latch = new CountDownLatch(1);
        DaoHandlerThread.getInstance().execute(() -> {
            DaoManager.getInstance().getMsgDao().insertOrReplaceInTx(msg);
            latch.countDown();
        });
        return latch;
    }

    public static CountDownLatch insertOrUpdate(MessageModel msg) {
        CountDownLatch latch = new CountDownLatch(1);
        DaoHandlerThread.getInstance().execute(() -> {
            MessageModelDao msgDao = DaoManager.getInstance().getMsgDao();
            if (msgDao != null) {
                long rowID = msgDao.insertOrReplace(msg);
                LoggerUtil.d("insert message in rowid: " + rowID);
            } else {
                LoggerUtil.e("Message dao is null!");
            }
            latch.countDown();
        });
        return latch;
    }

    //    public static void updateSendStatus(String msgId, MessageModel.Status sendStatus) {
//        MessageModelDao messageDao = DaoManager.getInstance().getMsgDao();
//        QueryBuilder<MessageModel> queryBuilder = messageDao.queryBuilder();
//        MessageModel oldMessage = queryBuilder.where(MessageModelDao.Properties.Msgid.eq(msgId)).unique();
//        if (oldMessage == null) {
//            LoggerUtil.d("updateSendStatus fail, can not find that message");
//        } else {//对于语音等消息，需要更新消息体
//            oldMessage.setSendStatus(sendStatus);
//            long rowID = messageDao.insertOrReplace(oldMessage);
//            LoggerUtil.d("updateSendStatus message in rowid: " + rowID);
//        }
//    }
//
    public static FutureTask<MessageModel> getMsgById(String msgId) {
        FutureTask<MessageModel> task = new FutureTask<>(() -> {
            MessageModelDao messageDao = DaoManager.getInstance().getMsgDao();
            QueryBuilder<MessageModel> queryBuilder = messageDao.queryBuilder();
            return queryBuilder.where(MessageModelDao.Properties.Msgid.eq(msgId)).unique();
        });
        DaoHandlerThread.getInstance().execute(task);

        return task;
    }

    public static void setMsgRead(String msgId) {
        DaoHandlerThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                MessageModelDao messageDao = DaoManager.getInstance().getMsgDao();
                QueryBuilder<MessageModel> queryBuilder = messageDao.queryBuilder();
                MessageModel oldMessage = queryBuilder.where(MessageModelDao.Properties.Msgid.eq(msgId)).unique();
                if (oldMessage != null) {
                    oldMessage.setIsRead(true);
                    long rowID = messageDao.insertOrReplace(oldMessage);
                    LoggerUtil.d("set message isRead in rowid: " + rowID);
                }
            }
        });
    }

//    public static void setMsgLocalPath(String msgId, String localPath) {
//        MessageModelDao messageDao = DaoManager.getInstance().getMsgDao();
//        QueryBuilder<MessageModel> queryBuilder = messageDao.queryBuilder();
//        MessageModel oldMessage = queryBuilder.where(MessageModelDao.Properties.Msgid.eq(msgId)).unique();
//        if (oldMessage != null) {
//            oldMessage.setLocalPath(localPath);
//            long rowID = messageDao.insertOrReplace(oldMessage);
//            LoggerUtil.d("set message localPath in rowid: " + rowID);
//        }
//    }

    public static CountDownLatch deleteMsg(MessageModel model) {
        CountDownLatch latch = new CountDownLatch(1);
        DaoHandlerThread.getInstance().execute(() -> {
            if (model != null) {
                DaoManager.getInstance().getMsgDao().delete(model);
                LoggerUtil.d("delete message : " + model.toString());
            } else {
                LoggerUtil.e("MessageModel is null!");
            }
            latch.countDown();
        });
        return latch;
    }

    public static void deleteAllMsg() {
        DaoManager.getInstance().getMsgDao().deleteAll();
    }

    public static void deleteMsgBySession(String sessionId) {
        MessageModelDao dao = DaoManager.getInstance().getMsgDao();
        List<MessageModel> msgsBySessionId = getMsgsBySessionId(sessionId);
        dao.deleteInTx(msgsBySessionId);
        LoggerUtil.d("Delete msg by sessionId: " + sessionId + " size: " + msgsBySessionId.size());
    }

    private static List<MessageModel> getMsgsBySessionId(String sessionId) {
        MessageModelDao dao = DaoManager.getInstance().getMsgDao();
        QueryBuilder<MessageModel> queryBuilder = dao.queryBuilder();
        List<MessageModel> tableMessageLazyList = queryBuilder.where(MessageModelDao.Properties.SessionId.eq(sessionId))
                .orderDesc(MessageModelDao.Properties.Id).build().list();
        LoggerUtil.d("get msg by sessionId: " + sessionId + " get size: " + tableMessageLazyList.size());
        return tableMessageLazyList;
    }

    public static List<MessageModel> getMsgsByMsgType(MessageType messageType) {
        MessageModelDao dao = DaoManager.getInstance().getMsgDao();
        QueryBuilder<MessageModel> queryBuilder = dao.queryBuilder();
        List<MessageModel> tableMessageLazyList = queryBuilder.where(MessageModelDao.Properties.Type.eq(messageType.value()))
                .orderDesc(MessageModelDao.Properties.Id).build().list();
        LoggerUtil.d("get msg by message: " + messageType.value() + " get size: " + tableMessageLazyList.size());
        return tableMessageLazyList;
    }


    private static List<MessageModel> getMsgsBySessionId(String sessionId, long id, int limit) {
        MessageModelDao dao = DaoManager.getInstance().getMsgDao();
        if (dao != null) {
            QueryBuilder<MessageModel> queryBuilder = dao.queryBuilder();
            if (id > 0) {
                queryBuilder.where(MessageModelDao.Properties.SessionId.eq(sessionId), MessageModelDao.Properties.Id.lt(id));
            } else {
                queryBuilder.where(MessageModelDao.Properties.SessionId.eq(sessionId));
            }
            List<MessageModel> tableMessageLazyList = queryBuilder
                    .orderDesc(MessageModelDao.Properties.Id).limit(limit).build().list();
            LoggerUtil.d("get msg by sessionId: %s great than %d, limit by %d", sessionId, id, limit);
            return tableMessageLazyList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 是否还有下页
     *
     * @param sessionId
     * @param id
     * @return
     */
    private static boolean hasMore(String sessionId, long id) {
        MessageModelDao dao = DaoManager.getInstance().getMsgDao();
        if (dao != null) {
            QueryBuilder<MessageModel> queryBuilder = dao.queryBuilder();
            queryBuilder.where(MessageModelDao.Properties.SessionId.eq(sessionId), MessageModelDao.Properties.Id.lt(id));
            return queryBuilder.buildCount().count() > 0;
        } else {
            return false;
        }
    }

    /**
     * 分页获取数据
     *
     * @param sessionId
     * @param id        最旧消息的id
     * @param limit     获取数量
     * @return
     */
    public static Observable<MessagePaging> getMsgsBySessionIdObservable(final String sessionId, final long id, final int limit) {
        return Observable.create((ObservableOnSubscribe<MessagePaging>) emitter -> {
            try {
                MessagePaging messagePaging = new MessagePaging();
                List<MessageModel> list = getMsgsBySessionId(sessionId, id, limit);
                if (list == null || list.isEmpty()) {
                    emitter.onNext(messagePaging);
                    emitter.onComplete();
                    return;
                }
                for (MessageModel messageModel : list) {
                    messageModel.getFrom();
                    messageModel.getTo();
                }
                messagePaging.list = list;
                long lastMsbDbId = list.get(list.size() - 1).getId();
                messagePaging.lastMsgDbId = lastMsbDbId;
                if (list.size() < limit) {
                    messagePaging.hasMore = false;
                } else {
                    messagePaging.hasMore = hasMore(sessionId, lastMsbDbId);
                }
                emitter.onNext(messagePaging);
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }).subscribeOn(AndroidSchedulers.from(DaoHandlerThread.getInstance().getLooper()))
                .observeOn(AndroidSchedulers.mainThread());
    }

}
