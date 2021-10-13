package com.github.alexzhang.retry;

import com.github.alexzhang.retry.attempts.AttemptTimeLimiters;
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
}
