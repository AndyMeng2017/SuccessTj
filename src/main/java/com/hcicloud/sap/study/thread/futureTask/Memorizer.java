package com.hcicloud.sap.study.thread.futureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Memorizer<A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memorizer(Computable<A, V> c) {
        this.c = c;
    }

    //先说些题外话，在上面这段代码中，参数和变量的命名都十分的简单，都是几个简单字母组成的。在实际项目的开发中不要使用这样的命名，示例中无妨。
    //Memoizer 这个类的工作就是缓存计算结果，避免计算工作的重复提交。这是一个示例代码，所以没有保护缓存失效等的逻辑。Memoizer 类实现了 Computable 接口。
    //同时，Memoizer 类构造方法里面也要接受一个 Computable 的实现类作为参数，这个 Computable 实现类将去做具体的计算工作。
    //Memoizer 的 compute 方法是我们要关注的主要部分。


    //先跳过 while (true)，第11行是查看缓存中是否已经存在计算任务，如果没有，新的任务才需要被提交，否则获取结果即可。
    //进入 if (f == null)，我们先将 Computable<A, V> c 封装进一个 FutureTask 中。然后调用 ConcurrentMap.putIfAbsent 方法去将计算任务放入缓存。
    //这个方法很关键，因为它是一个原子操作，返回值是 key，所对应的原有的值。如果原有值为空，计算任务才可以被真正启动，否则就会重复执行。最后在 try 中调用计算结果。抛去 while(true) 和 catch，这段代码很容易理解，因为这些都是正常的流程。


    //接下来说说 while(true)，它的作用在于计算任务被取消之后能够再次提交任务。


    //接下来说两个 catch。第一个是 catch CancellationException，但其实在此示例中，FutureTask 都是本地变量，也都没有调用 cancel 方法，所以程序没有机会执行到这里，所以这块只是起到了示例的作用。
    //需要注意的是第二个 catch。第二个 catch 捕获的是 ExecutionException，封装在 FutureTask 之内的 Runnable 或 Callable 执行时所抛出的异常都会被封装在 ExecutionException 之中，
    //可以通过 e.getCause() 取的实际的异常。显然，发生 ExecutionException 时，计算显然是没有结果的，而在此示例代码中，异常只是简单地被再次抛出。这会导致计算结果无法取得，
    //而且缓存仍旧被占用，新的计算任务无法被提交。如果 c.compute 是幂等的，那这样做是合理的，因为在此提交的任务还是会导致异常的发生。
    //但如果不是幂等的，比如一些偶然事件，比如网络断开等。这里就需要把计算任务从缓存中移除，使得新的任务可以提交进来。
    //在实际应用中，我们还需要根据具体的异常类型，做不同的处理。如果你不清楚 c.compute 是否是幂等的（因为你无法限制传进来的 Computable 实现有何特性），你可以限制一个重试次数。当重试超过限制，便不再移除缓存。

    @Override
    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (null == f) {
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };

                FutureTask<V> ft = new FutureTask<V>(eval);
                //若没有这个key则put。总是返回oldValue
                f = cache.putIfAbsent(arg, ft);
                if (null == f) {
                    f = ft;
                    ft.run();
                }
            }

            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }

    private InterruptedException launderThrowable(Throwable cause) {
        if (cause instanceof InterruptedException) {
            return (InterruptedException) cause;
        }
        return new InterruptedException(cause.getMessage());
    }
}

