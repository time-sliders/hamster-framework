package jdk1_8_new.functionInterface;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestDiff {

    public static void main(String[] args) {
        Predicate<String> p =               s -> s != null;
        Function<String, String> f =        s -> s + "a";
        Supplier<String>  l =               () -> "a";
        Consumer<String> c =                s -> s = s + "a";

        Consumer<String> c1 =       System.out::print;
        Supplier<Exception> s1 =    RuntimeException::new;

        p.test("Java");
//        (s -> s != null).test("Javas");
//        ((String s) -> s != null).test("Java");

        NewPredicate<String> p2 =           s -> s != null;
        p2.assertNotNull("Java");

    }

    @FunctionalInterface
    interface NewPredicate<T>{
        boolean assertNotNull(T t);
    }

}
