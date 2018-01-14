package com.noob.storage;

import org.springframework.util.StopWatch;
import sun.misc.Unsafe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author luyun
 * @version 1.0
 * @since 2018.01.06
 */
public class TestList {

    private static Unsafe unsafe;
    private static final String S = "Ssssss";
    private static final int dataSize = 7500000;

    static {
        Unsafe unsafe;
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Throwable t) {
            unsafe = null;
        }
    }

    public static void main(String[] args) throws IOException {
        /*
         * 性能分析
         * testArrayList();
         * testLinkedList();
         **/
        testArrayList();
        testLinkedList();

        /*
         * 内存分析 X 这个只是对象大小，不是内存大小
         */
        //testMemory();

    }

    private static void testMemory() throws IOException {
        int initialSize = 1000001;
        //System.out.println(initialSize);
        List<String> list001 = new ArrayList<String>(initialSize);
        for (int i = 0; i < dataSize; i++) {
            list001.add(S);//修改
        }
        System.out.println("ArrayList:size>>>" + printObjectMemorySize(list001));

        List<String> list002 = new LinkedList<String>();
        for (int i = 0; i < dataSize; i++) {
            list002.add(S);//修改
        }
        System.out.println("LinkedList:size>>>" + printObjectMemorySize(list002));
    }


    public static int printObjectMemorySize(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        byte[] buffer = bos.toByteArray();
        return buffer.length;
    }


    public static void testArrayList() {
        StopWatch sw = new StopWatch();
        int initialSize = 10000001;
        //System.out.println(initialSize);
        List<String> list001 = new ArrayList<String>(initialSize);
        sw.start();
        for (int i = 0; i < dataSize; i++) {
            list001.add(S);//修改
        }
        sw.stop();
        System.out.println("ArrayList:add Performance:" + list001.size() + "|" + sw.getTotalTimeMillis());
        sw.start();
        for (int i = 0; i < dataSize; i++) {
            list001.get(i);//查询
        }
        sw.stop();
        System.out.println("ArrayList:get Performance:" + list001.size() + "|" + sw.getTotalTimeMillis());
    }

    public static void testLinkedList() {
        StopWatch sw = new StopWatch();
        List<String> list = new LinkedList<String>();
        sw.start();
        for (int i = 0; i < dataSize; i++) {
            list.add(S);//修改
        }
        sw.stop();
        System.out.println("LinkedList:add Performance:" + list.size() + "|" + sw.getTotalTimeMillis());
        sw.start();
        for (int i = 0; i < dataSize; i++) {
            // if (i % 1000 == 0) System.out.println(i);
            list.get(i);// 查询 这里查询非常慢,i < (size >> 1) 首尾处理
        }
        sw.stop();
        System.out.println("LinkedList:get Performance:" + list.size() + "|" + sw.getTotalTimeMillis());

    }



}
