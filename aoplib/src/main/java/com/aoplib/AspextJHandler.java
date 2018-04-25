package com.aoplib;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 不听话的好孩子 on 2018/4/23.
 */

@Aspect
public class AspextJHandler {
    Map<String,Long> debounceMaps =new HashMap<>(3);
    @Pointcut("execution(@com.aoplib.Debounce * *(..))")
    public void Debounce(){}

    @Around("Debounce()&&@annotation(annotation)")
    public Object debounce(ProceedingJoinPoint joinPoint,Debounce annotation){
        Long lastTime = debounceMaps.get(annotation.mark());
        if(lastTime==null){
            lastTime=System.currentTimeMillis();
            debounceMaps.put(annotation.mark(),lastTime);
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }else{
            if(System.currentTimeMillis()-lastTime>annotation.value()){
                lastTime=System.currentTimeMillis();
                debounceMaps.put(annotation.mark(),lastTime);
                try {
                    return joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
       return null;
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
