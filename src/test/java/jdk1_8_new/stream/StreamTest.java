package jdk1_8_new.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest {

    public static void main(String[] args) {

        // Stream 的创建
        /*
        Stream<String> ss;
        ss = new ArrayList<String>().stream();  //java.util.Collection
        ss = Stream.generate(String::new);
        ss = Stream.of("1", "2", "3");

        // Stream 的转换
        ss.filter(s -> s.length() > 5);
        ss.mapToInt(Integer::valueOf);
        ss.sorted((a, b) -> a.length() - b.length());
        ss.limit(10);

        // Stream 的聚合
        ss.collect(Collectors.toList());
        ss.count();
        */

        List<Integer> il = Stream.of("1", "2", "3")
                .mapToInt(Integer::valueOf)
                .filter((i) -> i > 2)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        System.out.println(il);

    }
}
