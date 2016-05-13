package com.noob.storage.component;

import org.junit.Test;

/**
 * @author luyun
 * @since 2016.05.12
 */
public class FilePersistentComponentTest {

    @Test
    public void testWriteWithTmp() throws Exception {
        FilePersistentComponent.writeWithTmp("/Users/zhangwei/test/","FilePersistentComponent","hello");
    }

}