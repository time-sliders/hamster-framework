package jdk1_8_new.lock;

import org.junit.Test;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTest {

    /**
     * state 初始值 256
     * 每锁定一次 值 +1
     * 每释放一次 值 -1
     */
    @Test
    public void test1() {
        StampedLock lock = new StampedLock();
        long stamp1 = lock.readLock();
        long stamp2 = lock.readLock();
        long stamp3 = lock.readLock();
        long stamp4 = lock.readLock();
        long stamp5 = lock.readLock();

        lock.unlock(stamp1);
        lock.unlock(stamp2);
        lock.unlock(stamp3);
        lock.unlock(stamp4);
        lock.unlock(stamp5);
        System.out.println(lock.isReadLocked());
    }

    /**
     * unlock 与 stamp
     * 257-382
     */
    @Test
    public void test2() {
        StampedLock lock = new StampedLock();
        long stamp1 = lock.readLock();
        lock.unlock(383L);
        System.out.println(lock.isReadLocked());
    }

    // read over flow
    @Test
    public void test3() {
        StampedLock lock = new StampedLock();
        long stamp;
        for (int i = 0; i < 126; i++) {
            stamp = lock.readLock();
            System.out.println(stamp);
        }

        for (int i = 0; i < 10; i++) {
            stamp = lock.readLock();
            System.out.println(stamp);
        }

        System.out.println();
        for (int i = 0; i < 130; i++) {
            lock.unlock(258);
        }
        System.out.println();
    }

    /**
     * 读锁阻塞写锁,写锁阶段性公平策略
     * <p>
     * 384
     */
    @Test
    public void test4() {
        StampedLock lock = new StampedLock();
        long stamp;
        stamp = lock.writeLock();
        System.out.println(stamp);
    }

    // 使用
    @Test
    public void test0() {

        StampedLock lock = new StampedLock();

        // 乐观读
        long stamp = lock.tryOptimisticRead();

        if (stamp != 0L) {

            // 这里用本地变量存储读到的数据
            // do something

            if (!lock.validate(stamp)) {

                stamp = lock.readLock();

                try {

                    // 重新去读取数据

                } finally {
                    lock.unlockRead(stamp);
                }
            }
        }

    }

}

