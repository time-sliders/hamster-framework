package com.noob.storage.thread.ob;

import com.noob.storage.thread.ThreadPool;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <pre><code>
 * ExecutorService pool = Executors.newFixedThreadPool(2);
 * Counter counter = new Counter();
 * Observer ob = new Observer(pool,counter);
 * ob.add(new MyTask());
 * ob.add(new MyTask());
 * b.start();
 * </code></pre>
 */
public class ObServer {

    private Logger log = Logger.getLogger(getClass());

    /**
     * Global Thread Pool
     */
    private ExecutorService threadPool;

    /**
     * shared memory
     */
    private Counter counter;

    private List<ObServableTask> observableTasks = Collections.synchronizedList(new ArrayList<ObServableTask>());

    private Status status = Status.CREATE;

    public ObServer(){
        this.counter = new Counter();
        this.threadPool = new ThreadPool(0);
    }

    /**
     * @param pool    当前观察者使用线程池
     * @param counter 子线程之间计数以及通讯的共享内存
     */
    public ObServer(ExecutorService pool, Counter counter) {
        this.threadPool = pool;
        this.counter = counter;
    }

    public void add(ObServableTask task) {

        if (status != Status.CREATE) {
            throw new IllegalStateException(status.toString());
        }

        if (!observableTasks.contains(task)) {
            observableTasks.add(task);
            task.setCounter(counter);
            counter.allPlus();
        }
    }

    public void start() throws TaskNotFinishException {
        status = Status.STARTED;
        try {
            for (Runnable command : observableTasks) {
                threadPool.execute(command);
            }

            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait();
            }
        } catch (InterruptedException ignore) {
        } finally {
            log.info(counter);
            status = Status.END;
        }

        if (!counter.isFinish()) {
            throw new TaskNotFinishException();
        }
    }

}
