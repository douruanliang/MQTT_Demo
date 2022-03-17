package io.dourl.mqtt.job.msg;


import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;

import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.base.log.LoggerUtil;

/**
 * 处理新闻消息
 */
public class ProcessNewsMsgJob extends BaseMessageJob {

    private String mMsgString;

    public ProcessNewsMsgJob(String mMsg) {
        mMsgString = mMsg;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mMsgString)) {
            LoggerUtil.d("mqtt message is null!");
            return;
        }
        try {
           /* NHLog.d("mqtt message is " + mMsgString);
            NewsModel newsModel = GsonManager.getGson().fromJson(mMsgString, NewsModel.class);
            String oldIdStr = ConfigUtils.getString(PrefConstants.PREF_NEWS_ID, "");
            try {
                BigInteger oldId = new BigInteger(oldIdStr);
                BigInteger newId = new BigInteger(newsModel.id);
                if (oldId.compareTo(newId) >= 0) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            BeingCallAdapterFactory.ImCall<NewslatestResponse> imCall = RetrofitManager.get().create(NewsApis.class).getLatest(newsModel.id);
            imCall.enqueue(new ICallback<NewslatestResponse>() {
                @Override
                public void onSuccess(NewslatestResponse baseData) {
                    if (baseData.data != null && baseData.data.news != null) {
                        NewsManager.INSTANCE.preSaveToDb(baseData);
                    }
                }

                @Override
                public boolean onFail(int statusCode, @Nullable NewslatestResponse failDate, @Nullable Throwable error) {
                    return false;
                }

                @Override
                public void onFinish() {

                }
            });*/
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    static class NewsModel implements BaseObject {
        String id;
    }

}
