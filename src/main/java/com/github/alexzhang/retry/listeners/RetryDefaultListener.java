package com.github.alexzhang.retry.listeners;

import com.github.alexzhang.retry.Attempt;
import com.github.alexzhang.retry.RetryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xianshuangzhang
 * @date 2021-10-13 2:39 下午
 */
public class RetryDefaultListener<T> implements RetryListener<T> {
    Logger log= LoggerFactory.getLogger(getClass());
    @Override
    public <T> void onRetry(Attempt<T> attempt) {
        log.info("trying..attempts:{}..returned:{}",attempt.getAttemptNumber(),attempt.getResult());
    }
}
