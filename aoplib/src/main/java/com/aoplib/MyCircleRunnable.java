package com.aoplib;

/**
 * Created by 不听话的好孩子 on 2018/4/26.
 */

public abstract class MyCircleRunnable implements Runnable {
    Thread thread;

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
