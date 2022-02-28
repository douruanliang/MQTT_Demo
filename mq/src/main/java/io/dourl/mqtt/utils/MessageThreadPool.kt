package io.dourl.mqtt.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * 消息处理线程池
 */
object MessageThreadPool {

    private var receiveExecutor: ExecutorService = Executors.newCachedThreadPool()

    private var sendExecutor: ExecutorService = Executors.newFixedThreadPool(1)

    fun sendMessage(runnable: Runnable): Future<*>? {
        return sendExecutor.submit(runnable)
    }

    fun receiveMessage(runnable: Runnable): Future<*>? {
        return receiveExecutor.submit(runnable)
    }
}