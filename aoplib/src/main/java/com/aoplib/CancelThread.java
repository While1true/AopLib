package com.aoplib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 不听话的好孩子 on 2018/4/26.
 * 取消标记的所有{@link UI}{@link NewThread}线程
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CancelThread {
    /**
     * 默认根据所在类名文件取消
     * @return
     */
    String[] value() default {};
}
