package com.aoplib;

/**
 * Created by 不听话的好孩子 on 2018/4/26.
 */

public class MyCircleThread extends Thread {
    public MyCircleThread(MyCircleRunnable target) {
        super(target);
        target.setThread(this);
    }
}
