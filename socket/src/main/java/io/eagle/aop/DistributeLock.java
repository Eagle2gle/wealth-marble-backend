package io.eagle.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {

    String key(); // 락의 이름
    TimeUnit timeUnit() default TimeUnit.SECONDS; // 시간단위
    long waitTime() default 5L; // 락을 임대하기 위한 대기 시간
    long leaseTime() default 3L; // 락을 임대하는 시간

}
