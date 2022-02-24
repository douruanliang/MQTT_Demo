package io.dourl.mqtt.dao.bean;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig messageModelDaoConfig;
    private final DaoConfig sessionModelDaoConfig;
    private final DaoConfig userModelDaoConfig;

    private final MessageModelDao messageModelDao;
    private final SessionModelDao sessionModelDao;
    private final UserModelDao userModelDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        messageModelDaoConfig = daoConfigMap.get(MessageModelDao.class).clone();
        messageModelDaoConfig.initIdentityScope(type);

        sessionModelDaoConfig = daoConfigMap.get(SessionModelDao.class).clone();
        sessionModelDaoConfig.initIdentityScope(type);

        userModelDaoConfig = daoConfigMap.get(UserModelDao.class).clone();
        userModelDaoConfig.initIdentityScope(type);

        messageModelDao = new MessageModelDao(messageModelDaoConfig, this);
        sessionModelDao = new SessionModelDao(sessionModelDaoConfig, this);
        userModelDao = new UserModelDao(userModelDaoConfig, this);

        registerDao(MessageModel.class, messageModelDao);
        registerDao(SessionModel.class, sessionModelDao);
        registerDao(UserModel.class, userModelDao);
    }
    
    public void clear() {
        messageModelDaoConfig.clearIdentityScope();
        sessionModelDaoConfig.clearIdentityScope();
        userModelDaoConfig.clearIdentityScope();
    }

    public MessageModelDao getMessageModelDao() {
        return messageModelDao;
    }

    public SessionModelDao getSessionModelDao() {
        return sessionModelDao;
    }

    public UserModelDao getUserModelDao() {
        return userModelDao;
    }

}
