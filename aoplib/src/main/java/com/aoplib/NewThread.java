package com.aoplib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 不听话的好孩子 on 2018/4/26.
 * 在新子线程执行方法
 * 需要与取消{@link CancelThread}共同使用
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NewThread {
    /**
     * 默认根据是所在类名mark
     * @return
     */
    String value() default "";

    /**
     * 延迟时间
     * @return
     */
    int delay() default 0;
}
