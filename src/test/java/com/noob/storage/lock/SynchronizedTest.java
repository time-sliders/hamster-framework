package com.noob.storage.lock;

public class SynchronizedTest {

    private final Object o = new Object();

    public static void main(String[] args) {

    }

    public void SyncObject() throws InterruptedException {
        synchronized (o){
            o.wait();
        }
    }

    public void notifyObject(){
        synchronized (o){
            o.notify();
        }
    }

}