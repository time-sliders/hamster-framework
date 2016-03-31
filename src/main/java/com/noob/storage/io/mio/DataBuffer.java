package com.noob.storage.io.mio;

/**
 * 数据块单元
 */
public class DataBuffer {

    /**
     * 数据存储buffer
     */
    private byte[] buffer;

    /**
     * buffer 里面的有效数据量<br/>
     * 需要注意的是inputStream.read(buffer = new byte[size]))<br/>
     * 即使在inputStream读完之前，也并不一定能将buffer读满，所以需要<br/>
     * count字段来记录buffer里面的有效数据
     */
    private int count;

    public DataBuffer(byte[] buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
