package jdk1_8_new.lock;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTest {

    public static void main(String[] args) {
        StampedLock lock = new StampedLock();
        lock.tryReadLock();
    }

}
