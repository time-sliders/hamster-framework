package jdk1_8_new.lambda;


import org.apache.commons.lang.StringUtils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestColon {

    public static void main(String[] args) {

        //  ::  是lambda表达式的缩写

        Predicate<String> p1            = s -> StringUtils.isEmpty(s);
        Predicate<String> p2            = StringUtils::isEmpty;

        Function<String, Integer> f1    = (s) -> Integer.valueOf(s);
        Function<String, Integer> f2    = Integer::valueOf;

        Supplier<String> l1             = () -> new String();
        Supplier<String> l2             = String::new;

        Consumer<String> c1             = (s) -> System.out.println(s);
        Consumer<String> c2             = System.out::println;


    }
}
