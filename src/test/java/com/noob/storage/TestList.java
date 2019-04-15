package com.noob.storage;

import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author luyun
 * @version 1.0
 * @since 2018.01.06
 */
public class TestList {

    private static final String S = "Ssssss";
    private static final int dataSize = 750;


    @Test
    public synchronized void testArrayList() {
        StopWatch sw = new StopWatch();
        List<String> list001 = new ArrayList<String>();
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

    @Test
    public void testLinkedList() {
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
