package com.github.alexzhang.retry;

import com.github.alexzhang.retry.attempts.AttemptTimeLimiters;
import com.github.alexzhang.retry.strategies.StopStrategies;
import com.github.alexzhang.retry.strategies.WaitStrategies;
import com.google.common.base.Predicate;

import java.util.concurrent.TimeUnit;

/**
 * @author xianshuangzhang
 * @date 2021-10-13 3:47 下午
 */
public class RetryHelper {

    /**
     * retry when exception or predicate fired,retry 3 times duration 500ms
     * @param predicates
     * @param <T>
     * @return
     */
    public static <T> Retryer<T> getRetry3TimesRetryWithPredicateResult(Predicate predicates){
        return RetryerBuilder.newBuilder().retryIfResult(predicates)
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(500,TimeUnit.MILLISECONDS))
                .retryIfException()
                .build();
    }
    /**
     * retry when exception,retry 10 times duration 500ms with Fibonacci
     * 斐波那契数列的策略重试
     * @param <T>
     * @return
     */
    public static <T> Retryer<T> getFibonacciAfterAttempts10(){
        return   RetryerBuilder.<T>newBuilder()
                .retryIfException()
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.fibonacciWait(100, 2, TimeUnit.MINUTES))
                .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                .build();
    }
    /**
     * retry when exception,retry 10 times duration 500ms with exponential
     * 指数退避的策略重试，类似于TCP中的重试
     * @param <T>
     * @return
     */
    public static <T> Retryer<T> getxponentialAfterAttempts10(){
        return   RetryerBuilder.<T>newBuilder()
                .retryIfException()
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.exponentialWait(100, 2, TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                .build();
    }

}
