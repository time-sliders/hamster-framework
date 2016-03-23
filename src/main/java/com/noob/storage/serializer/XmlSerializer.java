package com.noob.storage.serializer;

import java.util.List;

/**
 * TODO Xml序列化
 *
 * @author luyun
 * @since 2016.03.23
 */
public class XmlSerializer implements Serializer {

    public byte[] serialization(Object object) {
        return new byte[0];
    }

    public <T> T deserialization(byte[] byteArray, Class<T> c) {
        return null;
    }

    public <E> List<E> deserializationList(byte[] byteArray, Class<E> elementC) {
        return null;
    }
}
