package jdk1_8_new.defaultInterface;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ConsumerTest {

    @Test
    public void testListIterator() {

        List<String> list = new LinkedList<>();

        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        // 原来的写法
        for (String s : list) {
            System.out.println(s);
        }

        // 现在的写法
        list.forEach(System.out::println);
    }
}
