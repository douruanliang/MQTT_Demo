package io.dourl.http;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.eclipse.paho.android.service.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.dourl.http.callback.CallAdapterFactory;
import io.dourl.http.retrofit.RetrofitManager;
import io.dourl.mqtt.constants.Constants;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.utils.StorageUtils;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpApiBase {
    protected static final int LIMIT = 20;
    private static SharedPreferences sp;

    public static String getSecureBaseUrl() {
        // return HOST == 0 ? "https://" + IPS[HOST] : "http://" + IPS[HOST];
        return "http://" + io.dourl.mqtt.BuildConfig.API_HOST+ ":9090";
    }

    public static final int DEFAULT_MAX_CONNECTIONS = 5;
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 60 * 1000;

    public static OkHttpClient sClient;

    @SuppressWarnings("ConstantConditions")
    public static void init(Application context) {

        initHost(context);
        //LoggerUtil.d("Init old_host is %d, BuildConfig Host is %d", HOST/*BuildConfig.HOST*/);
        Cache cache = new Cache(StorageUtils.getOwnCacheDirectory(context, "net"), 50 * 1024 * 1024);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(DEFAULT_MAX_CONNECTIONS);
        dispatcher.setMaxRequestsPerHost(DEFAULT_MAX_CONNECTIONS);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .dispatcher(dispatcher)
                .dns(Dns.SYSTEM)
                .cache(cache);
        //.addInterceptor(new RequestInterceptor());
        if (BuildConfig.DEBUG) {
            //builder.addInterceptor(HttpLogInterceptorCreator.create());
            builder.addNetworkInterceptor(new DebugNetworkInterceptor());
            // builder.addNetworkInterceptor(DebugToolsKt.getStethoIntercepter());
            // builder.addInterceptor(DebugToolsKt.getPandoraInterceptor());
        }
//        builder.hostnameVerifier(new ReleaseHostnameVerifier());

        try {
           /* char[] chars = Cng.ta().toCharArray();
            chars[8] -= 2;
//            builder.sslSocketFactory(TrustUtils.getTrustSslSocketFactory(null,
//                    AppConstant.getApp().getAssets().open("gameme.p12"), "2CApJOxL398ES4xo"));
            X509TrustManager trustManager = SSLContextFactory.loadTrustManagers(AppConstant.getApp().getAssets().open("being.com.pem"));
            builder.sslSocketFactory(SSLContextFactory.makeContext(
                    AppConstant.getApp().getAssets().open("gameme.p12"), new String(chars),
                    trustManager
            ).getSocketFactory(), trustManager);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        //for retrofit
        if (BuildConfig.DEBUG) {
            RetrofitManager.enableDebug();
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(getSecureBaseUrl());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(new CallAdapterFactory());
        //retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());

        sClient = builder.build();
        RetrofitManager.get().initRetrofit(retrofitBuilder, sClient);
    }

    public static void cancelAll() {
        if (sClient != null) {
            sClient.dispatcher().cancelAll();
        }
    }


    private static void initHost(Context context) {
        if (BuildConfig.DEBUG) {
            if (sp == null) {
                sp = context.getSharedPreferences("host", Context.MODE_PRIVATE);
            }
            // HOST = sp.getInt(PrefConstants.PREF_HOST, BuildConfig.HOST);
        }
    }

    private static class DebugNetworkInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.removeHeader("If-None-Match");
            return chain.proceed(builder.build());
        }
    }


    static HttpUrl getRequestUrl(@Nullable HttpUrl httpUrl) {
        if (httpUrl == null) {
            return null;
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        urlBuilder.addQueryParameter("device_id", Constants.DEVICEID + "@");
        // urlBuilder.addQueryParameter("lang", LocaleHelper.getLanguage());
        // urlBuilder.addQueryParameter("app_id", HttpApiBase.HOST != 1 ? BuildConfig.APP_ID : BuildConfig.DEBUG_APP_ID);
        urlBuilder.addQueryParameter("app_version", Constants.VERSION_NAME);
        urlBuilder.addQueryParameter("app_version_code", String.valueOf(Constants.VERSION_CODE));
        urlBuilder.addQueryParameter("app_channel", Constants.CHANNEL);
        urlBuilder.addQueryParameter("os_version", Constants.OS_VERSION);
        urlBuilder.addQueryParameter("os_name", "Android");
        urlBuilder.addQueryParameter("package_name", Constants.PACKAGE_NAME);
        if (LoginManager.isLogin()) {
            urlBuilder.addQueryParameter("access_token", LoginManager.getToken());
        }
//        long timeStamp = System.currentTimeMillis() / 1000;
//        if (html5) {
//            urlBuilder.addQueryParameter("timestamp", String.valueOf(timeStamp));
//            urlBuilder.addQueryParameter("sign", AuthUtils.md5Sign(timeStamp, HttpApiBase.HOST != 1 ? BuildConfig.APP_SECRET : BuildConfig.DEBUG_APP_SECRET,
//                    LoginManager.getInstance().getSecret()));
//        }
        return urlBuilder.build();
    }

    public static @Nullable
    HttpUrl getRequestUrl(@Nullable String url) {
        if (url == null) {
            return null;
        }
        return getRequestUrl(HttpUrl.parse(url));
    }

    /**
     * 基础download请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     *
     * @param url              请求的url
     * @param filePath         需要保存的文件路径
     *                         如果是文件夹，会从url中解析文件名，如果是绝对路径，则按照指定文件名保存
     * @param responseCallback 请求的回调
     * @return CallHandler
     */
   /* public static BaseDownloadTask download(final String url, final String filePath, final DownloadCallback responseCallback) {
        return download(url, filePath, false, responseCallback);
    }*/

    /**
     * 基础download请求，在回调中返回获取的数据。
     * 所有的回调都运行在download线程中
     *
     * @param url              请求的url
     * @param filePath         需要保存的文件路径
     *                         如果是文件夹，会从url中解析文件名，如果是绝对路径，则按照指定文件名保存
     * @param sync             true为同步回调
     * @param responseCallback 请求的回调
     * @return CallHandler
     */
    /*public static BaseDownloadTask download(final String url, final String filePath, boolean sync, final DownloadCallback responseCallback) {
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(url).setPath(filePath, new File(filePath).isDirectory()).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (responseCallback != null) {
                    responseCallback.onProgress(soFarBytes, totalBytes, totalBytes == soFarBytes);
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (responseCallback != null) {
                    long total = task.getSmallFileTotalBytes();
                    responseCallback.onProgress(total, total, true);
                    responseCallback.onSuccess(new File(filePath));
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (responseCallback != null) {
                    String message = task.getUrl() + "\n" + (e != null ? e.getMessage() : "");
                    responseCallback.onFail(0, new File(task.getPath()), new FileDownloadExecption(message, e));
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });
        baseDownloadTask.setSyncCallback(sync);
        baseDownloadTask.start();
        return baseDownloadTask;
    }*/

}
