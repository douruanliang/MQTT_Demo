/**
 *
 */
package io.dourl.mqtt.thread;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.atomic.AtomicInteger;

import io.dourl.mqtt.BuildConfig;
import io.dourl.mqtt.utils.log.LoggerUtil;

/**
 * 工作线程，在该线程中处理和维护和DB相关的数据吞吐<br>
 */
public class DaoHandlerThread extends Thread {
	private static final AtomicInteger counter = new AtomicInteger(0);// JUST FOR DEBUG
	private static DaoHandlerThread instance = null;
	private final Object lock = new Object();
	private Handler mHandler;

	private DaoHandlerThread(int count) {
		setName("DaoHandler Thread " + count);
		setUncaughtExceptionHandler((t, e) -> {
			if (!BuildConfig.DEBUG) {
				//BugTagsUtils.sendException(e);
			} else {
				LoggerUtil.e("DaoHandlerThread",e.getLocalizedMessage(), e);
			}
		});
	}

	public static DaoHandlerThread getInstance() {
		if (instance == null) {
			synchronized (DaoHandlerThread.class) {
				if (instance == null) {
					instance = new DaoHandlerThread(counter.incrementAndGet());
					instance.start();
				}
			}
		}
		return instance;
	}

	@Override
	public void run() {
		LoggerUtil.d("DaoHandler Thread Started!");
		Looper.prepare();
		synchronized (lock) {
			mHandler = new Handler();
			lock.notifyAll();
		}
		Looper.loop();
		LoggerUtil.d("DaoHandler Thread Stopped!");
		instance = null;
	}

	public Handler getHandler() {
		synchronized (lock) {
			if (mHandler == null) {
				LoggerUtil.d("waiting for DaoHandler thread starting!");
				try {
					lock.wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				return mHandler;
			}
			else {
				return mHandler;
			}
		}
	}

	/**
	 * 终止工作线程
	 */
	public void stopWorking() {
		getLooper().quit();
	}

	/**
	 * 发送到工作线程中执行任务，如果当前线程就是工作线程，则直接执行
	 *
	 * @param runnable
	 */
	public void execute(Runnable runnable) {
		try {
			if (Thread.currentThread() == this) {
				LoggerUtil.d("run in current thread.no need to post");
				runnable.run();
			}
			else {
				getHandler().post(runnable);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancel(Runnable runnable){
		try {
			if(runnable != null){
				getHandler().removeCallbacks(runnable);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Looper getLooper() {
		return getHandler().getLooper();
	}
}
