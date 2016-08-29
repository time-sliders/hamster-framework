package com.noob.storage.thread.sync;

import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 * @since app6.1
 */
public class SubTask_2 extends SubTask {

    @Override
    protected void doBusiness(ConcurrentMap<String, Object> context) {
        context.put("caption","1234");
        try {
            Thread.sleep(1000*4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass().getSimpleName()+" end");
    }
}
