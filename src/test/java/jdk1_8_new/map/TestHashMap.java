package jdk1_8_new.map;

import java.util.HashMap;
import java.util.Map;

public class TestHashMap {

    private static boolean s = false;

    public static void main(String[] args) {

        System.out.println(tab(100));
        System.out.println(100 & 15);
        System.out.println(100 | 15);
        System.out.println(100 ^ 15);

        if(!s){
            return ;
        }

        Map<Integer, String> map = new HashMap<>();

        for (Integer i = 0, j = 0; j < 9; i++) {
            int tab = tab(i);
            if (tab == 1) {
                System.out.println(i + ">>>" + tab);
                map.put(i, "Value");
                j++;
                System.out.println("map.put:key:" + i);
            }
        }

        System.out.println(map);

    }


    private static int tab(Object key) {
        /***
         * 00001100110
         * 00000001111
         * 00000000110
         */
        int h = key.hashCode();
        int p = h >>> 16;
        int r = h ^ p;
        return 15 & r;
    }
}
