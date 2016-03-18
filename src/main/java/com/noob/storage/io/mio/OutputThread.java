package com.noob.storage.io.mio;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class OutputThread extends Thread {

    private Logger log = Logger.getLogger(OutputThread.class);

    /**
     * 输出流
     */
    private OutputStream os;

    /**
     * 当前线程读取位置
     */
    private int cursor = 0;

    private MIOEngine engine;

    public void run() {
        DataBuffer buffer;
        while (true) {
            if (cursor < this.engine.getMaxKey()) {
                buffer = this.engine.getBuffers().get(cursor++);
                try {
                    this.os.write(buffer.getBuffer(), 0, buffer.getCount());
                } catch (IOException e) {
                    engine.getOutputThreads().remove(this);
                    log.error(Thread.currentThread().getName() + "/t 输出线程意外终止:" + e.getMessage(), e);
                    break;
                }
            }
            if (!this.engine.isAllDataReceived()) {
                synchronized (this) {
                    try {
                        /**这里不可使用wait()方法
                         * 如果if条件符合进入该if代码块之后，engine在wait之前读完
                         * 最后一个buffer并且完成2次notify之后，子线程才执行到wait()
                         * 方法，会导致子线程无法唤醒，添加wait时间防止这种情况
                         * */
                        this.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //所有数据接收完 并且已经全部写出则跳出
            } else if (cursor >= this.engine.getMaxKey()) {
                try {
                    this.os.close();
                } catch (IOException ignore) {
                }
                break;
            }
        }
        engine.getOutputThreads().remove(this);
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public MIOEngine getEngine() {
        return engine;
    }

    public void setEngine(MIOEngine engine) {
        this.engine = engine;
    }

}
