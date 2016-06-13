package jdk1_8_new.defaultInterface;

import java.util.function.Predicate;

public class PredicateTest {

    public static void main(String[] args) {
        String name = "zhw";

        Predicate<String> predicate = (s) -> s.length() > 0;

        System.out.println(predicate.test(name));              // true
        System.out.println(predicate.negate().test(name));     // false

        Predicate<String> isEmpty = String::isEmpty;
        System.out.println(isEmpty.test(name));

        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println(isNotEmpty.test(name));
    }

}
