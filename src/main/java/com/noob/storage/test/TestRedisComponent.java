package com.noob.storage.test;

import com.noob.storage.common.Millisecond;
import com.noob.storage.component.RedisComponent;
import org.springframework.context.ApplicationContext;

/**
 * @author 卢云(luyun)
 * @version TPro
 * @since 2019.05.15
 */
public class TestRedisComponent {

    public static void TestRedis(ApplicationContext ac) {
        RedisComponent redisComponent = null;
        boolean isLocked = false;
        String lockKey = "LOCK_TEST";

        try {
            redisComponent = ac.getBean("redisComponent", RedisComponent.class);
            isLocked = redisComponent.acquireLock(lockKey, Millisecond.TEN_MINUS);
            System.out.println("jdbc test successfully > isLocked: " + isLocked);

        } catch (Throwable e) {
            System.out.println(e.getMessage());

        } finally {
            if (isLocked) {
                redisComponent.releaseLock(lockKey);
            }
        }
    }
}
