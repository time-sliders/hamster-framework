package com.noob.storage.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卢云(luyun)
 * @since 2019.11.18
 */
public class MockOOM {

    public static void main(String[] args) {
        List<int[]> l = new ArrayList<>();
        while (true) {
            int[] i = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            l.add(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
        }
    }

}
