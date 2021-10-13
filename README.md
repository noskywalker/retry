
##Quickstart
A minimal sample of some of the functionality would look like:

```java
Callable<Boolean> callable = new Callable<Boolean>() {
    public Boolean call() throws Exception {
        return true; // do something useful here
    }
};

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

This will retry whenever the result of the `Callable` is null, if an `IOException` is thrown, or if any other
`RuntimeException` is thrown from the `call()` method. It will stop after attempting to retry 3 times and throw a
`RetryException` that contains information about the last failed attempt. If any other `Exception` pops out of the
`call()` method it's wrapped and rethrown in an `ExecutionException`.

##Exponential Backoff

Create a `Retryer` that retries forever, waiting after every failed retry in increasing exponential backoff intervals
until at most 5 minutes. After 5 minutes, retry from then on in 5 minute intervals.

```java
Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
        .retryIfExceptionOfType(IOException.class)
        .retryIfRuntimeException()
        .withWaitStrategy(WaitStrategies.exponentialWait(100, 5, TimeUnit.MINUTES))
        .withStopStrategy(StopStrategies.neverStop())
        .build();
```
You can read more about [exponential backoff](http://en.wikipedia.org/wiki/Exponential_backoff) and the historic role
it played in the development of TCP/IP in [Congestion Avoidance and Control](http://ee.lbl.gov/papers/congavoid.pdf).

##Fibonacci Backoff

Create a `Retryer` that retries forever, waiting after every failed retry in increasing Fibonacci backoff intervals
until at most 2 minutes. After 2 minutes, retry from then on in 2 minute intervals.

```java
Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
        .retryIfExceptionOfType(IOException.class)
        .retryIfRuntimeException()
        .withWaitStrategy(WaitStrategies.fibonacciWait(100, 2, TimeUnit.MINUTES))
        .withStopStrategy(StopStrategies.neverStop())
        .build();
```

Similar to the `ExponentialWaitStrategy`, the `FibonacciWaitStrategy` follows a pattern of waiting an increasing amount
of time after each failed attempt.

Instead of an exponential function it's (obviously) using a
[Fibonacci sequence](https://en.wikipedia.org/wiki/Fibonacci_numbers) to calculate the wait time.

Depending on the problem at hand, the `FibonacciWaitStrategy` might perform better and lead to better throughput than
the `ExponentialWaitStrategy` - at least according to
[A Performance Comparison of Different Backoff Algorithms under Different Rebroadcast Probabilities for MANETs](http://www.comp.leeds.ac.uk/ukpew09/papers/12.pdf).

The implementation of `FibonacciWaitStrategy` is using an iterative version of the Fibonacci because a (naive) recursive
version will lead to a [StackOverflowError](http://docs.oracle.com/javase/7/docs/api/java/lang/StackOverflowError.html)
at a certain point (although very unlikely with useful parameters for retrying).

Inspiration for this implementation came from [Efficient retry/backoff mechanisms](https://paperairoplane.net/?p=640).

##Documentation
Javadoc can be found [here](http://rholder.github.io/guava-retrying/javadoc/2.0.0).

##Building from source
The guava-retrying module uses a [Gradle](http://gradle.org)-based build system. In the instructions
below, [`./gradlew`](http://vimeo.com/34436402) is invoked from the root of the source tree and serves as
a cross-platform, self-contained bootstrap mechanism for the build. The only
prerequisites are [Git](https://help.github.com/articles/set-up-git) and JDK 1.6+.

### check out sources
`git clone git://github.com/rholder/guava-retrying.git`

### compile and test, build all jars
`./gradlew build`

### install all jars into your local Maven cache
`./gradlew install`

##License
The guava-retrying module is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).

##Contributors
* Jean-Baptiste Nizet (JB)
* Jason Dunkelberger (dirkraft)
* Diwaker Gupta (diwakergupta)
* Jochen Schalanda (joschi)
* Shajahan Palayil (shasts)
* Olivier Grégoire (fror)
* Andrei Savu (andreisavu)
* (tchdp)
* (squalloser)
* Yaroslav Matveychuk (yaroslavm)
* Stephan Schroevers (Stephan202)
* Chad (voiceinsideyou)
* Kevin Conaway (kevinconaway)
* Alberto Scotto (alb-i986)
