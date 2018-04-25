package com.aoplib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 不听话的好孩子 on 2018/4/23.
 */

/**
 * 多少毫秒内 只调标记用第一次
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Debounce {

    /**
     * 时间间隔
     * @return
     */
    int value() default 1000;

    /**
     * 根据mark计算时间差值
     * @return
     */
    String mark() default "Debounce";
}
