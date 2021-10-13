
##Quickstart

在guava的基础上进行了重写，主要是增加了默认的listener，用于打印重试的细节和次数
对callable进行了装饰，用于传递mdc，对线程上下文进行了传递，分布式环境下用于追踪的连续性以及采用线程上下文缓存的可读取
A minimal sample of some of the functionality would look like:

##使用简介
```java
Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
        .retryIfResult(Predicates.<Boolean>isNull())
        .retryIfExceptionOfType(IOException.class)
        .retryIfRuntimeException()
        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
        .build();
try {
    retryer.call(callable);
} catch (RetryException e) {
    e.printStackTrace();
} catch (ExecutionException e) {
    e.printStackTrace();
}
```
call过程中异常会返回ExecutionException

##异常重试，指数退避策略

```java
Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
        .retryIfExceptionOfType(IOException.class)
        .retryIfRuntimeException()
        .withWaitStrategy(WaitStrategies.exponentialWait(100, 5, TimeUnit.MINUTES))
        .withStopStrategy(StopStrategies.neverStop())
        .build();
```

##斐波那契重试策略

注意这是永远重试下去，没有停止策略

```java
Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
        .retryIfExceptionOfType(IOException.class)
        .retryIfRuntimeException()
        .withWaitStrategy(WaitStrategies.fibonacciWait(100, 2, TimeUnit.MINUTES))
        .withStopStrategy(StopStrategies.neverStop())
        .build();
```

##自定义结果检查重试

```java
    public static <T> Retryer<T> getRetry3TimesRetryWithPredicateResult(Predicate predicates){
        return RetryerBuilder.newBuilder().retryIfResult(predicates)
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(500,TimeUnit.MILLISECONDS))
                .retryIfException()
                .build();
    }
```

还有其他很多很丰富的重试策略很灵活，推荐使用