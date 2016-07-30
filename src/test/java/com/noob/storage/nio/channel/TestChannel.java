package com.noob.storage.nio.channel;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author luyun
 * @since 2016.06.16
 */
public class TestChannel {

    @Test
    public void testCopyFileWithChannel() throws Exception {

        FileChannel ic = new FileInputStream(new File("/Users/zhangwei/test/nio/1")).getChannel();
        FileChannel oc = new FileOutputStream(new File("/Users/zhangwei/test/nio/2")).getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (ic.read(buffer) != -1) {
            buffer.flip();
            oc.write(buffer);
            buffer.clear();
        }

        ic.close();
        oc.close();

    }

    @Test
    public void testGather() throws Exception {
        FileChannel oc = new FileOutputStream(new File("/Users/zhangwei/test/nio/3")).getChannel();
        ByteBuffer b1 = ByteBuffer.allocate(5);
        b1.put("zhw".getBytes());
        b1.flip();
        ByteBuffer b2 = ByteBuffer.allocate(5);
        b2.put(" was ".getBytes());
        b2.flip();
        ByteBuffer b3 = ByteBuffer.allocate(5);
        b3.put("here!".getBytes());
        b3.flip();
        ByteBuffer[] buffers = new ByteBuffer[]{b1, b2, b3};
        oc.write(buffers);
        b1.clear();
        b2.clear();
        b3.clear();
        oc.close();
    }

    @Test
    public void testScatter() throws Exception {
        FileChannel ic = new FileInputStream(new File("/Users/zhangwei/test/nio/3")).getChannel();
        ByteBuffer b1 = ByteBuffer.allocate(5);
        ByteBuffer b2 = ByteBuffer.allocate(3);
        ByteBuffer b3 = ByteBuffer.allocate(5);
        ByteBuffer split1 = ByteBuffer.allocate(1);
        split1.put("|".getBytes());
        ByteBuffer split2 = ByteBuffer.allocate(1);
        split2.put("|".getBytes());
        ByteBuffer[] buffers1 = new ByteBuffer[]{b1, b2, b3};
        ic.read(buffers1);
        FileChannel oc = new FileOutputStream(new File("/Users/zhangwei/test/nio/4")).getChannel();
        ByteBuffer[] buffers2 = new ByteBuffer[]{b1, split1, b2, split2, b3};
        b1.flip();
        b2.flip();
        b3.flip();
        split1.flip();
        split2.flip();
        oc.write(buffers2);
        ic.close();
        oc.close();
    }

    /*
    * <p> This method is potentially much more efficient than a simple loop
    * that reads from the source channel and writes to this channel.  Many
    * operating systems can transfer bytes directly from the source channel
    * into the filesystem cache without actually copying them.  </p>
    */
    @Test
    public void testTransfer() throws Exception {
        FileChannel ic = new FileInputStream(new File("/Users/zhangwei/test/nio/1")).getChannel();
        File of = new File("/Users/zhangwei/test/nio/5");
        long x = of.length();
        FileChannel oc = new FileOutputStream(of).getChannel();
        System.out.println(oc.transferFrom(ic, oc.position(), x));
        ic.close();
        oc.close();
    }

}
