package io.dourl.http.callback;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import io.dourl.http.exception.BeingApiException;
import io.dourl.http.model.IResponse;
import io.dourl.mqtt.BuildConfig;
import io.dourl.mqtt.base.log.LoggerUtil;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 请求适配器,统一处理部分业务逻辑
 */
public class CallAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (getRawType(returnType) != ImCall.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "Call must have generic type (e.g., Call<ResponseBody>)");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Executor callbackExecutor = retrofit.callbackExecutor();
        return new ErrorHandlingCallAdapter<>(responseType, callbackExecutor);
    }

    @SuppressWarnings("unchecked")
    private static final class ErrorHandlingCallAdapter<R> implements CallAdapter<R, ImCall<R>> {
        private final Type responseType;
        private final Executor callbackExecutor;

        ErrorHandlingCallAdapter(Type responseType, Executor callbackExecutor) {
            this.responseType = responseType;
            this.callbackExecutor = callbackExecutor;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public ImCall<R> adapt(@NonNull Call<R> call) {
            return new InternalCallAdapter<>(call, callbackExecutor);
        }

    }

    public interface BaseCall<T> {
        void cancel();

        @Deprecated
        void enqueue(@Nullable ICallback<T> callback);

        void enqueue(@Nullable ICallback<T> callback, Lifecycle lifecycle);

        Response<T> execute() throws IOException;

        BaseCall<T> clone();
    }

    @SuppressWarnings("unused")
    public interface ImCall<T> extends BaseCall<T> {

        Call<T> getCall();

    }

    /**
     * Adapts a {@link Call} to {@link ImCall}.
     */
    @SuppressWarnings("CloneDoesntCallSuperClone")
    private static class InternalCallAdapter<T> implements ImCall<T> {
        private final Call<T> call;
        private final Executor callbackExecutor;

        InternalCallAdapter(Call<T> call, Executor callbackExecutor) {
            this.call = call;
            this.callbackExecutor = callbackExecutor;
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public Call<T> getCall() {
            return call;
        }

        @Override
        public void enqueue(@Nullable final ICallback<T> callback, final Lifecycle lifecycle) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull final Call<T> call, @NonNull final Response<T> response) {
                    if (lifecycle != null) {
                        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
                            // NHLog.i("Lifecycler state is Destroyed, so just return.");
                            return;
                        }
                    }
                    if (callbackExecutor != null) {
                        callbackExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                handleResponse(response, callback);
                            }
                        });
                    } else {
                        handleResponse(response, callback);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull final Throwable t) {
                    if (lifecycle != null) {
                        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
                            LoggerUtil.i("Lifecycler state is Destroyed, so just return.");
                            return;
                        }
                    }
                    if (BuildConfig.DEBUG) {
                        t.printStackTrace();
                    }
                    if (callbackExecutor != null) {
                        callbackExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    try {
                                        callback.onFail(getCode(t), null, t);
                                        callback.onFinish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        if (BuildConfig.DEBUG) {
                                            throw e;
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        if (callback != null) {
                            try {
                                callback.onFail(getCode(t), null, t);
                                callback.onFinish();
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (BuildConfig.DEBUG) {
                                    throw e;
                                }
                            }
                        }
                    }
                }
            });
        }

        public void enqueue(@Nullable final ICallback<T> callback) {
            enqueue(callback, null);
        }

        private int getCode(Throwable t) {
            int code = ICallback.OTHER_STATUS_CODE;
            if (t instanceof IOException) {
                code = ICallback.NO_NETWORK_STATUS_CODE;
            }
            return code;
        }

        @Override
        public Response<T> execute() throws IOException {
            return call.execute();
        }

        private void handleResponse(Response<T> response, @Nullable ICallback<T> callback) {
            try {
                if (response.isSuccessful()) {
                    T body = response.body();
                    if (body instanceof IResponse) {
                        if (((IResponse) body).isSucceeded()) {
                            if (callback != null) {
                                callback.onSuccess(body);
                            }
                        } else {
                            BeingApiException exception = new BeingApiException(((IResponse) body).getErrorCode(),
                                    ((IResponse) body).getErrorMessage());

                            boolean showToast = true;
                            if (callback != null) {
                                showToast = !callback.onFail(((IResponse) body).getErrorCode(), body, exception);
                            }
                            if (showToast) {
                                // NHToastUtils.showToast(((IResponse) body).getErrorMessage());
                            }
                        }
                    } else {
                        if (callback != null) {
                            callback.onSuccess(body);
                        }
                    }
                } else {
                    @SuppressWarnings("ThrowableNotThrown")
                    BeingApiException exception = new BeingApiException(response.code(),
                            response.message());
                    if (callback != null) {
                        callback.onFail(response.code(), response.body(),
                                new BeingApiException(response.code(), response.message()));
                    }
                }
                if (callback != null) {
                    callback.onFinish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (BuildConfig.DEBUG) {
                    throw e;
                }
            }
        }

        @Override
        public ImCall<T> clone() {
            return new InternalCallAdapter<>(call.clone(), callbackExecutor);
        }
    }
}
