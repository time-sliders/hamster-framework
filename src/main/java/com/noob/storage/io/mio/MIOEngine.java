package com.noob.storage.io.mio;

import com.noob.storage.exception.ProcessException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 异步流处理引擎<br/>
 * 该类提供一输入流同时输出到多个输出流的功能，输入流输出流互不干涉
 * <p>
 * 你可以这样使用:
 * <hr>
 * <pre><strong>
 * InputStream is ……;
 * OutputStream os1 ……;
 * OutputStream os2 ……;
 * MIOEngine engine = new MIOEngine();
 * engine.setInputStream(is);
 * engine.addOutputStream(os1);
 * engine.addOutputStream(os2);
 * engine.start();
 * </strong></pre>
 * <hr>
 */
public class MIOEngine {

    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 单个数据块的大小，默认64K
     */
    int size = 1024 * 64;

    /**
     * 数据缓冲队列
     */
    private Map<Integer, DataBuffer> buffers =
            Collections.synchronizedMap(new HashMap<Integer, DataBuffer>(0));

    /**
     * 所有输出子线程
     */
    private List<OutputThread> outputThreads = new ArrayList<OutputThread>(0);

    /**
     * 引擎是否已经启动
     */
    private AtomicBoolean isStarted = new AtomicBoolean(false);

    /**
     * 输入流数据是否已经全部读取完毕
     */
    private boolean isAllDataReceived = false;

    private int maxKey = 0;

    public void addOutputStream(OutputStream os) {
        if (isStarted.get()) {
            throw new ProcessException(
                    "engine is already started! refuse OutputStream add to engine!");
        }
        OutputThread ot = new OutputThread();
        ot.setOs(os);
        ot.setEngine(this);
        this.outputThreads.add(ot);
    }

    /**
     * 输入流开始读取数据 启动输出流并开始写出数据
     */
    public void start() {
        if (isStarted.compareAndSet(false, true)) {
            throw new ProcessException(
                    "engine is already started!");
        }
        int count;
        byte[] buffer;
        try {
            /**
             * while循环必须要new 一个新的byte[]
             * 不然缓冲队列buffers里面全是同一个对象
             * */
            while ((count = inputStream.read(buffer = new byte[size])) != -1) {
                buffers.put(maxKey++, new DataBuffer(buffer, count));
                // 第一次传入数据之后启动所有子线程
                if (maxKey == 1) {
                    startAllOutputThread();
                }
                notifyAllOutputThread();
                removeAbandonBuffer(getMinCursor());
            }
            setAllDataReceived(true);
            notifyAllOutputThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动所有子线程
     */
    private void startAllOutputThread() {
        for (OutputThread outputThread : outputThreads) {
            outputThread.start();
        }
    }

    /**
     * 唤醒全部子线程
     */
    public void notifyAllOutputThread() {
        for (OutputThread outputThread : outputThreads) {
            synchronized (outputThread) {
                outputThread.notify();
            }
        }
    }

    /**
     * 获取所有子线程中读取的最小位置 用于清除缓冲区中已经全部写出的数据块
     */
    public int getMinCursor() {
        if (outputThreads == null || outputThreads.isEmpty()) {
            return 0;
        }
        int minCursor = Integer.MAX_VALUE;//1310T
        for (OutputThread outputThread : outputThreads) {
            minCursor = outputThread.getCursor() < minCursor ? outputThread.getCursor() : minCursor;
        }
        return minCursor;
    }


    private int removeFlag = 0;

    /**
     * 删除缓冲队列中已经被所有输出流写出的数据块
     * 防止输入流数据量过大导致系统内存超高
     */
    public void removeAbandonBuffer(int minCursor) {
        for (int i = removeFlag; i < minCursor; i++) {
            buffers.remove(i);
        }
        removeFlag = minCursor;
    }

    public void setInputStream(InputStream is) {
        if (isStarted.get()) {
            throw new ProcessException(
                    "engine is already started! refuse InputStream add to engine!");
        }
        this.inputStream = is;
    }

    public boolean isAllDataReceived() {
        return isAllDataReceived;
    }

    private void setAllDataReceived(boolean isAllDataReceived) {
        this.isAllDataReceived = isAllDataReceived;
    }

    public Map<Integer, DataBuffer> getBuffers() {
        return buffers;
    }

    public List<OutputThread> getOutputThreads() {
        return outputThreads;
    }

    public int getMaxKey() {
        return maxKey;
    }

}
