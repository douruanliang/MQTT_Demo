package io.dourl.http.coroutine.internal

import io.dourl.http.coroutine.Initializer
import io.dourl.mqtt.utils.log.LoggerUtil
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * File description.
 *  不是代理的代理类- 反向代理
 * @author dourl
 * @date 2022/10/24
 */
internal abstract class CallDelegate<TIn, TOut>(protected val proxy: Call<TIn>) : Call<TOut> {

    final override fun clone(): Call<TOut> = cloneImpl()
    final override fun execute(): Response<TOut> = executeImpl()
    final override fun enqueue(callback: Callback<TOut>) {
        LoggerUtil.e("CallDelegate","啥时候调的")
        enqueueImpl(callback)
    }

    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = Initializer.sandwichTimeout ?: proxy.timeout()


    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun executeImpl(): Response<TOut>
    abstract fun cloneImpl(): Call<TOut>

}