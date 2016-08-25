package com.noob.storage.thread.sync;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 * @since app6.1
 */
public class SubTask_1 extends SubTask {

    @Override
    void doBusiness(ConcurrentMap<String, Object> context) {
        context.put("income", BigDecimal.ONE);
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass().getSimpleName()+" end");
    }
}
