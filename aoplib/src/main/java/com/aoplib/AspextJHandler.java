package com.aoplib;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 不听话的好孩子 on 2018/4/23.
 */

@Aspect
public class AspextJHandler {
    Map<String, Long> debounceMaps = new HashMap<>(3);

    Map<String, List<Object>> threads = new HashMap<>(3);
    Handler handler = new Handler(Looper.getMainLooper());

    @Pointcut("execution(@com.aoplib.Debounce * *(..))")
    public void Debounce() {
    }

    @Pointcut("execution(@com.aoplib.UI * *(..))")
    public void UI() {
    }
    @Pointcut("execution(@com.aoplib.NewThread * *(..))")
    public void NewThread() {
    }
    @Pointcut("execution(@com.aoplib.CancelThread * *(..))")
    public void CancelThread() {
    }

    @Around("Debounce()&&@annotation(annotation)")
    public Object debounce(ProceedingJoinPoint joinPoint, Debounce annotation) {
        Object[] args = joinPoint.getArgs();
        String key = annotation.mark();
        if(TextUtils.isEmpty(key)){
            key=joinPoint.getSourceLocation().getFileName();
        }
        if (joinPoint.getSignature().getName().equals("onClick")) {
            if (args != null && args.length == 1 && args[0] instanceof View) {
                key = ((View) args[0]).getId() + "";
            }
        }
        Long lastTime = debounceMaps.get(key);
        if (lastTime == null) {
            lastTime = System.currentTimeMillis();
            debounceMaps.put(key, lastTime);
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            if (System.currentTimeMillis() - lastTime > annotation.value()) {
                lastTime = System.currentTimeMillis();
                debounceMaps.put(key, lastTime);
                try {
                    return joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return null;
    }

    @Around("UI()&&@annotation(annotation)")
    public void ui(final ProceedingJoinPoint joinPoint, UI annotation) {
        String mark = annotation.value();
        int delay = annotation.delay();
        if(TextUtils.isEmpty(mark)){
            mark=joinPoint.getSourceLocation().getFileName();
        }
        final String value=mark;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Object> objects = threads.get(value);
                    if (objects == null) {
                        objects = new ArrayList<>();
                        threads.put(value, objects);
                    }
                    objects.add(this);
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        },delay);
    }

    @Around("NewThread()&&@annotation(annotation)")
    public void newThread(final ProceedingJoinPoint joinPoint, NewThread annotation) {
        String mark = annotation.value();
        final int delay = annotation.delay();
        if(TextUtils.isEmpty(mark)){
            mark=joinPoint.getSourceLocation().getFileName();
        }
        final String value=mark;
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        List<Object> objects = threads.get(value);
        if (objects == null) {
            objects = new ArrayList<>();
            threads.put(value, objects);
        }
        objects.add(thread);
        thread.start();
    }

    @Before("CancelThread()&&@annotation(annotation)")
    public void cancelThread(JoinPoint joinPoint, CancelThread annotation) {
        String[] value = annotation.value();
        if(value.length==0){
            value=new String[]{joinPoint.getSourceLocation().getFileName()};
        }
        for (String key : value) {
            List<Object> objects = threads.get(key);
            if(objects!=null){
                for (Object object : objects) {
                    if(object instanceof Runnable){
                        handler.removeCallbacks((Runnable) object);
                    }else {
                        try {
                            if(((Thread)object).isAlive()) {
                                ((Thread) object).interrupt();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            threads.remove(key);
            System.out.println("标记为 "+key+" 的任务已取消");
        }
    }
    //    @AfterReturning(pointcut = "Debounce()",returning = "result")
//    public void ifCanVisit(String result) {
//    }
//    @Around("methodCheckIfCanVisit()")
//    public Object ifCanVisit2(ProceedingJoinPoint joinPoint) {
//          Object aThis = joinPoint.getThis();
//        return null;
//    }

}
