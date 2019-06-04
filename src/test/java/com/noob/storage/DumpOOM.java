package com.noob.storage;

import java.util.LinkedList;

/**
 * @author 卢云(luyun)
 * @version 1.0
 * @since 2019.06.04
 */
public class DumpOOM {
    public static void main(String[] args) {
        LinkedList<Integer[]> l = new LinkedList<>();
        int i = 128, x = 0;

        try {
            while (++x > 0) {
                // 8 + 8 + 8 + 8 + 8 * 8 = 64 + 32
                // 8 + 8 + 8 + 4 = 28 * 8 = 224 + 64 + 32 = 320
                l.add(new Integer[]{i++, i++, i++, i++, i++, i++, i++, i++});
                System.out.println(l.size());
            }
        } finally {
            System.out.println("x " + x);
            System.out.println("l " + l.size());
        }
    }
}
