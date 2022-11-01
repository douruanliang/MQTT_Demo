package io.dourl.mqtt.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.event.SessionEvent;
import io.dourl.mqtt.manager.EventBusManager;
import io.dourl.mqtt.storage.DbCallback;
import io.dourl.mqtt.storage.SessionManager;
import io.dourl.mqtt.utils.log.LoggerUtil;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/10/31
 */
public class SessionViewModel extends ViewModel {

    private MutableLiveData<List<SessionModel>> mSessionList;

    public SessionViewModel() {
        super();
        EventBusManager.getInstance().register(this);
    }

    public LiveData<List<SessionModel>> getSessionList() {
        if (mSessionList == null) {
            mSessionList = new MutableLiveData<>();
        }
        return mSessionList;
    }

    public void loadData() {
        SessionManager.getInstance().getAllSession(new DbCallback<List<SessionModel>>() {
            @Override
            public void onSuccess(List<SessionModel> sessionModels) {
                mSessionList.setValue(sessionModels);
            }

            @Override
            public void onFail(Throwable e) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sessionEvent(SessionEvent event) {
        LoggerUtil.d("session","sessionEvent{}"+event.toString());
        loadData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBusManager.getInstance().unRegister(this);
    }
}
