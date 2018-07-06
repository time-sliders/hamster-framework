package com.noob.storage.utils.send;

import com.noob.storage.common.Millisecond;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶算法
 * <p>
 * SendThread
 * <p>
 * 1.当桶满的时候，等待
 * 2.当有令牌的时候，唤醒等待线程
 * 3.单位时间内最大令牌控制
 *
 * @author LuYun
 * @since 2018.05.10
 */
public abstract class TokenBucket<E> implements BucketInterface<E> {

    private static final long SLEEP_TIME_MILLS = 10L;
    private static final long INITIAL_SEND_COUNT = 0L;

    private long maxTransmissionPerMills;
    private long currentSeconds = 0L;
    private AtomicLong currentMillsSendCount = new AtomicLong(0L);

    public TokenBucket(long maxTransmissionPerMills) {
        this.maxTransmissionPerMills = maxTransmissionPerMills;
    }

    @Override
    public void send(E e) throws InterruptedException {

        long currentTimeSeconds;
        while (true) {

            currentTimeSeconds = System.currentTimeMillis() / Millisecond.ONE_SECONDS;

            if (currentTimeSeconds != this.currentSeconds) {
                this.currentSeconds = currentTimeSeconds;
                currentMillsSendCount.set(INITIAL_SEND_COUNT);

            } else if (currentMillsSendCount.get() >= maxTransmissionPerMills) {
                Thread.sleep(SLEEP_TIME_MILLS);
                continue;
            }
            break;
        }

        currentMillsSendCount.getAndIncrement();
        handleSend(e);
    }

    protected abstract void handleSend(E e);









    public static void main(String[] args) {
        TokenBucketImpl t = new TokenBucketImpl(10);
        int i = 0;
        for(;;){
            i++;
            try {
                t.send(String.valueOf(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TokenBucketImpl extends TokenBucket<String> {

        public TokenBucketImpl(long maxTransmissionPerMills) {
            super(maxTransmissionPerMills);
        }

        @Override
        protected void handleSend(String s) {
            System.out.println(System.currentTimeMillis() / Millisecond.ONE_SECONDS + "  " + s);
        }
    }

}
