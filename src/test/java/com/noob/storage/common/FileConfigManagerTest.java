package com.noob.storage.common;

import com.noob.storage.common.config.FileConfig;
import com.noob.storage.common.config.FileConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author luyun
 * @since 2016.05.12
 */
public class FileConfigManagerTest {

    public static void main(String[] args) throws IOException {

        Map<String, String> configList = new HashMap<String, String>();

        File path = new File("/Users/zhangwei/test/");
        if (!path.exists() && !path.mkdirs()) {
            return;
        }

        File f = new File("/Users/zhangwei/test/myTestConfig.properties");
        if (!f.exists() && !f.createNewFile()) {
            return;
        }

        configList.put("myTestConfig", "/Users/zhangwei/test/myTestConfig.properties");
        FileConfigManager manager = new FileConfigManager(configList);
        int i = 0;
        while (i++ < 100) {
            FileConfig config = manager.getConfig("myTestConfig");
            if (i % 2 == 1) {
                config.add(String.valueOf(i), "hello" + i);
            } else {
                int r = new Random().nextInt(i - 1);
                config.remove(String.valueOf(r));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {

            }
        }
    }
}
