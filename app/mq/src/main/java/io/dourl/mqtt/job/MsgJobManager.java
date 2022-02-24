package io.dourl.mqtt.job;

import android.content.Context;

import androidx.annotation.WorkerThread;

import com.birbit.android.jobqueue.CancelResult;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.JqLog;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/2/23
 */
public class MsgJobManager {
    private static MsgJobManager ourInstance;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public static MsgJobManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new MsgJobManager();
        }
        return ourInstance;
    }

    private MsgJobManager() {
    }


    public <T> Future<T> addJob(Callable<T> task) {
        return mExecutorService.submit(task);
    }

    public Future<?> addJob(Runnable runnable) {
        return mExecutorService.submit(runnable);
    }

    public <T> Future<T> submit(Runnable task, T result){
        return mExecutorService.submit(task, result);
    }


    public void stop() {
        mExecutorService.shutdown();
        ourInstance = null;
    }
}
