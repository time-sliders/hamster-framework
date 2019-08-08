package com.noob.storage.io.nio.server.serialization;

/**
 * @author 卢云(luyun)
 * @since 2019.08.08
 */
public interface Serialization {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes);
}
