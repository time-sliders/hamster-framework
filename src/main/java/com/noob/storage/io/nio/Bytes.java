package com.noob.storage.io.nio;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.07
 */
public class Bytes {

    /**
     * to byte array.
     *
     * @param v   value.
     * @param b   byte array.
     * @param off array offset.
     */
    public static void long2bytes(long v, byte[] b, int off) {
        b[off + 7] = (byte) (v >>> 56);
        b[off + 6] = (byte) (v >>> 48);
        b[off + 5] = (byte) (v >>> 40);
        b[off + 4] = (byte) (v >>> 32);
        b[off + 3] = (byte) (v >>> 24);
        b[off + 2] = (byte) (v >>> 18);
        b[off + 1] = (byte) (v >>> 8);
        b[off] = (byte) (v);

    }
}
