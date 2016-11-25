package jdk1_8_new.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    public static void main(String[] args) {

        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");

        /**
         * 创建
         */
        Stream<String> ss = stringList.stream().parallel();

        /**
         * 处理
         */
        IntStream is;
        is = ss.mapToInt(Integer::valueOf);
        is = is.filter((i) -> i < 3);

        /**
         * 结束操作
         */
        boolean isAllMatch = is.allMatch((i) -> i < 5);

        System.out.println(isAllMatch);

    }
}
